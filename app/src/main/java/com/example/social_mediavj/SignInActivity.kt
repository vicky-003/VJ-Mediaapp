package com.example.social_mediavj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        var signup = findViewById<Button>(R.id.signup_link_btn)
        auth = Firebase.auth


        signup.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
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