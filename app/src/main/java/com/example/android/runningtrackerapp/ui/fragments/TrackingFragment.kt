package com.example.android.runningtrackerapp.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.runningtrackerapp.R
import com.example.android.runningtrackerapp.db.Run
import com.example.android.runningtrackerapp.other.Constants
import com.example.android.runningtrackerapp.other.TrackingUtility
import com.example.android.runningtrackerapp.services.Polyline
import com.example.android.runningtrackerapp.services.TrackingService
import com.example.android.runningtrackerapp.ui.viewmodels.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking.*
import java.util.*
import javax.inject.Inject
import kotlin.math.round

const val DIALOG_TAG = "DIALOG_TAG"

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private val viewModel: MainViewModel by viewModels()

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()

    private var map: GoogleMap? = null

    private var currentTimeInMillis = 0L

    private var menu: Menu? = null

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_tracking_menu, menu)
        this.menu = menu
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (!TrackingService.isFirstRun) {
            this.menu?.findItem(R.id.cancelTracking)?.isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cancelTracking -> showCancelTrackingDialog()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showCancelTrackingDialog() {
        DialogCancelTracking().apply {
            setListener { stopRun() }
        }.show(parentFragmentManager, DIALOG_TAG)
    }

    private fun stopRun() {
        tvTimer.text = "00:00:00:00"
        sendCommandToService(Constants.ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView?.onCreate(savedInstanceState)

        btnStopStartRun.setOnClickListener {
            toggleServiceRun()
        }

        btnEndRun.setOnClickListener {
            zoomViewTrackBounds()
            endRunAndSaveToDB()
        }

        mapView?.getMapAsync {
            map = it
            addAllPolylines()
        }

        subscribeToObservers()

        if (savedInstanceState != null) { //dialog logic should continue on rotate.
            val dialogCancelTracking =
                parentFragmentManager.findFragmentByTag(DIALOG_TAG) as DialogCancelTracking?
            dialogCancelTracking?.setListener { stopRun() }
        }

    }


    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            //display time only if we started the tracking service
            if(!TrackingService.isFirstRun) {
                currentTimeInMillis = it
                val formattedTime = TrackingUtility.getFormattedTimer(currentTimeInMillis, true)
                tvTimer.text = formattedTime
            }
        })
    }


    private fun toggleServiceRun() {
        if (isTracking) {
            sendCommandToService(Constants.ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE)
        }
    }


    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (isTracking) {
            menu?.findItem(R.id.cancelTracking)?.isVisible = true
            btnStopStartRun.text = "Stop"
            btnEndRun.visibility = View.GONE
        } else if (!TrackingService.isFirstRun) {
            btnStopStartRun.text = "Start"
            btnEndRun.visibility = View.VISIBLE
        }
    }


    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    Constants.MAP_ZOOM
                )
            )
        }
    }


    private fun zoomViewTrackBounds() {
        val bounds = LatLngBounds.builder()
        for (polyline in pathPoints) {
            for (pos in polyline) {
                bounds.include(pos)
            }
        }
        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                mapView.width,
                mapView.height,
                (mapView.height * 0.05f).toInt() //padding for mapView
            )
        )
    }

    private fun endRunAndSaveToDB() {
        map?.snapshot { bmp ->
            var distanceInMeters = 0

            for (polyline in pathPoints) {
                distanceInMeters += TrackingUtility.calcPolylineLength(polyline).toInt()
            }
            val avgSpeed = round((distanceInMeters / 1000f) / (currentTimeInMillis / 1000f / 3600) * 10) / 10f

            val dateTimestamp = Calendar.getInstance().timeInMillis

            val weight = sharedPref.getFloat(Constants.KEY_WEIGHT, 80f)
            val caloriesBurned = ((distanceInMeters / 1000f) * weight).toInt()

            val run = Run(bmp, dateTimestamp, avgSpeed, distanceInMeters, currentTimeInMillis, caloriesBurned)

            viewModel.insertRun(run)
            Snackbar.make(
                requireActivity().findViewById(R.id.rootView),
                "Run saved successfully", Snackbar.LENGTH_LONG).show()

            stopRun()
        }
    }


    private fun addAllPolylines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(Constants.POLYLINE_COLOR)
                .width(Constants.POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last()[pathPoints.last().size - 1]

            val polylineOptions = PolylineOptions()
                .color(Constants.POLYLINE_COLOR)
                .width(Constants.POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)

            map?.addPolyline(polylineOptions)
        }
    }


    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }


    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}