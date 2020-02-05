package com.sut.smart_bin

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler

class ScannerActivity : AppCompatActivity(),ResultHandler{

    private val REQUES_CAMERA = 1
    private var scannerView: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        if (!checkPermission())
            requestPermission()

    }
    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this@ScannerActivity,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            REQUES_CAMERA
        )
    }
    override fun onResume() {
        super.onResume()
        if (checkPermission()) {
            if (scannerView == null) {
                scannerView = ZXingScannerView(this)
                setContentView(scannerView)
            }
            scannerView?.setResultHandler(this)
            scannerView?.startCamera()
        }

    }
    override fun handleResult(p0: Result?) {
        val result = p0?.text
        val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE)as Vibrator
        vibrator.vibrate(1000)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Result")
        builder.setPositiveButton("OK") {dialog, which ->
            scannerView?.resumeCameraPreview(this@ScannerActivity)
            startActivity(intent)
        }
        builder.setMessage(result)
        val alert =  builder.create()
        println(alert)
        alert.show()
    }
}
