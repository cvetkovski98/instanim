package mk.com.ukim.finki.mpip.instanim.ui.profiles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import mk.com.ukim.finki.mpip.instanim.R

class ProfileListFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileListFragment()
    }

    private lateinit var viewModel: ProfileListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
