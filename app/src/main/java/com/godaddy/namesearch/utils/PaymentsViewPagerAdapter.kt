package com.godaddy.namesearch.utils

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.godaddy.namesearch.cart_screen.CartFragment
import com.godaddy.namesearch.payment_screen.PaymentFragment
import com.godaddy.namesearch.search_screen.SearchFragment

class PaymentsViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragments: MutableList<Fragment> = mutableListOf()
    private var titles: MutableList<String> = mutableListOf()

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                SearchFragment.newInstance(0)
            }
            1 -> {
                CartFragment.newInstance(1)
            }
            2 -> {
                PaymentFragment.newInstance(2)
            }
            else -> {
                SearchFragment.newInstance(0)
            }
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        fragments.add(position, fragment)
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    fun addFrag(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }
}