package com.antonyhayek.weatherbuddy.presentation.base

import android.app.Dialog
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.antonyhayek.weatherbuddy.R
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.presentation.utils.DialogUtils

open class BaseActivity : AppCompatActivity() {
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        loadingDialog = DialogUtils.initLoadingDialog(this, getString(R.string.loading))

    }

    fun showLoadingDialog() {
        loadingDialog?.show()
    }

    fun hideLoadingDialog() {
        loadingDialog?.hide()
    }

    protected fun showErrorDialog(
        onRetryClicked: () -> Unit,
        failure: Resource.Failure,
        canCancelDialog: Boolean = true,
    ) {
        var title: String? = null
        val errorString = when {
            failure.isNetworkError -> {
                getString(R.string.server_error_message)
            }
            failure.apiException != null -> {
                //  title = failure.apiException.title
                failure.apiException.message
            }
            failure.errorCode == 401 -> {
                getString(R.string.invalid_api_key)
            }

            failure.errorCode == 404 -> {
                getString(R.string.wrong_input)
            }

            failure.errorCode == 429 -> {
                getString(R.string.max_limit_reached)
            }

            failure.errorCode in listOf(500, 502, 503, 504) -> {
                getString(R.string.server_error_message)
            }

            failure.errorBody != null -> {
                failure.errorBody.toString()
            }
            else -> {
                title = getString(R.string.no_connection_error_message)
                getString(R.string.server_error_message)
            }
        }

        DialogUtils.displayErrorDialog(this, title ?: "", errorString)
    }

}