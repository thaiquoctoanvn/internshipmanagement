package com.example.internshipmanagement.util

import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.compose.ui.geometry.Rect

/*
* Tham khảo: https://stackoverflow.com/a/26964010/12697321
* */
class KeyBoardDetector {
    interface OnKeyBoardStateChange {
        fun onKeyBoardVisibilityChange(isShowing: Boolean)
    }

    companion object {
        fun detectKeyBoard(viewGroup: ViewGroup, onKeyBoardStateChange: OnKeyBoardStateChange) {
            viewGroup.viewTreeObserver.addOnGlobalLayoutListener {
                // Khung hiển thị của view group
                val rect = android.graphics.Rect()
                viewGroup.getWindowVisibleDisplayFrame(rect)
                val screenHeight = viewGroup.rootView.height

                // rect.bottom là điểm cuối cùng của khung view group
                val keypadHeight = screenHeight - rect.bottom

                // Nếu phần che khung hiển thị lơn hơn 15% khung thì bàn ohimd đang show
                if(keypadHeight > screenHeight * 0.15) {
                    onKeyBoardStateChange.onKeyBoardVisibilityChange(true)
                } else {
                    onKeyBoardStateChange.onKeyBoardVisibilityChange(false)
                }
            }
        }
    }


}