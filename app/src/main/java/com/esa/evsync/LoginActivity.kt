package com.esa.evsync

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.esa.evsync.app.dataModels.UserModel
import com.esa.evsync.databinding.ActivityLoginBinding


import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private lateinit var binding: ActivityLoginBinding

    private lateinit var credentialManager: CredentialManager
    private lateinit var context: LoginActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        context = this

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        credentialManager = CredentialManager.create(this)
        binding.googleSignInButton.setOnClickListener {
            startGoogleSignIn()
        }
    }

    private fun startGoogleSignIn() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )
                onGetCredentialResponse(result.credential)
            } catch (e: GetCredentialException) {
                Log.e("GoogleSignIn", "get credential failed", e)
                Toast.makeText(this@LoginActivity, "Failed to Sign in", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun onGetCredentialResponse(credential: Credential) {
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
            val res = Firebase.auth.signInWithCredential(firebaseCredential).await()
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val isNewUser = res.additionalUserInfo?.isNewUser ?: false
                if (isNewUser) {
                    val user = UserModel(
                        username = currentUser.displayName,
                        email = currentUser.email,
                        phone = currentUser.phoneNumber,
                        profile_picture = currentUser.photoUrl.toString()
                    )
                    try {
                        db.collection("users").document(currentUser.uid).set(user).await()
                    } catch (e: Error) {
                        Log.e("firebase", "failed to insert data", e)
                    }
                }
                Log.d("GoogleSignIn", "Sign-In successful: ${currentUser?.email}")
                startActivity(Intent(this, AppActivity::class.java))
                finish()
            }
        } else {
            Log.e("GoogleSignIn", "Unexpected credential")
        }
    }
}
