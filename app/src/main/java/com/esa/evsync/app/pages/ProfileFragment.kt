package com.esa.evsync.app.pages

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.esa.evsync.LoginActivity
import com.esa.evsync.R
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileImageView: ImageView = view.findViewById(R.id.profileImageView)
        val tvUserName: TextView = view.findViewById(R.id.tvUserName)
        val tvUserEmail: TextView = view.findViewById(R.id.tvUserEmail)
        //val tvUserPhone: TextView = view.findViewById(R.id.tvUserPhone)

        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        user?.let {
            // Set name
            tvUserName.text = it.displayName ?: "Name not available"

            // Set email
            tvUserEmail.text = it.email ?: "Email not available"

            //tvUserPhone.text = it.phoneNumber ?: "Phone number not available"

            // Load profile picture
            val photoUrl = it.photoUrl
            Glide.with(this)
                .load(photoUrl)
                .into(profileImageView)
        }


        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val googleSignInClient = GoogleSignIn.getClient(requireContext(), GoogleSignInOptions.DEFAULT_SIGN_IN)
            googleSignInClient.signOut().addOnCompleteListener {
                // User has been signed out of Google
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                requireActivity().finish()
            }
        }

        val editProfileButton: Button = view.findViewById(R.id.editProfile)

        editProfileButton.setOnClickListener {
            // Navigate to EditProfileFragment using NavController
            val navController = findNavController()
            navController.navigate(R.id.action_profile_to_editProfile)
        }

    }
}