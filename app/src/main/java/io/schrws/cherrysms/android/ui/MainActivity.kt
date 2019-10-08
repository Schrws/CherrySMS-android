package io.schrws.cherrysms.android.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.schrws.cherrysms.android.service.MessageService
import io.schrws.cherrysms.android.R
import io.schrws.cherrysms.android.di.Constants.Companion.PERMISSION_REQUEST_CODE
import io.schrws.cherrysms.android.di.SharedPreferenceStorage
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val shared: SharedPreferenceStorage by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS),
                PERMISSION_REQUEST_CODE)
        } else startService()

        id_edit.setText(shared.telegramID.toString())

        id_button.setOnClickListener {
            shared.telegramID = id_edit.text.toString().toLongOrNull() ?: 0
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                    startService()
            }
        }
    }

    private fun startService() {
        ContextCompat.startForegroundService(this, Intent(this, MessageService::class.java))
    }
}
