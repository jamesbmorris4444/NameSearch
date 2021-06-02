package com.godaddy.namesearch.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.godaddy.namesearch.R
import com.godaddy.namesearch.cart_screen.CartFragment
import com.godaddy.namesearch.payment_screen.PaymentFragment
import com.godaddy.namesearch.search_screen.SearchFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_view_pager.view.*


class ViewPagerFragment : BaseFragment() {

    private lateinit var adapter: PaymentsViewPagerAdapter
    private lateinit var pager: ViewPager
    private lateinit var tabLayout: TabLayout

    companion object {
        fun newInstance(): ViewPagerFragment { return ViewPagerFragment() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = PaymentsViewPagerAdapter(requireActivity().supportFragmentManager)
        adapter.addFrag(SearchFragment.newInstance(0), "APPLE")
        adapter.addFrag(CartFragment.newInstance(1), "ORANGE")
        adapter.addFrag(PaymentFragment.newInstance(2), "GRAPES")
        pager = requireView().pager
        pager.adapter = adapter
        tabLayout = requireView().tabs
        tabLayout.setupWithViewPager(pager)
    }

}