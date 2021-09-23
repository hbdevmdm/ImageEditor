package com.app.imageeditor.editor.texteditor

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.imageeditor.BaseActivity
import com.app.imageeditor.R
import com.app.imageeditor.editor.brush.BrushPropertiesViewModel
import com.app.imageeditor.editor.brush.ColorPickerAdapter
import kotlinx.android.synthetic.main.add_text_dialog.*

class TextEditorDialogFragment(private val listener: TextEditorListener) : DialogFragment() {

    companion object {
        val TAG = TextEditorDialogFragment::class.java.simpleName
        fun getStartFragment(
            listener: TextEditorListener,
            inputText: String = "",
            colorCode: Int = 0
        ): TextEditorDialogFragment {
            return TextEditorDialogFragment(listener).apply {
                val args = Bundle()
                args.putString(BaseActivity.EXTRA_INPUT_TEXT, inputText)
                args.putInt(BaseActivity.EXTRA_COLOR_CODE, colorCode)
                arguments = args
            }
        }
    }

    private var colorCode = 0
    private var mInputMethodManager: InputMethodManager? = null

    private val viewModel: BrushPropertiesViewModel by lazy {
        ViewModelProviders.of(this).get(BrushPropertiesViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        //Make dialog full screen with transparent background
        //Make dialog full screen with transparent background
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_text_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mInputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        rvColors.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvColors.adapter = ColorPickerAdapter(viewModel.getColorList(), listener = {
            colorCode = it
            inputAddText.setTextColor(it)
        })

        inputAddText.setText(arguments?.getString(BaseActivity.EXTRA_INPUT_TEXT)?:"")
        colorCode = arguments?.getInt(BaseActivity.EXTRA_COLOR_CODE) ?: 0
        if (colorCode > 0) {
            inputAddText.setTextColor(colorCode)
        }else{
            colorCode = ContextCompat.getColor(inputAddText.context, R.color.white)
            inputAddText.setTextColor(colorCode)
        }
        mInputMethodManager?.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            0
        )
        btnDone.setOnClickListener {
            mInputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
            dismiss()
            if (inputAddText.text.toString().isNotEmpty()) {
                listener.onDone(inputText = inputAddText.text.toString(), colorCode = colorCode)
            }

        }
    }

}