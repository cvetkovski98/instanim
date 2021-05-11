package mk.com.ukim.finki.mpip.instanim.ui.posts.create

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import mk.com.ukim.finki.mpip.instanim.R
import mk.com.ukim.finki.mpip.instanim.data.model.Status
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentCreatePostBinding
import mk.com.ukim.finki.mpip.instanim.util.FactoryInjector

class CreatePostFragment : Fragment() {

    private var _binding: FragmentCreatePostBinding? = null
    private val binding
        get() = _binding!!

    private val args: CreatePostFragmentArgs by navArgs()
    private val myMapHandler: MyMapHandler = MyMapHandler()

    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 100.0)
        googleMap.addMarker(
            MarkerOptions().position(sydney).title("Marker in Sydney").draggable(true)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.setOnMarkerDragListener(myMapHandler)
    }

    private val viewModel: PostCreateViewModel by viewModels {
        FactoryInjector.getPostCreateViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uri = args.imageUri
        binding.postImageThumbnail.setImageURI(uri)

        binding.createPostButton.setOnClickListener {
            handleCreate()
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


        viewModel.createResult.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    redirectToList()
                }
                else -> {
                    // do nothing
                    // TODO: disable user input with dialog while waiting
                }
            }
        })

    }

    private fun redirectToList() {
        val action = CreatePostFragmentDirections.actionCreatePostFragmentToPostListFragment()
        findNavController().navigate(action)
    }

    private fun handleCreate() {
        val description = binding.postDescription.text.toString()
        val lat = myMapHandler.getLat()
        val lng = myMapHandler.getLng()
        val uri = args.imageUri

        val options = ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.6f)
            .build()
        val acceptableLabels = resources.getStringArray(R.array.accepted_labels)
        val labeler = ImageLabeling.getClient(options)
        val image = InputImage.fromFilePath(requireContext(), uri)
        labeler.process(image)
            .addOnSuccessListener { labels ->
//                Log.d("LABELS", labels.toString())
                val result =
                    labels.stream().filter { label -> acceptableLabels.contains(label.text) }
                        .findFirst()
                if (result.isPresent) {
                    viewModel.createPost(description, lat, lng, uri)
                } else {
                    Toast.makeText(
                        context,
                        "You didn't upload a picture of an animal or a plant",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

class MyMapHandler : GoogleMap.OnMarkerDragListener {

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onMarkerDragStart(p0: Marker?) {
        p0?.let {
            Log.d("onMarkerDragStart", it.position.toString())
        }
    }

    override fun onMarkerDrag(p0: Marker?) {
        p0?.let {
            Log.d("onMarkerDrag", it.position.toString())
        }
    }

    override fun onMarkerDragEnd(p0: Marker?) {
        p0?.let {
            Log.d("onMarkerDragEnd", it.position.toString())
            latitude = it.position.latitude
            longitude = it.position.longitude
        }
    }

    fun getLat(): Double {
        return latitude
    }

    fun getLng(): Double {
        return longitude
    }

}
