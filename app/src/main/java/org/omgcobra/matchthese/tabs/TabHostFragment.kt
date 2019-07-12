package org.omgcobra.matchthese.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import org.omgcobra.matchthese.R

class TabHostFragment: Fragment() {
    private lateinit var tabs: List<TabFragment>
    private lateinit var tabPagerAdapter: TabPagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        tabs = listOf(ItemsTabFragment(), TagsTabFragment())
        return inflater.inflate(R.layout.tab_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tabPagerAdapter = TabPagerAdapter(childFragmentManager, tabs)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = tabPagerAdapter
    }
}