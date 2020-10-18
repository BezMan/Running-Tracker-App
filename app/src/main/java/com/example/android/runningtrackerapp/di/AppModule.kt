package com.example.android.runningtrackerapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.android.runningtrackerapp.db.RunDatabase
import com.example.android.runningtrackerapp.other.Constants.DATABASE_NAME
import com.example.android.runningtrackerapp.other.Constants.SHARED_PREFS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRunDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(
        app, RunDatabase::class.java, DATABASE_NAME
    ).build()


    @Provides
    @Singleton
    fun provideRunDao(db: RunDatabase) = db.getRunDao()


    @Provides
    @Singleton
    fun provideSharePrefs(@ApplicationContext app: Context): SharedPreferences =
        app.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)


}