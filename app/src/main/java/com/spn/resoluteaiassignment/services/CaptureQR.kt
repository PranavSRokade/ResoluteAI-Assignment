package com.spn.resoluteaiassignment.services

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import com.journeyapps.barcodescanner.CaptureActivity
import com.spn.resoluteaiassignment.ScanQRCodeActivity

class CaptureQR : CaptureActivity(){
    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this@CaptureQR, ScanQRCodeActivity::class.java))
        finishAffinity()
    }
}