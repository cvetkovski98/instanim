package mk.com.ukim.finki.mpip.instanim.ui.profiles

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import mk.com.ukim.finki.mpip.instanim.adapter.ProfileListAdapter
import mk.com.ukim.finki.mpip.instanim.data.entity.User
import mk.com.ukim.finki.mpip.instanim.data.model.Status
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentProfileListBinding
import mk.com.ukim.finki.mpip.instanim.util.FactoryInjector

class ProfileListFragment : Fragment() {

    private var _binding: FragmentProfileListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var profileListAdapter: ProfileListAdapter
    private val profileListViewModel: ProfileListViewModel by viewModels {
        FactoryInjector.getProfileListViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initSearch()
        profileListViewModel.fetchProfiles(null)
        profileListViewModel.profiles.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.ERROR -> {
                    updateAdapterData(listOf())
                    binding.loadingPanel.visibility = View.GONE
//                    Log.d("ERROR", "onViewCreated: ${it.message}")
                }
                Status.LOADING -> {
                    binding.loadingPanel.visibility = View.VISIBLE
                    // do nothing
                }
                Status.SUCCESS -> {
                    binding.loadingPanel.visibility = View.GONE
                    it.data?.let { users ->
                        updateAdapterData(users)
//                        Log.d("SUCCESS", "onViewCreated: $users")
//                        Log.d("SUCCESS", "onViewCreated: ${it.message}")
                    }
                }
            }
        })
    }

    private fun initSearch() {
        binding.editText.editText?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            when {
                (event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) -> {
                    var text: String? = binding.editText.editText!!.text.toString()
    //                    var text: String? = binding.editText.text.toString()
                    if (text.isNullOrBlank()) {
                        text = null
                    }
                    profileListViewModel.fetchProfiles(text)
                    return@OnKeyListener true
                }
                else -> false
            }

        })
    }

    private fun initRecycler() {
        profileListAdapter = ProfileListAdapter {
            navigateToProfileDetails(it)
        }

        val llm = LinearLayoutManager(context)

        binding.recycler.apply {
            adapter = profileListAdapter
            layoutManager = llm
        }
    }

    private fun updateAdapterData(users: List<User>) {
        profileListAdapter.setUsers(users)
    }

    private fun navigateToProfileDetails(uid: String) {
        val action = ProfileListFragmentDirections.actionProfileListFragmentToProfileFragment(uid)
        findNavController().navigate(action)
    }
}
