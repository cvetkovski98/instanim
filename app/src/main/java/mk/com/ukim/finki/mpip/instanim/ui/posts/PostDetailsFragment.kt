package mk.com.ukim.finki.mpip.instanim.ui.posts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import mk.com.ukim.finki.mpip.instanim.R
import mk.com.ukim.finki.mpip.instanim.data.entity.Post
import mk.com.ukim.finki.mpip.instanim.data.model.Status
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentPostDetailsBinding
import mk.com.ukim.finki.mpip.instanim.glide.GlideApp
import mk.com.ukim.finki.mpip.instanim.util.FactoryInjector

class PostDetailsFragment : Fragment() {
    private var _binding: FragmentPostDetailsBinding? = null
    private val binding
        get() = _binding!!

    private val args: PostDetailsFragmentArgs by navArgs()

    private val viewModel: PostViewModel by viewModels {
        FactoryInjector.getPostViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postId = args.postId
        postId?.let {
            viewModel.fetchPost(postId)
        }

        viewModel.post.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { post ->
                        Log.i("PostDetailsFragment", "onViewCreated: $post")
                        attachDataToFragment(post)
                    }
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        })

        viewModel.updatePostStatus.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    postId?.let {
                        viewModel.fetchPost(postId)
                    }
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        })
    }

    private fun attachDataToFragment(post: Post){
        GlideApp.with(binding.root)
            .load(post.imageUri)
            .into(binding.postImageDetails)
        binding.descriptionPostDetails.text = post.description
        binding.likesPostDetails.text = resources.getString(R.string.liked_by, post.likedBy.size)
        binding.likeButton.setOnClickListener {
            viewModel.likePost(post)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
