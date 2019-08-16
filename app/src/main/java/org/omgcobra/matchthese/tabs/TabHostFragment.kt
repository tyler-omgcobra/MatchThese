package org.omgcobra.matchthese.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.tabs.item.ItemsTabFragment
import org.omgcobra.matchthese.tabs.tag.TagsTabFragment

class TabHostFragment : Fragment() {
    private lateinit var tabs: List<TabFragment>
    private lateinit var tabPagerAdapter: TabPagerAdapter
    private lateinit var viewPager: NoSwipeViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        tabs = listOf(
                ItemsTabFragment(getString(R.string.items_tab_title)),
                TagsTabFragment(getString(R.string.tags_tab_title))
        )
        return inflater.inflate(R.layout.tab_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tabPagerAdapter = TabPagerAdapter(childFragmentManager, tabs)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = tabPagerAdapter
    }
}