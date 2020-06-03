package com.app.imageeditor.editor

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.app.imageeditor.BaseActivity
import com.app.imageeditor.R
import com.app.imageeditor.editor.brush.BrushPropertiesFragment
import com.app.imageeditor.editor.brush.Properties
import com.app.imageeditor.editor.emoji.EmojiDialogFragment
import com.app.imageeditor.editor.filter.FilterAdapter
import com.app.imageeditor.editor.sticker.StickerDialogFragment
import com.app.imageeditor.editor.texteditor.TextEditorDialogFragment
import com.app.imageeditor.editor.texteditor.TextEditorListener
import ja.burhanrashid52.photoeditor.*
import ja.burhanrashid52.photoeditor.PhotoEditor.OnSaveListener
import kotlinx.android.synthetic.main.activity_image_editor.*
import java.io.File

class ImageEditorActivity : BaseActivity(), Properties, OnPhotoEditorListener, TextEditorListener {

    private lateinit var mPropertiesBSFragment: BrushPropertiesFragment
    var mPhotoEditor: PhotoEditor? = null
    var emojiDialogFragment: EmojiDialogFragment?=null
    var stickerDialogFragment: StickerDialogFragment?=null
    private lateinit var filterAdapter : FilterAdapter

    private val mConstraintSet = ConstraintSet()
    private var mIsFilterVisible = false

    private val viewModel: ImageEditorActivityViewModel by lazy {
        ViewModelProviders.of(this).get(ImageEditorActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_editor)
        viewModel.setImageEditorOptions(intent)
        if (viewModel.isCropOnly()) {
            openCropperActivity()
        } else {
            initView()
            initListeners()
        }
    }

    private fun initView() {
        mPropertiesBSFragment = BrushPropertiesFragment(this)

        //Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_medium);
        //Typeface mEmojiTypeFace = Typeface.createFromAsset(getAssets(), "emojione-android.ttf");
        mPhotoEditor = PhotoEditor.Builder(this, photoEditorView)
            .setPinchTextScalable(true) // set flag to make text scalable when pinch
            //.setDefaultTextTypeface(mTextRobotoTf)
            //.setDefaultEmojiTypeface(mEmojiTypeFace)
            .build() // build photo editor sdk


        mPhotoEditor?.setOnPhotoEditorListener(this)
        photoEditorView.source.setImageURI(Uri.parse(viewModel.getImageEditorOptions().imageUri))
        filterAdapter = FilterAdapter(viewModel.getFilters(), listener = {
            mPhotoEditor?.setFilterEffect(it)
        })
        initRecycleView()
        initEmojiDialog()
        initStickerDialog()

    }

    /**
     * Initialize emoji dialog
     */
    private fun initEmojiDialog() {
        emojiDialogFragment = EmojiDialogFragment {
            mPhotoEditor?.addEmoji(it)
            tvSelectedTool.setText(R.string.label_emoji)
        }
    }

    /**
     * Initialize sticker dialog
     */
    private fun initStickerDialog() {
        stickerDialogFragment = StickerDialogFragment(viewModel.getImageEditorOptions().stickerImages, listener = {
            mPhotoEditor?.addImage(it)
            tvSelectedTool.setText(R.string.label_sticker)
        })
    }

    private fun initListeners() {
        btnCrop.setOnClickListener {
            openCropperActivity()
        }

        btnClose.setOnClickListener {
            onBackPressed()
        }

        btnDone.setOnClickListener{
            saveImage()
        }

        btnUndo.setOnClickListener{
            mPhotoEditor?.undo()
        }

        btnRedo.setOnClickListener{
            mPhotoEditor?.redo()
        }
    }

    private fun initRecycleView() {
        initEditorTools()
        initFilters()
    }

    /**
     * Initialize editor tool recycle view
     */
    private fun initEditorTools() {
        rvEditorTools.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvEditorTools.adapter = ToolsAdapter(viewModel.getEditorTools(), listener = {
            applySelectedTool(it)
        })
    }

    private fun initFilters(){
        rvFilterView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvFilterView.adapter = filterAdapter
    }

    private fun applySelectedTool(type: ToolModel){
        when(type.toolType){
            ToolType.BRUSH ->{
                mPhotoEditor?.setBrushDrawingMode(true)
                mPropertiesBSFragment.show(supportFragmentManager, mPropertiesBSFragment.tag)
                tvSelectedTool.text = type.toolName
            }
            ToolType.TEXT -> {
                TextEditorDialogFragment.getStartFragment(this).show(supportFragmentManager,
                    TextEditorDialogFragment.TAG
                )
            }
            ToolType.ERASER -> {
                mPhotoEditor?.brushEraser()
                tvSelectedTool.text = getString(R.string.label_eraser_mode)
            }
            ToolType.FILTER -> {
                tvSelectedTool.setText(R.string.label_filter)
                showFilter(true)
            }
            ToolType.EMOJI ->{
             emojiDialogFragment?.show(supportFragmentManager,EmojiDialogFragment.TAG )
            }
            ToolType.STICKER -> {
                stickerDialogFragment?.show(supportFragmentManager, StickerDialogFragment.TAG)
            }
        }
    }


