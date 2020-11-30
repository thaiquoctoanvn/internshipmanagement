package com.example.internshipmanagement.util

import android.content.Context
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.Criterion
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieDataSet
import kotlinx.android.synthetic.main.activity_task_reference_detail.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class FunctionHelper {
    companion object {
        // Copy file gốc ra một file tạm và trả về link file tạm
        fun getPathFromUri(context: Context, uri: Uri): String {
            val id = DocumentsContract.getDocumentId(uri)
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir.absolutePath + "/" + id)
            val filePath = file.absolutePath
            if (inputStream != null) {
                val outputStream = FileOutputStream(file)
                val buf = ByteArray(1024)
                var len: Int
                while (inputStream.read(buf).also { len = it } > 0) {
                    outputStream.write(buf, 0, len)
                }
            }
            return filePath
        }

        fun getPathFromBitmap(context: Context, bitmap: Bitmap): String {
            val id = "${System.currentTimeMillis()}${Random(100)}"
            val file = File("${context.cacheDir.absolutePath}/$id")
            val filePath = file.absolutePath
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            return filePath
        }

        fun getDateFromTimeMilliSecond(timeStamp: String): String {
            if (timeStamp.isNotEmpty()) {
                val date = Date(timeStamp.toLong())
                val language = "en"
                val formattedDateAsShortMonth =
                    SimpleDateFormat("dd MMM yyyy", Locale(language))
                return formattedDateAsShortMonth.format(date)
            }
            return ""
        }

        fun getMilliSecondFromDate(dateInString: String): Long? {
            if (dateInString.isNotEmpty()) {
                val language = "en"
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale(language))
                val date = simpleDateFormat.parse(dateInString)
                return date.time
            }
            return null
        }

        fun provideCriteria(): MutableList<Criterion> {
            return mutableListOf(
                Criterion("Behavior", "-1"),
                Criterion("Knowledge", "-1"),
                Criterion("Be proactive at work", "-1")
            )
        }

        fun provideMarkLevel(): MutableList<Int> {
            return mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        }

        fun generateImageMaterials(
            context: Context,
            materials: MutableList<String>,
            layoutMaterialContainer: ConstraintLayout
        ): MutableList<View> {
            val number = materials.size
            val thumbnails = mutableListOf<View>()
            if (number > 0) {
                val widthScreen = context.resources.displayMetrics.widthPixels
                val set = ConstraintSet()
                when (number) {
                    1 -> {
                        val image = ImageView(context).apply {
                            layoutParams = ConstraintLayout.LayoutParams(
                                Constraints.LayoutParams.MATCH_PARENT,
                                widthScreen / 2
                            )
                            id = View.generateViewId()
                            scaleType = ImageView.ScaleType.CENTER_CROP

                        }
                        Glide.with(context)
                            .load("$SERVER_URL${materials[0]}")
                            .placeholder(R.drawable.app_logo)
                            .into(image)

                        layoutMaterialContainer.addView(image)
                        set.clone(layoutMaterialContainer)

                        set.connect(
                            image.id,
                            ConstraintSet.TOP,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.TOP
                        )
                        set.connect(
                            image.id,
                            ConstraintSet.START,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.START
                        )

                        thumbnails.add(image)
                    }
                    2 -> {
                        val firstImage = ImageView(context).apply {
                            layoutParams =
                                ConstraintLayout.LayoutParams(widthScreen / 2, widthScreen / 2)
                            id = View.generateViewId()
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        val secondImage = ImageView(context).apply {
                            layoutParams =
                                ConstraintLayout.LayoutParams(widthScreen / 2, widthScreen / 2)
                            id = View.generateViewId()
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        Glide.with(context)
                            .load("$SERVER_URL${materials[0]}")
                            .placeholder(R.drawable.app_logo)
                            .into(firstImage)
                        Glide.with(context)
                            .load("$SERVER_URL${materials[1]}")
                            .placeholder(R.drawable.app_logo)
                            .into(secondImage)
                        layoutMaterialContainer.apply {
                            addView(firstImage)
                            addView(secondImage)
                        }

                        set.clone(layoutMaterialContainer)
                        set.connect(
                            firstImage.id,
                            ConstraintSet.TOP,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.TOP
                        )
                        set.connect(
                            secondImage.id,
                            ConstraintSet.TOP,
                            firstImage.id,
                            ConstraintSet.TOP
                        )
                        set.createHorizontalChain(
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.LEFT,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.RIGHT,
                            arrayOf(firstImage.id, secondImage.id).toIntArray(),
                            null,
                            ConstraintSet.CHAIN_SPREAD
                        )

                        thumbnails.apply {
                            add(firstImage)
                            add(secondImage)
                        }
                    }
                    else -> {

                        val firstImage = ImageView(context).apply {
                            layoutParams = ConstraintLayout.LayoutParams(
                                (widthScreen * 0.6).toInt(),
                                widthScreen / 2
                            )
                            id = View.generateViewId()
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        val secondImage = ImageView(context).apply {
                            layoutParams = ConstraintLayout.LayoutParams(0, widthScreen / 2 / 2)
                            id = View.generateViewId()
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        val thirdImage = ImageView(context).apply {
                            layoutParams = ConstraintLayout.LayoutParams(0, widthScreen / 2 / 2)
                            id = View.generateViewId()
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        Glide.with(context)
                            .load("$SERVER_URL${materials[0]}")
                            .placeholder(R.drawable.app_logo)
                            .into(firstImage)
                        Glide.with(context)
                            .load("$SERVER_URL${materials[1]}")
                            .placeholder(R.drawable.app_logo)
                            .into(secondImage)
                        Glide.with(context)
                            .load("$SERVER_URL${materials[2]}")
                            .placeholder(R.drawable.app_logo)
                            .into(thirdImage)

                        layoutMaterialContainer.apply {
                            addView(firstImage)
                            addView(secondImage)
                            addView(thirdImage)
                        }

                        set.clone(layoutMaterialContainer)
                        set.connect(
                            firstImage.id,
                            ConstraintSet.TOP,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.TOP
                        )
                        set.connect(
                            firstImage.id,
                            ConstraintSet.START,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.START
                        )

                        set.connect(
                            secondImage.id,
                            ConstraintSet.START,
                            firstImage.id,
                            ConstraintSet.END
                        )
                        set.connect(
                            secondImage.id,
                            ConstraintSet.END,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.END
                        )

                        set.connect(
                            thirdImage.id,
                            ConstraintSet.START,
                            secondImage.id,
                            ConstraintSet.START
                        )
                        set.connect(
                            thirdImage.id,
                            ConstraintSet.END,
                            secondImage.id,
                            ConstraintSet.END
                        )

                        set.createVerticalChain(
                            firstImage.id,
                            ConstraintSet.TOP,
                            firstImage.id,
                            ConstraintSet.BOTTOM,
                            arrayOf(secondImage.id, thirdImage.id).toIntArray(),
                            null,
                            ConstraintSet.CHAIN_SPREAD
                        )

                        thumbnails.apply {
                            add(firstImage)
                            add(secondImage)
                            add(thirdImage)
                        }

                        if (number > 3) {
                            val viewOpacity = TextView(context).apply {
                                layoutParams = ConstraintLayout.LayoutParams(0, 0)
                                typeface = Typeface.DEFAULT_BOLD
                                text = "+${materials.size - 3}"
                                gravity = Gravity.CENTER
                                setBackgroundResource(R.color.black_opacity)
                                setTextColor(ContextCompat.getColor(this.context, R.color.white))
                                id = View.generateViewId()
                            }
                            layoutMaterialContainer.addView(viewOpacity)

                            set.connect(
                                viewOpacity.id,
                                ConstraintSet.START,
                                thirdImage.id,
                                ConstraintSet.START
                            )
                            set.connect(
                                viewOpacity.id,
                                ConstraintSet.END,
                                thirdImage.id,
                                ConstraintSet.END
                            )
                            set.connect(
                                viewOpacity.id,
                                ConstraintSet.TOP,
                                thirdImage.id,
                                ConstraintSet.TOP
                            )
                            set.connect(
                                viewOpacity.id,
                                ConstraintSet.BOTTOM,
                                thirdImage.id,
                                ConstraintSet.BOTTOM
                            )

                            thumbnails.add(viewOpacity)
                        }
                    }
                }
                set.applyTo(layoutMaterialContainer)
            }
            return thumbnails
        }

        fun generateCustomLegends(
            container: ConstraintLayout,
            pieDataSet: PieDataSet,
            legends: MutableList<String>
        ) {
            val colorCodes = pieDataSet.colors
            val context = container.context
            val constConverter = context.resources.displayMetrics.density
            val colorIconSize =
                (context.resources.getDimension(R.dimen.lv0) * constConverter).toInt()
            val marginDistance =
                (context.resources.getDimension(R.dimen.lv0) * constConverter).toInt()


            val tvFirstColor = TextView(context).apply {
                layoutParams = ConstraintLayout.LayoutParams(colorIconSize, colorIconSize)
                id = View.generateViewId()
                setBackgroundColor(colorCodes[0])
            }
            val tvFirstLegend = TextView(context).apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    Constraints.LayoutParams.WRAP_CONTENT,
                    Constraints.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginStart = marginDistance
                }
                id = View.generateViewId()
                text = legends[0]
                gravity = Gravity.START
                setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            val tvSecondColor = TextView(context).apply {
                layoutParams = ConstraintLayout.LayoutParams(colorIconSize, colorIconSize).apply {
                    topMargin = marginDistance
                }
                id = View.generateViewId()
                setBackgroundColor(colorCodes[1])
            }
            val tvSecondLegend = TextView(context).apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    Constraints.LayoutParams.WRAP_CONTENT,
                    Constraints.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginStart = marginDistance
                }
                id = View.generateViewId()
                text = legends[1]
                gravity = Gravity.START
                setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            val tvThirdColor = TextView(context).apply {
                layoutParams = ConstraintLayout.LayoutParams(colorIconSize, colorIconSize).apply {
                    topMargin = marginDistance
                }
                id = View.generateViewId()
                setBackgroundColor(colorCodes[2])
            }
            val tvThirdLegend = TextView(context).apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    Constraints.LayoutParams.WRAP_CONTENT,
                    Constraints.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginStart = marginDistance
                }
                id = View.generateViewId()
                text = legends[2]
                setTextColor(ContextCompat.getColor(context, R.color.black))
            }

            container.apply {
                addView(tvFirstColor)
                addView(tvFirstLegend)
                addView(tvSecondColor)
                addView(tvSecondLegend)
                addView(tvThirdColor)
                addView(tvThirdLegend)
            }

            ConstraintSet().apply {
                clone(container)

                connect(
                    tvFirstColor.id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP
                )
                connect(tvFirstLegend.id, ConstraintSet.START, tvFirstColor.id, ConstraintSet.END)
                connect(tvFirstLegend.id, ConstraintSet.TOP, tvFirstColor.id, ConstraintSet.TOP)
                connect(
                    tvFirstLegend.id,
                    ConstraintSet.BOTTOM,
                    tvFirstColor.id,
                    ConstraintSet.BOTTOM
                )
                createHorizontalChain(
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.RIGHT,
                    arrayOf(tvFirstColor.id, tvFirstLegend.id).toIntArray(),
                    null,
                    ConstraintSet.CHAIN_SPREAD
                )

                connect(tvSecondColor.id, ConstraintSet.TOP, tvFirstColor.id, ConstraintSet.BOTTOM)
                connect(tvSecondLegend.id, ConstraintSet.START, tvSecondColor.id, ConstraintSet.END)
                connect(tvSecondLegend.id, ConstraintSet.TOP, tvSecondColor.id, ConstraintSet.TOP)
                connect(
                    tvSecondLegend.id,
                    ConstraintSet.BOTTOM,
                    tvSecondColor.id,
                    ConstraintSet.BOTTOM
                )
                createHorizontalChain(
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.RIGHT,
                    arrayOf(tvSecondColor.id, tvSecondLegend.id).toIntArray(),
                    null,
                    ConstraintSet.CHAIN_SPREAD
                )

                connect(tvThirdColor.id, ConstraintSet.TOP, tvSecondColor.id, ConstraintSet.BOTTOM)
                connect(tvThirdLegend.id, ConstraintSet.START, tvThirdColor.id, ConstraintSet.END)
                connect(tvThirdLegend.id, ConstraintSet.TOP, tvThirdColor.id, ConstraintSet.TOP)
                connect(
                    tvThirdLegend.id,
                    ConstraintSet.BOTTOM,
                    tvThirdColor.id,
                    ConstraintSet.BOTTOM
                )
                createHorizontalChain(
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.RIGHT,
                    arrayOf(tvThirdColor.id, tvThirdLegend.id).toIntArray(),
                    null,
                    ConstraintSet.CHAIN_SPREAD
                )

                applyTo(container)
            }
        }
    }
}