package mk.com.ukim.finki.mpip.instanim.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import mk.com.ukim.finki.mpip.instanim.R
import mk.com.ukim.finki.mpip.instanim.adapter.ProfilePostsAdapter
import mk.com.ukim.finki.mpip.instanim.data.entity.Post
import mk.com.ukim.finki.mpip.instanim.data.entity.User
import mk.com.ukim.finki.mpip.instanim.data.model.Status
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentProfileBinding
import mk.com.ukim.finki.mpip.instanim.glide.GlideApp
import mk.com.ukim.finki.mpip.instanim.ui.auth.AuthViewModel
import mk.com.ukim.finki.mpip.instanim.ui.auth.LoginActivity
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

    private val authViewModel: AuthViewModel by activityViewModels {
        FactoryInjector.getAuthViewModel()
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
                    binding.loadingPanel.visibility = View.VISIBLE
                    // do nothing
                }
                Status.ERROR -> {
                    binding.loadingPanel.visibility = View.GONE
//                    Log.i(_tag, "onViewCreated: ${it.message}")
                }
                Status.SUCCESS -> {
                    binding.loadingPanel.visibility = View.GONE
                    it.data?.let { posts ->
                        updateAdapterData(posts.sortedByDescending { post -> post.createdAt })
//                        Log.d("ProfileFragment", "onViewCreated: $posts")
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
//                    Log.i(_tag, "onViewCreated: ${it.message}")
                }
                Status.SUCCESS -> {
                    it.data?.let { profile ->
                        updateProfileData(profile)
//                        Log.d("ProfileFragment", "onViewCreated: $profile")
                    }
                }
            }
        })

        profileViewModel.updateProfile.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    binding.loadingPanel.visibility = View.VISIBLE
                    // do nothing
                }
                Status.ERROR -> {
//                    Log.i(_tag, "onViewCreated: ${it.message}")
                }
                Status.SUCCESS -> {
                    binding.loadingPanel.visibility = View.GONE
                    it.data?.let {
                        profileViewModel.fetchProfileData(args.profileUid)
//                        Log.d("ProfileFragment", "onViewCreated: $profile")
                    }
                }
            }
        })

        binding.logoutButton.setOnClickListener {
            authViewModel.signOutUser()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun updateProfileData(profile: User) {
        GlideApp.with(binding.root)
            .load(profile.imageUri)
            .into(binding.profileImage)

        authViewModel.fetchCurrentUser()
        if (authViewModel.currentUser.value?.data?.uid == profile.uid){
            binding.followButton.visibility = View.GONE
            binding.logoutButton.visibility = View.VISIBLE
        }else{
            binding.logoutButton.visibility = View.GONE
            if (profile.followedBy.contains(authViewModel.currentUser.value?.data?.uid)){
                binding.followButton.icon = ResourcesCompat.getDrawable(binding.root.resources, R.drawable.ic_baseline_follow_24, null)
            } else {
                binding.followButton.icon = ResourcesCompat.getDrawable(binding.root.resources, R.drawable.ic_outline_follow_24, null)
            }
            binding.followButton.setOnClickListener {
                authViewModel.currentUser.value?.data?.uid?.let { it1 -> followUser(it1, profile.uid) }
            }
        }

        binding.bio.text = "${profile.name} ${profile.lastName}\n${profile.bio}"
        binding.username.text = profile.username
        binding.followersCount.text = "Followed By\n${profile.followedBy.size}"
        binding.followingCount.text = "Follows\n${profile.follows.size}"
        binding.postCount.text = "Posts\n0"
    }

    private fun initRecycler() {
        profilePostsAdapter = ProfilePostsAdapter(
            mutableListOf(), // empty list
        ) { id -> navigateToDetails(id) }

        val glm = GridLayoutManager(context, 3)
        binding.postsRecycler.setItemViewCacheSize(30)

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

    private fun followUser(followerUser: String, followedUser: String) {
        profileViewModel.follow(followerUser, followedUser)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
