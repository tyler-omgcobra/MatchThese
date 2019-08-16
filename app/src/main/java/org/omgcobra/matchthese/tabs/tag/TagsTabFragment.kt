package org.omgcobra.matchthese.tabs.tag


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.omgcobra.matchthese.MatchTheseApplication
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.tabs.TabFragment

/**
 * A simple [Fragment] subclass.
 *
 */
class TagsTabFragment(title: String) : TabFragment(title) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tags_tab, container, false)
    }
}
