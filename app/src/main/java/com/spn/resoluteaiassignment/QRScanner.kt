package com.spn.resoluteaiassignment

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.spn.resoluteaiassignment.services.CaptureQR


class QRScanner : ComponentActivity() {
    lateinit var qrLauncher: ActivityResultLauncher<ScanOptions>
    lateinit var launcher: ActivityResultLauncher<String>
    val db = Firebase.firestore

//    var hasCamPermission =
//        ContextCompat.checkSelfPermission(
//            this@QRScanner,
//            Manifest.permission.CAMERA
//        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        qrLauncher = registerForActivityResult(
            ScanContract()
        ) { result: ScanIntentResult ->
            val dialog = AlertDialog.Builder(this, R.style.DialogWindowTheme)
            dialog.setMessage("Scanned Successfully\nDo you want to save the information in the QR Code?")

            dialog.setPositiveButton(
                "Yes"
            ) { _, _ ->
                val information = result.contents.toString()

                db.collection("QRCodeInformation")
                    .add(mapOf(information to information))
                    .addOnSuccessListener { documentReference ->
                        Log.d("TAG38", "DocumentSnapshot added with ID: ${documentReference.id}")

                        val intent = Intent(
                            this@QRScanner,
                            ScanQRCodeActivity::class.java
                        )

                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        val toast = "There was an error adding the information. Try Again"
                        val centeredText = SpannableString(toast)
                        centeredText.setSpan(
                            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, toast.length - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE
                        )
                        Toast.makeText(applicationContext, centeredText, Toast.LENGTH_SHORT).show()
                    }
            }

            dialog.setNegativeButton(
                "No"
            ) { _, _ ->
                val intent = Intent(
                    this@QRScanner,
                    ScanQRCodeActivity::class.java
                )

                startActivity(intent)
                finish()
            }

//            dialog.setOnDismissListener {
//                val intent = Intent(
//                    this@QRScanner,
//                    ScanQRCodeActivity::class.java
//                )
//                startActivity(intent)
//                finish()
//            }

            Log.d("TAG73", result.contents.toString())

            dialog.show()
        }

        launcher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//                hasCamPermission = isGranted
            }

        launcher.launch(Manifest.permission.CAMERA)

//        if (hasCamPermission) {
        scanCode()
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                hasCamPermission = true
            } else {
//                hasCamPermission = false
            }
        }
    }

    private fun scanCode() {
        val scanOptions = ScanOptions()
        scanOptions.setBeepEnabled(false) //beep when read

        scanOptions.setOrientationLocked(true) //will work in portrait mode

        scanOptions.captureActivity = CaptureQR::class.java
        qrLauncher.launch(scanOptions)
    }
}