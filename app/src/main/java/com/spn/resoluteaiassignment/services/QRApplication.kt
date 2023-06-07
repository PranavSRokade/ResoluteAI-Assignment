package com.spn.resoluteaiassignment.services

import android.app.Application
import android.content.Intent
import com.google.zxing.client.android.BuildConfig
import com.spn.resoluteaiassignment.ScanQRCodeActivity
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level
import org.koin.dsl.module

class QRApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@QRApplication)
            modules(module {
//                viewModel { ApplicationViewModel(androidApplication()) }
            })
        }
    }
}