package com.dean.gitmore.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dean.core.data.Resource
import com.dean.core.domain.model.User
import com.dean.gitmore.R
import com.dean.gitmore.databinding.FragmentDetailBinding
import com.dean.gitmore.ui.follow.FollowFragment
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    private lateinit var detailBinding: FragmentDetailBinding
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var user: User
    private var isFavorite = false
    private val args: DetailFragmentArgs by navArgs()
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = args.username
        detailBinding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        detailBinding.lifecycleOwner = viewLifecycleOwner
        observeDetail()
        return detailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* changedFavorite(isFavorite)
         detailBinding.fabFavorite.setOnClickListener {
             addOrRemoveFavorite()
             changedFavorite(isFavorite)
         }*/
        val tabList = arrayOf(resources.getString(R.string.followers), resources.getString(R.string.following))
        pagerAdapter = PagerAdapter(tabList, args.username, this)
        detailBinding.pager.adapter = pagerAdapter

        TabLayoutMediator(detailBinding.tabs, detailBinding.pager) {tab, position ->
            tab.text = tabList[position]
        }.attach()
    }

    private fun observeDetail() {

        detailViewModel.detailUsers(args.username).observe(viewLifecycleOwner, {
            when(it) {
                is Resource.Success -> {
                    user = it.data!!
                    detailBinding.data = it.data
                    detailViewModel.getDetailState(args.username)?.observe(viewLifecycleOwner) { user ->
                        isFavorite = user.isFavorite == true
                        changedFavorite(isFavorite)
                    }
                    detailBinding.fabFavorite.show()
                }

                is Resource.Error -> {
                    detailBinding.fabFavorite.hide()
                }

                is Resource.Loading -> {
                    detailBinding.fabFavorite.hide()
                }
            }
            changedFavorite(isFavorite)
            detailBinding.fabFavorite.setOnClickListener {
                addOrRemoveFavorite()
                changedFavorite(isFavorite)
            }
        })
        /* detailViewModel.isFavorite.observe(viewLifecycleOwner, {
             isFavorite = it
             changedFavorite(it)
         })*/
    }

    private fun addOrRemoveFavorite() {
        if (!isFavorite) {
            user.isFavorite = !isFavorite
            detailViewModel.insertFavorite(user)
            Toast.makeText(context, resources.getString(R.string.favorite_add, user.login), Toast.LENGTH_SHORT).show()
            isFavorite = !isFavorite
        } else {
            user.isFavorite = !isFavorite
            detailViewModel.deleteFavorite(user)
            Toast.makeText(context, resources.getString(R.string.favorite_remove, user.login), Toast.LENGTH_SHORT).show()
            isFavorite = !isFavorite
        }
    }

    private fun changedFavorite(statusFavorite: Boolean) {
        if (statusFavorite){
            detailBinding.fabFavorite.setImageResource(R.drawable.ic_favorite)
        }
        else {
            detailBinding.fabFavorite.setImageResource(R.drawable.ic_unfavorite)
        }
    }

    inner class PagerAdapter(
        private val tabList: Array<String>,
        private val username: String,
        fragment: Fragment
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = tabList.size

        override fun createFragment(position: Int): Fragment =
            FollowFragment.newInstance(username, tabList[position])
    }
}