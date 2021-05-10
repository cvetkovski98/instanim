package mk.com.ukim.finki.mpip.instanim.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import mk.com.ukim.finki.mpip.instanim.adapter.ProfilePostsAdapter
import mk.com.ukim.finki.mpip.instanim.data.entity.Post
import mk.com.ukim.finki.mpip.instanim.data.entity.User
import mk.com.ukim.finki.mpip.instanim.data.model.Status
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentProfileBinding
import mk.com.ukim.finki.mpip.instanim.glide.GlideApp
import mk.com.ukim.finki.mpip.instanim.util.FactoryInjector

class ProfileFragment : Fragment() {

    private val _tag: String = "ProfileFragment"
    private val args: ProfileFragmentArgs by navArgs()

    private var _binding: FragmentProfileBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var profilePostsAdapter: ProfilePostsAdapter

    private val profileViewModel: ProfileViewModel by viewModels {
        FactoryInjector.getProfileViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.fetchProfileData(args.profileUid)
        initRecycler()

        profileViewModel.profilePosts.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    // do nothing
                }
                Status.ERROR -> {
                    Log.i(_tag, "onViewCreated: ${it.message}")
                }
                Status.SUCCESS -> {
                    it.data?.let { posts ->
                        updateAdapterData(posts)
                        Log.d("ProfileFragment", "onViewCreated: $posts")
                    }
                }
            }
        })

        profileViewModel.profile.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    // do nothing
                }
                Status.ERROR -> {
                    Log.i(_tag, "onViewCreated: ${it.message}")
                }
                Status.SUCCESS -> {
                    it.data?.let { profile ->
                        updateProfileData(profile)
                        Log.d("ProfileFragment", "onViewCreated: $profile")
                    }
                }
            }
        })

//        binding.logoutButton.setOnClickListener {
//            authViewModel.signOutUser()
//        }
    }

    private fun updateProfileData(profile: User) {
        GlideApp.with(binding.root)
            .load(profile.imageUri)
            .into(binding.profileImage)

        binding.bio.text = "${profile.name} ${profile.lastName}\n${profile.bio}"
        binding.username.text = profile.username
        binding.followersCount.text = "Followed By\n${profile.followedBy.size}"
        binding.followingCount.text = "Follows\n${profile.follows.size}"
    }

    private fun initRecycler() {
        profilePostsAdapter = ProfilePostsAdapter(
            mutableListOf(), // empty list
        ) { id -> navigateToDetails(id) }

        val glm = GridLayoutManager(context, 3)

        binding.postsRecycler.apply {
            adapter = profilePostsAdapter
            layoutManager = glm
        }

    }

    private fun updateAdapterData(posts: List<Post>) {
        profilePostsAdapter.setPosts(posts)
        binding.postCount.text = "Posts\n${posts.size}"
    }

    private fun navigateToDetails(postId: String) {
        val action = ProfileFragmentDirections.actionProfileFragmentToPostDetailsFragment(postId)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
