package infinuma.android.shows.ui.showsList

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import infinuma.android.shows.R
import infinuma.android.shows.data.PROFILEPHOTOURI
import infinuma.android.shows.data.REMEMBER_LOGIN
import infinuma.android.shows.data.USERNAME
import infinuma.android.shows.databinding.FragmentUserProfileListDialogBinding
import infinuma.android.shows.ui.login.sharedPreferences
import java.io.File
import java.io.IOException
import java.util.Date
import java.util.Locale

class UserProfileDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentUserProfileListDialogBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 101
    private lateinit var photoUri: Uri
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileListDialogBinding.inflate(inflater, container, false)

        binding.userEmail.text = sharedPreferences.getString(USERNAME, getString(R.string.placeholder_review_author))
        val tempPath = sharedPreferences.getString(PROFILEPHOTOURI, "")
        if (tempPath == "")
            binding.userProfilePicture.setImageResource(R.drawable.placeholder_profile_picture)
        else
            binding.userProfilePicture.setImageURI(Uri.parse(tempPath))
        binding.logoutButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext()).setMessage("Are you sure you want to logout?")
                .setTitle("Logout?")
                .setCancelable(true)
                .setPositiveButton("Yes") { dialog, _ ->
                    sharedPreferences.edit {
                        putBoolean(REMEMBER_LOGIN, false)
                        apply()
                    }
                    dialog.cancel()
                    findNavController().navigate(R.id.action_showsFragment_to_loginFragment)
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
                .create()
                .show()
        }

        binding.changePhotoButton.setOnClickListener {

            dispatchTakePictureIntent()

        }



        return binding.root

    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            photoUri = FileProvider.getUriForFile(requireContext(), "com.infinuma.android.shows.fileprovider", this)
        }
    }

    private fun dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            requireContext(),
                            "com.infinuma.android.shows.fileprovider",
                            it
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
        requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                throw (SecurityException("No camera permission11"))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            sharedPreferences.edit {
                putString(PROFILEPHOTOURI, photoUri.toString())
                commit()
            }
            binding.userProfilePicture.setImageURI(photoUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}