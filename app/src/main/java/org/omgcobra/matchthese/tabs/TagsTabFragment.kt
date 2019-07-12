package org.omgcobra.matchthese.tabs


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.omgcobra.matchthese.MatchTheseApplication
import org.omgcobra.matchthese.R

/**
 * A simple [Fragment] subclass.
 *
 */
class TagsTabFragment : TabFragment() {
    override val title: String = MatchTheseApplication.getInstance().getString(R.string.tags_tab_title)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tags_tab, container, false)
    }
}
