package io.schrws.cherrysms.android.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import io.schrws.cherrysms.android.service.MessageService
import io.schrws.cherrysms.android.R
import io.schrws.cherrysms.android.di.SharedPreferenceStorage
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val shared: SharedPreferenceStorage by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        ContextCompat.startForegroundService(this, Intent(this, MessageService::class.java))

        id_edit.setText(shared.telegramID.toString())

        id_button.setOnClickListener {
            shared.telegramID = id_edit.text.toString().toLongOrNull() ?: 0
        }
    }

}
