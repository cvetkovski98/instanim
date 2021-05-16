package mk.com.ukim.finki.mpip.instanim.ui.profile

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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import mk.com.ukim.finki.mpip.instanim.MainActivity
import mk.com.ukim.finki.mpip.instanim.data.model.Status
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentUpdateProfileBinding
import mk.com.ukim.finki.mpip.instanim.ui.auth.AuthViewModel
import mk.com.ukim.finki.mpip.instanim.util.FactoryInjector

class UpdateProfileFragment : Fragment() {
    private val args: UpdateProfileFragmentArgs by navArgs()

    private var _binding: FragmentUpdateProfileBinding? = null
    private val binding
        get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels {
        FactoryInjector.getAuthViewModel()
    }

    private var imageUri: Uri? = null

    private val pickPhotoAction =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                imageUri = it.data?.data as Uri

                binding.profileImage.setImageURI(imageUri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindButtons()
        authViewModel.currentUser.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.ERROR -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Status.SUCCESS -> {
                    redirectToList()
                }
                Status.LOADING -> {
                    // do nothing
                }
            }
        })
    }

    private fun bindButtons() {

        binding.upload.setOnClickListener {
            uploadPhoto()
        }

        binding.create.setOnClickListener {
            val username = args.username
            val password = args.password
            val email = args.email

            val name = binding.profileName.editText?.text.toString()
            val lastName = binding.profileLastName.editText?.text.toString()
            val bio = binding.profileBio.editText?.text.toString()
            val localUri = this.imageUri

            if (name.isBlank() || lastName.isBlank() || bio.isBlank()) {
                // validate new data
                Toast.makeText(
                    context,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (localUri == null) {
                Toast.makeText(
                    context,
                    "Please select an image",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // if auth data is present, create the user, else update
                if (username != null && password != null && email != null) {
                    authViewModel.signUpUser(
                        username = username,
                        email = email,
                        password = password,
                        name = name,
                        lastName = lastName,
                        bio = bio,
                        profileImageLocalUri = localUri
                    )
                } else {
                    //TODO(Handle Updating)
                }
            }
        }
    }

    private fun uploadPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickPhotoAction.launch(intent)
    }

    private fun redirectToList() {
//        val action = UpdateProfileFragmentDirections.actionUpdateProfileFragmentToMainActivity()
//        findNavController().navigate(action)
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
