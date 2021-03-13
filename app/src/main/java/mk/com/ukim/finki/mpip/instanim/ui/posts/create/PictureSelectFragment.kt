package mk.com.ukim.finki.mpip.instanim.ui.posts.create

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentPictureSelectBinding
import mk.com.ukim.finki.mpip.instanim.util.FactoryInjector

class PictureSelectFragment : Fragment() {

    private lateinit var binding: FragmentPictureSelectBinding
    private lateinit var imageUri: Uri
    private val viewModel: PostCreateViewModel by activityViewModels {
        FactoryInjector.getPostCreateViewModel()
    }

    private val pickPhotoAction =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                imageUri = it.data?.data as Uri

                binding.uploadImageView.setImageURI(imageUri)
                viewModel.setPostUri(imageUri.toString())
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPictureSelectBinding.inflate(inflater, container, false)
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
        val action =
            PictureSelectFragmentDirections.actionPictureSelectFragmentToCreatePostFragment()
        findNavController().navigate(action)
    }

}