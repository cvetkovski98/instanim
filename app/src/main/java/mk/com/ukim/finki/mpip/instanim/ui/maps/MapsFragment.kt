package mk.com.ukim.finki.mpip.instanim.ui.maps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mk.com.ukim.finki.mpip.instanim.R
import mk.com.ukim.finki.mpip.instanim.data.entity.Post
import mk.com.ukim.finki.mpip.instanim.data.model.Status
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentMapsBinding
import mk.com.ukim.finki.mpip.instanim.ui.posts.PostViewModel
import mk.com.ukim.finki.mpip.instanim.util.FactoryInjector

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding
        get() = _binding!!
    private var posts: List<Post>? = null
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        var lastPost:LatLng? = null
        posts?.forEach { post ->
            run {
                val latLng: LatLng? = post.lat?.let { post.lng?.let { it1 -> LatLng(it, it1) } }
                googleMap.addMarker(latLng?.let { MarkerOptions().position(it).title(post.description?.take(15)) })
                lastPost = latLng
            }
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(lastPost))
    }

    private val postListViewModel: PostViewModel by viewModels {
        FactoryInjector.getPostViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postListViewModel.fetchPosts()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        postListViewModel.posts.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.ERROR -> {
                    // do nothing
                }
                Status.LOADING -> {
                    // do nothing
                }
                Status.SUCCESS -> {
                    it.data?.let { postsList ->
                        posts = postsList
                        mapFragment?.getMapAsync(callback)
                    }
                }
            }
        })


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
