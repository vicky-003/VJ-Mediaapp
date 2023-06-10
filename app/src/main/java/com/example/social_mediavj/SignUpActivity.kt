package com.example.social_mediavj

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.social_mediavj.databinding.ActivityMainBinding
import com.example.social_mediavj.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    lateinit var upfullname: String
    lateinit var upusername: String
    lateinit var upemail: String
    lateinit var uppassword: String

    lateinit var signin: TextView
    lateinit var signup: Button
    lateinit var fullname: EditText
    lateinit var username: EditText
    lateinit var email: EditText
    lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth

        // database = Firebase.database.reference

        signin = findViewById(R.id.signin_link_btn)
        signup = findViewById(R.id.signup_btn)
        fullname = findViewById(R.id.fullname_signup)
        username = findViewById(R.id.username_signup)
        email = findViewById(R.id.email_signup)
        password = findViewById(R.id.password_singup)

        signin.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        signup.setOnClickListener {
            CreateAccount()
        }
    }

    private fun CreateAccount() {
        upfullname = fullname.text.toString().trim()
        upusername = username.text.toString().trim()
        upemail = email.text.toString().trim()
        uppassword = password.text.toString().trim()

        when {
            TextUtils.isEmpty(upfullname) -> Toast.makeText(this, "full name is required.", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(upusername) -> Toast.makeText(this, "user name is required.", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(upemail) -> Toast.makeText(this, "email is required.", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(uppassword) -> Toast.makeText(this, "password is required.", Toast.LENGTH_LONG).show()

            else -> {
                val progressDialog = ProgressDialog(this@SignUpActivity)
                progressDialog.setTitle("SignUp")
                progressDialog.setMessage("Please wait, this may take a while...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                auth.createUserWithEmailAndPassword(upemail, uppassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            saveUserInfo(upfullname, upusername, upemail, progressDialog)

                        } else {
                            val message = task.exception!!.toString()
                            Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                            auth.signOut()
                            progressDialog.dismiss()
                        }
                    }
            }
        }
    }

    private fun saveUserInfo(upfullname: String, upusername: String, upemail: String, progressDialog: ProgressDialog) {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserId
        userMap["upfullname"] = upfullname.lowercase(Locale.ROOT)
        userMap["upusername"] = upusername.lowercase(Locale.ROOT)
        userMap["upemail"] = upemail
        userMap["bio"] = "hey i am using Coding Cafe social media app."
        userMap["image"] = "https://firebasestorage.googleapis.com/v0/b/social-mediavj.appspot.com/o/image%2Fprofile.png?alt=media&token=51085f10-ae2d-4d20-a289-501766ae9f40"

        usersRef.child(currentUserId).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Account has been created successfully", Toast.LENGTH_LONG)
                        .show()


                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(currentUserId)
                            .child("Following").child(currentUserId)
                            .setValue(true)

                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    val message = task.exception!!.toString()
                    Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                    auth.signOut()
                    progressDialog.dismiss()
                }
            }
    }
}
