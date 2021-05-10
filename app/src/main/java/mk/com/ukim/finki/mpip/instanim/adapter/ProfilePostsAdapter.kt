package mk.com.ukim.finki.mpip.instanim.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.com.ukim.finki.mpip.instanim.data.entity.Post
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentProfilePostListItemBinding
import mk.com.ukim.finki.mpip.instanim.glide.GlideApp

class ProfilePostsAdapter(
    private val posts: MutableList<Post>,
    private val onDetails: (String) -> Unit,
) : RecyclerView.Adapter<ProfilePostsAdapter.ProfilePostsViewHolder>() {
    private lateinit var binding: FragmentProfilePostListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePostsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = FragmentProfilePostListItemBinding.inflate(inflater, parent, false)
        return ProfilePostsViewHolder()
    }

    override fun onBindViewHolder(holder: ProfilePostsViewHolder, position: Int) {
        val currentPost: Post = posts[position]

        binding.root.setOnClickListener {
            onDetails(currentPost.postId)
        }

        holder.bind(currentPost)
    }

    fun setPosts(posts: List<Post>) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = posts.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ProfilePostsViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.apply {
                GlideApp.with(root.context)
                    .load(post.imageUri)
                    .into(image)

                this.image.setOnClickListener {
                    onDetails(post.postId)
                }
            }

        }
    }
}
