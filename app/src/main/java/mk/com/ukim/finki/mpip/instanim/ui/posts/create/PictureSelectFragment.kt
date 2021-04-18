package mk.com.ukim.finki.mpip.instanim.ui.posts.create

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentPictureSelectBinding

class PictureSelectFragment : Fragment() {

    private var _binding: FragmentPictureSelectBinding? = null
    private val binding
        get() = _binding!!

    private var imageUri: Uri? = null

    private val pickPhotoAction =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                imageUri = it.data?.data as Uri

                binding.uploadImageView.setImageURI(imageUri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictureSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.uploadButton.setOnClickListener {
            uploadPhoto()
        }

        binding.nextButton.setOnClickListener {
            handleNext()
        }

    }

    private fun uploadPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickPhotoAction.launch(intent)
    }

    private fun handleNext() {
        imageUri?.let {
            val action =
                PictureSelectFragmentDirections.actionPictureSelectFragmentToCreatePostFragment(it)
            findNavController().navigate(action)
        } ?: run {
            Toast.makeText(
                context,
                "Please select an image",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
