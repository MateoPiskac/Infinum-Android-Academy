package infinuma.android.shows.ui.showsList

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import infinuma.android.shows.R
import infinuma.android.shows.data.PROFILE_PHOTO_URI
import infinuma.android.shows.data.REMEMBER_LOGIN
import infinuma.android.shows.data.USERNAME
import infinuma.android.shows.databinding.FragmentUserProfileDialogBinding
import infinuma.android.shows.databinding.ProfilePhotoAlertDialogBinding
import infinuma.android.shows.ui.login.sharedPreferences
import java.io.File
import java.io.IOException

class UserProfileDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentUserProfileDialogBinding? = null
    private lateinit var alertDialogBinding: ProfilePhotoAlertDialogBinding
    private val binding get() = _binding!!
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    private val REQUEST_CAMERA_PERMISSION = 101
    private lateinit var photoUri: Uri
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileDialogBinding.inflate(inflater, container, false)
        binding.userEmail.text = sharedPreferences.getString(USERNAME, getString(R.string.placeholder_review_author))

        val tempPath = sharedPreferences.getString(PROFILE_PHOTO_URI, "")
        if (tempPath == "")
            binding.userProfilePicture.setImageResource(R.drawable.placeholder_profile_picture)
        else
            Glide.with(requireContext()).load(tempPath)
                .into(binding.userProfilePicture)
        binding.logoutButton.setOnClickListener {
            val builder =
                AlertDialog.Builder(requireContext()).setMessage("Are you sure you want to logout?").setTitle("Logout?").setCancelable(true)
                    .setPositiveButton("Yes") { dialog, _ ->

                        sharedPreferences.edit {
                            putBoolean(REMEMBER_LOGIN, false)
                            apply()
                        }
                        dialog.cancel()
                        findNavController().navigate(R.id.action_showsFragment_to_loginFragment)

                    }.setNegativeButton("No") { dialog, _ ->

                        dialog.cancel()

                    }
                    .create()
                    .show()
        }

        binding.changePhotoButton.setOnClickListener {
            alertDialogBinding = ProfilePhotoAlertDialogBinding.inflate(layoutInflater)

            val alertBuilder = AlertDialog.Builder(requireContext())
                .setView(alertDialogBinding.alertLayout)
                .setCancelable(true)
            val alertDialog = alertBuilder.create()

            alertDialogBinding.cameraButton.setOnClickListener {
                alertDialog.dismiss()
                dispatchTakePictureIntent()
            }
            alertDialogBinding.galleryButton.setOnClickListener {
                alertDialog.dismiss()
                requestMediaAccessAndPickImage()
            }
            alertDialog.show()
        }

        return binding.root

    }

    private fun createImageFile(): File {
        val storageDir: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("Profile_photo", ".jpg", storageDir).apply {
            photoUri = FileProvider.getUriForFile(requireContext(), "com.infinuma.android.shows.fileprovider", this)
        }
    }

    private fun dispatchPickImageIntent() {
        val pickImageIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        pickImageIntent.type = "image/*"
        startActivityForResult(pickImageIntent, REQUEST_PICK_IMAGE)

    }

    private fun dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            requireContext(), "com.infinuma.android.shows.fileprovider", it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
        } else {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
    }

    private fun requestMediaAccessAndPickImage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            dispatchPickImageIntent()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PICK_IMAGE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                throw (SecurityException("No camera permission"))
            }
        }
        if (requestCode == REQUEST_PICK_IMAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchPickImageIntent()
            } else {
                Toast.makeText(requireContext(), "Media access permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    binding.userProfilePicture.setImageURI(photoUri)
                    sharedPreferences.edit {
                        putString(PROFILE_PHOTO_URI, photoUri.toString())
                        commit()
                    }
                }

                REQUEST_PICK_IMAGE -> {
                    val imageUri: Uri? = data?.data
                    if (imageUri != null) {
                        sharedPreferences.edit {
                            putString(PROFILE_PHOTO_URI, imageUri.toString())
                            commit()
                        }
                        Glide.with(requireContext()).load(imageUri)
                            .into(binding.userProfilePicture)
                    } else {
                        Toast.makeText(requireContext(), "Failed to pick image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}