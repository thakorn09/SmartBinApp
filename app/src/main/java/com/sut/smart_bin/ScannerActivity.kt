package com.sut.smart_bin

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler

class ScannerActivity : AppCompatActivity(),ResultHandler{

    private val REQUES_CAMERA = 1
    private var scannerView: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val u = intent.getSerializableExtra("USERS") as Users
        var Ids = u.Ids
        var Uid = u.Uid
        var Name = u.Name
        var Email = u.Email
        var Phone = u.Phone
        var Photo = u.Photo
        var GoodBin = u.GoodBin
        var BadBin = u.BadBin



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
    override fun handleResult(rawResult: Result) {
        /*val result = p0?.text
        val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE)as Vibrator
        vibrator.vibrate(1000)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Result")
        builder.setPositiveButton("Yes") {dialog, which ->
            scannerView?.resumeCameraPreview(this@ScannerActivity)
            startActivity(intent)
        }
        builder.setMessage(result)
        val alert =  builder.create()
        println(alert)
        alert.show()*/

        /*startActivity(Intent(this, ContinueTrashActivity::class.java))

        finish()*/
        var sCan = rawResult.toString()
        changeBinstate(sCan)

    }


    fun changeBinstate( sCan : String){
        val user = FirebaseAuth.getInstance().currentUser
        Fuel.put("https://smartbin-sut.herokuapp.com/SmartBin/${sCan}/${user!!.uid}/1")
            .also { println(it) }
            .response { result -> }



        Toast.makeText(applicationContext,sCan,Toast.LENGTH_SHORT).show()


        val intent = Intent(this, ContinueTrashActivity::class.java)
        intent.putExtra("scan", sCan)
        startActivity(intent)
        finish()

    }




}