    /**
     * Open crop activity for crop an image
     */
    private fun openCropperActivity() {
        startActivityForResult(
            getCropIntent(viewModel.getImageEditorOptions())
            , REQUEST_CROP
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CROP) {
                val imageUri = data?.getParcelableExtra<Uri>("URI")
                if (viewModel.isCropOnly()) {
                    // If user want to use only crop functionality, then return crop result without showing edit image activity
                    sendResult(imageUri, isSuccess = true)
                } else {
                    // Show cropped image in editor view.
                    // First clear existing image from  editor then show cropped image.
                    photoEditorView.source.setImageURI(Uri.EMPTY)
                    photoEditorView.source.setImageURI(imageUri)
                    viewModel.getImageEditorOptions().imageUri = imageUri.toString()
                }
            }
        } else {
            if (viewModel.isCropOnly()) {
                sendResult(isSuccess = false)
            }
        }

    }

    /**
     * Save edited image and send result to an activity
     */
    @SuppressLint("MissingPermission")
    private fun saveImage() {
        showHideProgressDialog(true)
        try {
            val saveSettings = SaveSettings.Builder()
                .setClearViewsEnabled(true)
                .setTransparencyEnabled(true)
                .build()

            mPhotoEditor?.saveAsFile(
                viewModel.getImageEditorOptions().outPutPath,
                saveSettings,
                object : OnSaveListener {
                    override fun onSuccess(imagePath: String) {
//                        showHideProgressDialog(false)
                        showSnackbar("Image Saved Successfully")
                        sendResult(Uri.fromFile(File(imagePath)),true )
                    }

                    override fun onFailure(exception: Exception) {
                        showHideProgressDialog(false)
                        showSnackbar("Failed to save Image")
                    }
                })
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            showHideProgressDialog(false)
            showSnackbar(e.message ?: "")
        }
    }

    override fun onColorChanged(colorCode: Int) {
        mPhotoEditor?.brushColor = colorCode
        tvSelectedTool.text = getString(R.string.brush)
    }

    override fun onOpacityChanged(opacity: Int) {
        mPhotoEditor?.setOpacity(opacity)
        tvSelectedTool.text = getString(R.string.brush)
    }

    override fun onBrushSizeChanged(brushSize: Int) {
        mPhotoEditor?.brushSize = brushSize.toFloat()
        tvSelectedTool.text = getString(R.string.brush)
    }

    override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
        TextEditorDialogFragment.getStartFragment(this, text?:"", colorCode).show(supportFragmentManager,
            TextEditorDialogFragment.TAG
        )
    }

    override fun onStartViewChangeListener(viewType: ViewType?) {
    }

    override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
    }

    override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
    }

    override fun onStopViewChangeListener(viewType: ViewType?) {
    }

    override fun onDone(inputText: String?, colorCode: Int) {
        val styleBuilder = TextStyleBuilder()
        styleBuilder.withTextColor(colorCode)
        mPhotoEditor?.addText(inputText, styleBuilder)
        tvSelectedTool.setText(R.string.label_text)
    }

    /**
     * Show image filter optons
     */
    private fun showFilter(isVisible: Boolean) {
        mIsFilterVisible = isVisible
        mConstraintSet.clone(rootView)
        if (isVisible) {
            mConstraintSet.clear(rvFilterView.id, ConstraintSet.START)
            mConstraintSet.connect(
                rvFilterView.id, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START
            )
            mConstraintSet.connect(
                rvFilterView.id, ConstraintSet.END,
                ConstraintSet.PARENT_ID, ConstraintSet.END
            )
        } else {
            mConstraintSet.connect(
                rvFilterView.id, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.END
            )
            mConstraintSet.clear(rvFilterView.id, ConstraintSet.END)
        }
        val changeBounds = ChangeBounds()
        changeBounds.duration = 350
        changeBounds.interpolator = AnticipateOvershootInterpolator(1.0f)
        TransitionManager.beginDelayedTransition(rootView, changeBounds)
        mConstraintSet.applyTo(rootView)
    }

    override fun onBackPressed() {
        if (mIsFilterVisible) {
            showFilter(false)
            tvSelectedTool.text = ""
        } else if (!mPhotoEditor!!.isCacheEmpty) {
            showSaveDialog()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Show confirmation dialog, when user close the editor
     */
    private fun showSaveDialog() {
        val builder =
            AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.msg_save_image))
        builder.setPositiveButton(
            "Save"
        ) { _, _ -> saveImage() }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.dismiss() }
        builder.setNeutralButton(
            "Discard"
        ) { _, _ -> finish() }
        builder.create().show()
    }
}
