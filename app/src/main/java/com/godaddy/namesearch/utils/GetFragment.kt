package com.godaddy.namesearch.utils

import androidx.fragment.app.Fragment
import com.godaddy.namesearch.activity.MainActivity

interface GetFragment {
    fun getFragment(): Fragment
    fun getNonNullActivity(): MainActivity
}