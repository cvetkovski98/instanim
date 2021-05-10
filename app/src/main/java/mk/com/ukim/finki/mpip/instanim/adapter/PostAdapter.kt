package mk.com.ukim.finki.mpip.instanim.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.com.ukim.finki.mpip.instanim.R
import mk.com.ukim.finki.mpip.instanim.data.entity.Post
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentPostListItemBinding
import mk.com.ukim.finki.mpip.instanim.glide.GlideApp

class PostAdapter(
    private val userId: String,
    private val posts: MutableList<Post>,
    private val likePost: (Post) -> Unit,
    private val onDetails: (String) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private lateinit var binder: FragmentPostListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binder = FragmentPostListItemBinding.inflate(inflater, parent, false)
        return PostViewHolder()
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentPost: Post = posts[position]

        binder.detailsButton.setOnClickListener {
            onDetails(currentPost.postId)
        }

        binder.likeButton.setOnClickListener {
            likePost(currentPost)
            notifyItemChanged(position)
        }

        holder.bind(currentPost)
    }

    override fun getItemCount(): Int = posts.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setPosts(posts: List<Post>) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    inner class PostViewHolder : RecyclerView.ViewHolder(binder.root) {
        fun bind(post: Post) {
            binder.usernameTextViewList.text = post.userId
            binder.descriptionPostList.text = post.description
            binder.likedBy.text = binder.root.resources.getString(R.string.liked_by, post.likedBy.size)
            GlideApp.with(binder.root.context)
                .load(post.imageUri)
                .into(binder.postImage)
            if (post.likedBy.contains(userId)){
                binder.likeButton.text = "Unlike"
            } else {
                binder.likeButton.text = "Like"
            }
        }
    }

}
