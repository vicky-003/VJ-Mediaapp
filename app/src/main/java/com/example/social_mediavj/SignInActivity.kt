package com.example.social_mediavj

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    lateinit var upemail: String
    lateinit var uppassword: String

    lateinit var email: EditText
    lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        var signup = findViewById<Button>(R.id.signup_link_btn)
        var login = findViewById<Button>(R.id.login_btn)

        email = findViewById(R.id.email_login)
        password = findViewById(R.id.password_login)
        auth = Firebase.auth


        signup.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
        }

        login.setOnClickListener{
            loginUser()
        }


    }

    private fun loginUser() {
        upemail = email.text.toString().trim()
        uppassword = password.text.toString().trim()

        when{
            TextUtils.isEmpty(upemail) -> Toast.makeText(this, "email is required.", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(uppassword) -> Toast.makeText(this, "password is required.", Toast.LENGTH_LONG).show()

            else -> {
                val progressDialog = ProgressDialog(this@SignInActivity)
                progressDialog.setTitle("SignUp")
                progressDialog.setMessage("Please wait, this may take a while...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                auth.signInWithEmailAndPassword(upemail, uppassword).addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        progressDialog.dismiss()
                        val intent = Intent(this@SignInActivity,MainActivity::class.java)
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

    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            val intent = Intent(this@SignInActivity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}