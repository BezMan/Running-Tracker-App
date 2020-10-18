package com.example.android.runningtrackerapp.other

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Build
import androidx.fragment.app.Fragment
import com.example.android.runningtrackerapp.other.Constants.LOCATION_PERMISSION_TEXT
import com.example.android.runningtrackerapp.other.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.android.runningtrackerapp.services.Polyline
import pub.devrel.easypermissions.EasyPermissions
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.concurrent.TimeUnit

object TrackingUtility {

    fun hasLocationPermissions(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )

        }


    fun requestLocationPermissions(fragment: Fragment) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                fragment,
                LOCATION_PERMISSION_TEXT,
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                fragment,
                LOCATION_PERMISSION_TEXT,
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )

        }


    fun getFormattedTimer(ms: Long, includeMillis: Boolean = false): String {
        var millis = ms
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        millis -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        millis -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis)

        val f: NumberFormat = DecimalFormat("00")

        if (!includeMillis) {
            return "${f.format(hours)}:${f.format(minutes)}:${f.format(seconds)}"
        }

        millis -= TimeUnit.SECONDS.toMillis(seconds)
        millis /= 10 //display only 2 digit val

        return "${f.format(hours)}:${f.format(minutes)}:${f.format(seconds)}:${f.format(millis)}"
    }


    fun calcPolylineLength(polyline: Polyline): Float {
        var distance = 0f
        for (i in 0..polyline.size - 2) {
            val p1 = polyline[i]
            val p2 = polyline[i + 1]

            val result = FloatArray(1) // init result for each polyline

            Location.distanceBetween(
                p1.latitude,
                p1.longitude,
                p2.latitude,
                p2.longitude,
                result //result just saves the current distance between p1 and p2
            )
            distance += result[0]
        }
        return distance
    }

}