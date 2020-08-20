package ru.nikstep.alarm.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule(private val context: Context) {

    @Provides
    @Singleton
    fun providesContext(): Context = context

}