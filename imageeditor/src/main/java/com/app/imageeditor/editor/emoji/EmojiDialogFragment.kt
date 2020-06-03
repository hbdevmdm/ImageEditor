package com.app.imageeditor.editor.emoji

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.imageeditor.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ja.burhanrashid52.photoeditor.PhotoEditor


class EmojiDialogFragment(private val listener: (String) -> Unit) : BottomSheetDialogFragment() {
    companion object {
        val TAG = EmojiDialogFragment::class.java.simpleName
    }


    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(
            context,
            R.layout.fragment_bottom_sticker_emoji_dialog,
            null
        )
        dialog.setContentView(contentView)
        initView(contentView)
    }

    private fun initView(view:View) {
        val param = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = param.behavior
        if (behavior is BottomSheetBehavior) {
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        behavior.removeBottomSheetCallback(this)
                        dismiss()
                    }
                }

            })
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        (view.parent as View).setBackgroundColor(
            ContextCompat.getColor(view.context, android.R.color.transparent)
        )
        initRecycleView(view)

        view.findViewById<AppCompatImageView>(R.id.btnClose).setOnClickListener {
            dismiss()
        }
    }

    private fun initRecycleView(view:View) {
        view.findViewById<RecyclerView>(R.id.rvEmoji).layoutManager = GridLayoutManager(activity, 5)
        view.findViewById<RecyclerView>(R.id.rvEmoji).adapter = EmojiAdapter(PhotoEditor.getEmojis(activity), listener = listener)
    }
}