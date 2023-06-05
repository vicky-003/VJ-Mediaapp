package com.example.social_mediavj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.social_mediavj.databinding.ActivityAccountSettingBinding
import com.example.social_mediavj.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountSettingBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // setContentView(R.layout.activity_account_setting)
        auth = Firebase.auth

        binding.logoutBtn.setOnClickListener{
            auth.signOut()

            val intent = Intent(this@AccountSettingActivity, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

        }
    }
}