package com.example.ekontest_hackathon

import android.text.TextUtils
import android.widget.EditText
import java.util.*

class EmptyField(private val collectionEditText: ArrayList<EditText>) {
    val isAllFieldFilled: Boolean
        get() {
            for (i in collectionEditText.indices) {
                if (TextUtils.isEmpty(collectionEditText[i].text)) {
                    return false
                }
            }
            return true
        }
}