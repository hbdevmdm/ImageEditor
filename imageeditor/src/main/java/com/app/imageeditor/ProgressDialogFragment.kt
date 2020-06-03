package com.app.imageeditor

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.app.imageeditor.databinding.InflateProgressViewBinding
import kotlinx.android.synthetic.main.inflate_progress_view.*

class ProgressDialogFragment : DialogFragment() {

//    var binding: InflateProgressViewBinding? = null

    companion object {

        var FRAGMENT_TAG = "dialog"
        fun newInstance(): ProgressDialogFragment {
            val dialogFragment = ProgressDialogFragment()
            dialogFragment.isCancelable = false
            return dialogFragment
        }

        var message: String = ""
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        binding = InflateProgressViewBinding.inflate(inflater, container, false)
        if (message.isNotEmpty())
            showMessage()
        else
            hideMessage()
//        return root
        return  inflater.inflate(R.layout.inflate_progress_view, container, false)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
    }

    override fun dismiss() {
        tvProgressMessage?.text = ""
        tvProgressMessage?.visibility = View.GONE
        message = ""
        if (dialog != null)
            super.dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    private fun showMessage() {
        tvProgressMessage?.text = message
        tvProgressMessage?.visibility = View.VISIBLE
    }

    private fun hideMessage(){
        tvProgressMessage?.text = ""
        tvProgressMessage?.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()

        val window = dialog?.window
        val windowParams = window?.attributes
        windowParams?.dimAmount = 0f

        window?.decorView?.setBackgroundResource(R.color.transparent)
        windowParams?.flags = windowParams!!.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window.attributes = windowParams
    }
}