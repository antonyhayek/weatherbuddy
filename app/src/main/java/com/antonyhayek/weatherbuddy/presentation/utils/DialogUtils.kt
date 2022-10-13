package com.antonyhayek.weatherbuddy.presentation.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.antonyhayek.weatherbuddy.R
import com.google.android.material.textview.MaterialTextView
import com.nordan.dialog.Animation
import com.nordan.dialog.DialogType
import com.nordan.dialog.NordanAlertDialog
import com.nordan.dialog.NordanLoadingDialog
import java.util.*

object DialogUtils {

    private lateinit var alertDialog: Dialog
    private lateinit var questionDialog: Dialog

    fun initLoadingDialog(activity: Activity, loadingText: String = "Loading..."): Dialog =
        createLoadingDialog(activity, loadingText, 0, -1)

    fun displayErrorDialog(activity: Activity, title: String, errorString: String?) {

        alertDialog = NordanAlertDialog.Builder(activity)
            .setAnimation(Animation.POP)
            .isCancellable(false)
            .setTitle(title)
            .setMessage(errorString)
            .setDialogType(DialogType.ERROR)
            .setPositiveBtnText(activity.getString(R.string.text_ok))
            .onPositiveClicked{
            }
            .build()

        if(::alertDialog.isInitialized || !alertDialog.isShowing)
            alertDialog.show()
    }

    fun displayQuestionDialog(
        requireActivity: FragmentActivity,
        title: String,
        message: String,
        positiveText: String,
        negativeText: String,
        onPositiveClicked: () -> Unit,
        onNegativeClicked: () -> Unit
    ) {

        questionDialog = NordanAlertDialog.Builder(requireActivity)
            .setDialogType(DialogType.QUESTION)
            .setTitle(title)
            .setMessage(message)
            .setPositiveBtnText(positiveText)
            .setNegativeBtnText(negativeText)
            .onPositiveClicked {
                onPositiveClicked()
            }
            .onNegativeClicked {
                onNegativeClicked()
            }
            .build()

        if(::questionDialog.isInitialized || !questionDialog.isShowing)
            questionDialog.show()
    }


    private fun createLoadingDialog(
        activity: Activity,
        loadingText: String?,
        accentColor: Int,
        progress: Int
    ): Dialog {

        val loadingDialog = Dialog(activity)
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        Optional.ofNullable(loadingDialog.window)
            .ifPresent { window: Window ->
                window.setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT)
                )
            }
        loadingDialog.setCancelable(false)
        loadingDialog.setContentView(R.layout.custom_loading_dialog)
        (loadingDialog.findViewById<View>(R.id.tv_loading) as TextView).text = loadingText
        val loadingBar = loadingDialog.findViewById<ProgressBar>(R.id.progress_bar)
        if (accentColor > 0) {
            loadingBar.indeterminateDrawable.setColorFilter(
                activity.getColor(accentColor),
                PorterDuff.Mode.MULTIPLY
            )
        }
        if (progress > -1) {
            loadingBar.isIndeterminate = false
            loadingBar.progress = progress
        }
        return loadingDialog
    }
}