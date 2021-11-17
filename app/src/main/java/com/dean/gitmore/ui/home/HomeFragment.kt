package com.dean.gitmore.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dean.core.data.Resource
import com.dean.core.ui.UserAdapter
import com.dean.gitmore.R
import com.dean.gitmore.databinding.FragmentFavoriteBinding
import com.dean.gitmore.databinding.FragmentFollowBinding
import com.dean.gitmore.databinding.FragmentHomeBinding
import com.dean.gitmore.util.ShowState
import org.koin.android.ext.android.getKoin
import org.koin.android.viewmodel.ViewModelParameter
import org.koin.android.viewmodel.koin.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

class HomeFragment : Fragment(), ShowState {

    private inline fun<reified VM: ViewModel> Fragment.sharedGraphViewModel (
        @IdRes navGraphId: Int,
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition?= null
    ) = lazy {
        val store = findNavController().getViewModelStoreOwner(navGraphId).viewModelStore
        getKoin().getViewModel(ViewModelParameter(VM::class, qualifier, parameters, store))
    }

    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var homeAdapter: UserAdapter
    private val homeViewModel: HomeViewModel by sharedGraphViewModel(R.id.mobile_navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.app_name)
        homeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        homeBinding.errLayout.emptyText.text = getString(R.string.title_setting)

        homeAdapter = UserAdapter(arrayListOf()) {username, iv ->
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(username),
                FragmentNavigatorExtras(iv to username)
            )
        }

        homeBinding.recyclerHome.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = homeAdapter
        }

        homeBinding.searchView.apply {
            queryHint = resources.getString(R.string.title_setting)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    homeViewModel.setSearch(query)
                    homeBinding.searchView.clearFocus()
                    return true
                }
                override fun onQueryTextChange(newText: String): Boolean = false
            })
        }
        observeHome()
    }

    private fun observeHome() {
        homeViewModel.users.observe(viewLifecycleOwner, {
            if (it != null) {
                when(it) {
                    is Resource.Success<*> -> {
                        onSuccessState(homeFragmentBinding = homeBinding)
                        it.data?.let { data -> homeAdapter.setData(data) }
                    }
                    is Resource.Loading<*> -> onLoadingState(homeFragmentBinding = homeBinding)
                    is Resource.Error<*> -> onErrorState(homeFragmentBinding = homeBinding, message = it.message)
                }
            }
        })
    }

    override fun onSuccessState(
        homeFragmentBinding: FragmentHomeBinding?,
        followFragmentBinding: FragmentFollowBinding?,
        favoriteFragmentBinding: FragmentFavoriteBinding?,
    ) {
        homeFragmentBinding?.apply {
            errLayout.mainNotFound.visibility = View.GONE
            homeBinding.progress.visibility = View.GONE
            recyclerHome.visibility = View.VISIBLE
            resources
        }
    }

    override fun onLoadingState(
        homeFragmentBinding: FragmentHomeBinding?,
        followFragmentBinding: FragmentFollowBinding?,
        favoriteFragmentBinding: FragmentFavoriteBinding?,
    ) {
        homeFragmentBinding?.apply {
            errLayout.mainNotFound.visibility = View.GONE
            homeBinding.progress.visibility
            recyclerHome.visibility = View.GONE
        }
    }

    override fun onErrorState(
        homeFragmentBinding: FragmentHomeBinding?,
        followFragmentBinding: FragmentFollowBinding?,
        favoriteFragmentBinding: FragmentFavoriteBinding?,
        message: String?
    ) {
        homeFragmentBinding?.apply {
            errLayout.apply {
                mainNotFound.visibility = View.VISIBLE
                if (message == null) {
                    emptyText.text = resources.getString(R.string.not_found)
                    ivSearch.setImageResource(R.drawable.ic_search_reset)
                } else {
                    emptyText.text = message
                    ivSearch.setImageResource(R.drawable.ic_search_off)
                }
            }
            homeBinding.progress.visibility = View.GONE
            recyclerHome.visibility = View.GONE
        }
    }
}