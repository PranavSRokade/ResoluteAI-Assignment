package com.spn.resoluteaiassignment

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.content.pm.PackageManager
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.spn.resoluteaiassignment.services.CaptureQR

class ScanQRCodeActivity : AppCompatActivity() {
    lateinit var scan: Button
    lateinit var loader: ProgressBar
    lateinit var emptyLayout: View

    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: Adapter? = null
    lateinit var recyclerView: RecyclerView

    var qrCodeInformationList = ArrayList<String>()

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qrcode)

        supportActionBar?.title = "Scanned QR Codes Data"

        scan = findViewById(R.id.scan_qr)
        recyclerView = findViewById(R.id.recycler_view)
        loader = findViewById(R.id.loader)
        emptyLayout = findViewById(R.id.empty_layout)

        loader.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE
        emptyLayout.visibility = View.INVISIBLE


        db.collection("QRCodeInformation")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val info = document.data.values.toString().replace("[", "").replace("]", "")
                    qrCodeInformationList.add(info)
                }

                if(!qrCodeInformationList.isEmpty()){
                    layoutManager = LinearLayoutManager(this)
                    recyclerView.layoutManager = layoutManager

                    adapter = Adapter(qrCodeInformationList)
                    recyclerView.adapter = adapter
                    recyclerView.visibility = View.VISIBLE
                }
                else{
                    emptyLayout.visibility = View.VISIBLE
                }

                loader.visibility = View.INVISIBLE
            }
            .addOnFailureListener { exception ->
                Log.w("TAG38", "Error getting documents.", exception)
            }

        scan.setOnClickListener {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(Intent(this@ScanQRCodeActivity, QRScanner::class.java))
            } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                val dialog = AlertDialog.Builder(this, R.style.DialogWindowTheme)
                dialog.setMessage("Camera Permission is needed to scan QR Codes. Enable it from the settings.")

                dialog.setPositiveButton(
                    "Enable"
                ) { _, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }

                dialog.setNegativeButton(
                    "Deny"
                ) { _, _ ->
                }

                dialog.show()
            }
        }
    }

    fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            startActivity(Intent(this@ScanQRCodeActivity, QRScanner::class.java))
        }
    }
}