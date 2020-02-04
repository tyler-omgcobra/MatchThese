package org.omgcobra.matchthese.fragments

import android.content.Context
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import org.omgcobra.matchthese.R
import java.util.*
import kotlin.concurrent.timerTask

class ExitToastCallback(context: Context,
                        private val timeInterval: Long = 2000L): OnBackPressedCallback(true) {
    private val timer = Timer()
    private val toast = Toast.makeText(context, R.string.exit_toast, Toast.LENGTH_SHORT)

    override fun handleOnBackPressed() {
        toast.show()
        isEnabled = false
        timer.schedule(timerTask {
            isEnabled = true
        }, timeInterval)
    }

    fun cancel() {
        toast.cancel()
        timer.cancel()
        isEnabled = true
    }
}