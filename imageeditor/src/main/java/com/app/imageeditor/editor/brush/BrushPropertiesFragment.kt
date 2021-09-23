package com.app.imageeditor.editor.brush

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.imageeditor.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_properties_dialog.*

class BrushPropertiesFragment(private val mProperties: Properties) : BottomSheetDialogFragment(), SeekBar.OnSeekBarChangeListener {

    private val viewModel: BrushPropertiesViewModel by lazy {
        ViewModelProviders.of(this).get(BrushPropertiesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_properties_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sbOpacity.setOnSeekBarChangeListener(this@BrushPropertiesFragment)
        sbSize.setOnSeekBarChangeListener(this@BrushPropertiesFragment)

        rvColors.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvColors.setHasFixedSize(true)
        rvColors.adapter = ColorPickerAdapter(viewModel.getColorList(), listener = {
            mProperties.apply {
                dismiss()
                onColorChanged(it)
            }
        })

    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when(seekBar?.id){
            R.id.sbOpacity -> {
                mProperties.onOpacityChanged(progress)
            }

            R.id.sbSize -> {
                mProperties?.onBrushSizeChanged(progress)
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}