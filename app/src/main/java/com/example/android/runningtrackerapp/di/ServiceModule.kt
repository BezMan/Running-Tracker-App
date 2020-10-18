package com.example.android.runningtrackerapp.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.android.runningtrackerapp.R
import com.example.android.runningtrackerapp.other.Constants
import com.example.android.runningtrackerapp.ui.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    @ServiceScoped
    fun provideNotificationManager(@ApplicationContext app: Context) =
        app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    @ServiceScoped
    fun provideFusedLocationClient(@ApplicationContext app: Context) =
        FusedLocationProviderClient(app)

    @Provides
    @ServiceScoped
    fun provideMainActivityPendingIntent(@ApplicationContext app: Context): PendingIntent =
        PendingIntent.getActivity(
            app,
            0,
            Intent(app, MainActivity::class.java).also {
                it.action = Constants.ACTION_SHOW_TRACKING_FRAGMENT
            }, PendingIntent.FLAG_UPDATE_CURRENT
        )


    @Provides
    @ServiceScoped
    fun provideBaseNotificationBuilder(@ApplicationContext app: Context, pendingIntent: PendingIntent)
            : NotificationCompat.Builder =
        NotificationCompat.Builder(app, Constants.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentTitle("Running App")
            .setContentText("00:00:00")
            .setContentIntent(pendingIntent)


}