package com.example.internshipmanagement.ui.fullimage

import android.os.Bundle
import android.util.Log
import com.example.internshipmanagement.R
import com.example.internshipmanagement.ui.base.BaseActivity
import com.example.internshipmanagement.util.ZoomOutPageTransformer
import kotlinx.android.synthetic.main.activity_full_image.*

class FullImageActivity : BaseActivity() {

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_full_image
    }

    override fun setViewOnEventListener() {}

    override fun setObserver() {
        ibCloseFullImage.setOnClickListener { this.finish() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadMaterialImage()
    }

    private fun loadMaterialImage() {
        val intentData = intent
        if(intentData != null) {
            val selection = intentData.getStringExtra("selection")
            val materials = intentData.getStringArrayListExtra("materials")
            if(selection != null && materials != null) {
                updateMaterialImageUI(selection.toInt(), materials.toMutableList())
            }
            Log.d("###", "Selection: $selection")
            Log.d("###", "Materials: ${materials.toString()}")
        }
    }

    private fun updateMaterialImageUI(selection: Int, materials: MutableList<String>) {
        val fullImageAdapter = FullImageAdapter()
        fullImageAdapter.submitList(materials)
        vpFullImage.apply {
            setPageTransformer(ZoomOutPageTransformer())
            adapter = fullImageAdapter
            currentItem = selection
            Log.d("###", "Load full image")
        }
    }
}