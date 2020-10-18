package com.example.android.runningtrackerapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.runningtrackerapp.R
import com.example.android.runningtrackerapp.adapters.RunAdapter
import com.example.android.runningtrackerapp.other.SortType
import com.example.android.runningtrackerapp.other.TrackingUtility
import com.example.android.runningtrackerapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_run.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RunFragment: Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var runAdapter: RunAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissions()

        setupRecyclerView()

        spFilter.setSelection(viewModel.currSortType.ordinal)

        spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val sortType = SortType.values()[position]
                viewModel.sortRuns(sortType)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        viewModel.runs.observe(viewLifecycleOwner, Observer {
            runAdapter.submitListDiff(it)
        })

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment2)
        }
    }


    private fun setupRecyclerView() = rvRuns.apply {
        runAdapter = RunAdapter()
        adapter = runAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun requestPermissions() {
        if(TrackingUtility.hasLocationPermissions(requireContext() /*best practice for context within fragment*/))
            return
        TrackingUtility.requestLocationPermissions(this)

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(this).build().show() //lead to App Settings to toggle back the permissions
        } else {
            requestPermissions()
        }
    }

    //we must override and notify external EasyPermissions lib about the permissions data being handled//
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}