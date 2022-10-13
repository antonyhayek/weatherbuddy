package com.antonyhayek.weatherbuddy.presentation.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.antonyhayek.weatherbuddy.R
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.presentation.utils.DialogUtils


typealias Inflate<I> = (LayoutInflater, ViewGroup?, Boolean) -> I

abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!

    private var loadingDialog: Dialog? = null
    private var loadingView: View? = null
    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view.findViewById(R.id.root)
        loadingView = view.findViewById(R.id.view_loading)
        loadingDialog = DialogUtils.initLoadingDialog(requireActivity(), getString(R.string.loading))
        setUiWindowInsets()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showLoading() {
        loadingView?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        loadingView?.visibility = View.GONE
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

        DialogUtils.displayErrorDialog(requireActivity(), title ?: "", errorString)
    }

    protected open fun setUiWindowInsets() {
        if (rootView == null) {
            return
        }

        var posTop = 0
        var posBottom = 0
        ViewCompat.setOnApplyWindowInsetsListener(rootView!!) { v, insets ->

            if (posBottom == 0) {
                posTop = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                posBottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            }

            v.updatePadding(
                v.paddingStart,
                posTop,
                v.paddingEnd,
                posBottom
            )

            insets
        }
    }


}