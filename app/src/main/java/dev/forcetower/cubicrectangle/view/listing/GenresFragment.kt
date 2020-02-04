package dev.forcetower.cubicrectangle.view.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dev.forcetower.cubicrectangle.core.base.BaseFragment
import dev.forcetower.cubicrectangle.core.base.BaseFragmentPagerAdapter
import dev.forcetower.cubicrectangle.core.model.database.Genre
import dev.forcetower.cubicrectangle.databinding.FragmentGenresBinding

class GenresFragment : BaseFragment() {
    private lateinit var binding: FragmentGenresBinding
    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentGenresBinding.inflate(inflater, container, false).also {
            binding = it
            viewPager = it.pagerGenres
            tabs = it.tabLayout
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragments = genres.map { createListingFragment(it.id) }
        val adapter = GenreFragmentPagerAdapter(fragments, childFragmentManager)
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

    inner class GenreFragmentPagerAdapter(
        fragments: List<Fragment>,
        fm: FragmentManager
    ) : BaseFragmentPagerAdapter(fragments, fm) {
        override fun getPageTitle(position: Int): CharSequence? {
            return genres[position].name
        }
    }

    companion object {
        val genres = listOf(
            Genre(28L, "Ação"),
            Genre(18L, "Drama"),
            Genre(14L, "Fantasia"),
            Genre(878L, "Ficção")
        )

        fun createListingFragment(genreId: Long): GenreListingFragment {
            return GenreListingFragment().apply {
                arguments = bundleOf("genre_id" to genreId)
            }
        }
    }
}