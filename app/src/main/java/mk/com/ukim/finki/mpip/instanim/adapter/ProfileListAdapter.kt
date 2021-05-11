package mk.com.ukim.finki.mpip.instanim.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.com.ukim.finki.mpip.instanim.data.entity.User
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentProfileListItemBinding
import mk.com.ukim.finki.mpip.instanim.glide.GlideApp

class ProfileListAdapter(
    private val onDetails: (String) -> Unit
) : RecyclerView.Adapter<ProfileListAdapter.ProfileListViewHolder>() {

    private lateinit var binding: FragmentProfileListItemBinding

    private var users: ArrayList<User> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = FragmentProfileListItemBinding.inflate(inflater, parent, false)
        return ProfileListViewHolder()
    }

    override fun onBindViewHolder(holder: ProfileListViewHolder, position: Int) {
        val user: User = users[position]

        binding.root.setOnClickListener {
            onDetails(user.uid)
        }
        holder.setIsRecyclable(false)
        holder.bind(user)
    }

    fun setUsers(u: List<User>) {
        this.users.clear()
        this.users.addAll(u)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = users.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ProfileListViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: User) {
            binding.apply {
                GlideApp.with(root.context)
                    .load(profile.imageUri)
                    .into(profileImage)

                username.text = "@${profile.username}"
                fullName.text = "${profile.name} ${profile.lastName}"
            }
        }
    }
}
