package mk.com.ukim.finki.mpip.instanim.ui.posts

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mk.com.ukim.finki.mpip.instanim.R
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentPostDetailsMapsBinding

class PostDetailsMapsFragment : Fragment() {

    private var _binding: FragmentPostDetailsMapsBinding? = null
    private val binding
        get() = _binding!!
    private val args: PostDetailsMapsFragmentArgs by navArgs()

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
        val marker = LatLng(args.lat.toDouble(), args.lng.toDouble())
        googleMap.addMarker(MarkerOptions().position(marker).title(args.description))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 4.0f))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailsMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}