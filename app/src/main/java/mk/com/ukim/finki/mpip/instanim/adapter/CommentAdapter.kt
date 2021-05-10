package mk.com.ukim.finki.mpip.instanim.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.com.ukim.finki.mpip.instanim.data.entity.Comment
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentPostDetailsCommentItemBinding

class CommentAdapter(
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private lateinit var binder: FragmentPostDetailsCommentItemBinding
    private var comments: ArrayList<Comment> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binder = FragmentPostDetailsCommentItemBinding.inflate(inflater, parent, false)
        return CommentViewHolder()
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentComment: Comment = comments[position]
        holder.bind(currentComment)
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int = comments.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setComments(comments: List<Comment>) {
        this.comments.clear()
        this.comments.addAll(comments)
        notifyDataSetChanged()
    }

    inner class CommentViewHolder : RecyclerView.ViewHolder(binder.root) {
        fun bind(comment: Comment) {
            binder.commentUsername.text = comment.userId
            binder.commentContent.text = comment.content
        }
    }
}