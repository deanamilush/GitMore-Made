package com.dean.gitmore.util

import com.dean.gitmore.databinding.FragmentDashboardBinding
import com.dean.gitmore.databinding.FragmentFollowBinding
import com.dean.gitmore.databinding.FragmentHomeBinding

interface ShowState {
    fun onSuccessState(homeFragmentBinding: FragmentHomeBinding? = null,
                       followFragmentBinding: FragmentFollowBinding? = null,
                       favoriteFragmentBinding: FragmentDashboardBinding? = null)

    fun onLoadingState(homeFragmentBinding: FragmentHomeBinding? = null,
                       followFragmentBinding: FragmentFollowBinding? = null,
                       favoriteFragmentBinding: FragmentDashboardBinding? = null)

    fun onErrorState(homeFragmentBinding: FragmentHomeBinding? = null,
                     followFragmentBinding: FragmentFollowBinding? = null,
                     favoriteFragmentBinding: FragmentDashboardBinding? = null,
                     message: String?)
}