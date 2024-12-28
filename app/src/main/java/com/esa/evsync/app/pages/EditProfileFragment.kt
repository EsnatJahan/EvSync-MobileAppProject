package com.esa.evsync.app.pages

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.esa.evsync.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View

class EditProfileFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var editTextName: EditText
    private lateinit var btnSave: Button

    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImageView = view.findViewById(R.id.profileImageView)
        editTextName = view.findViewById(R.id.editTextName)
        btnSave = view.findViewById(R.id.btnSave)

        user?.let {
            editTextName.setText(it.displayName)
            Glide.with(this)
                .load(it.photoUrl)
                .into(profileImageView)
        }

        btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val name = editTextName.text.toString()

        if (user == null) {
            Toast.makeText(requireContext(), "No user is logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "User name changed successfully", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("Profile", "Error updating profile: ${task.exception?.message}")
                Toast.makeText(requireContext(), "Failed to update profile: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



//    private var verificationId: String? = null

//    private fun updatePhoneNumber(phone: String) {
//        val code = otpEditText.text.toString() // User enters OTP here
//        if (verificationId != null && code.isNotEmpty()) {
//            val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
//
//            user?.reauthenticate(credential)?.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    user?.updatePhoneNumber(credential)?.addOnCompleteListener { phoneUpdateTask ->
//                        if (phoneUpdateTask.isSuccessful) {
//                            Toast.makeText(requireContext(), "Phone number updated successfully", Toast.LENGTH_SHORT).show()
//                        } else {
//                            Toast.makeText(requireContext(), "Failed to update phone number", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                } else {
//                    Toast.makeText(requireContext(), "Re-authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        } else {
//            Toast.makeText(requireContext(), "Invalid Verification Code or ID", Toast.LENGTH_SHORT).show()
//        }
//    }
