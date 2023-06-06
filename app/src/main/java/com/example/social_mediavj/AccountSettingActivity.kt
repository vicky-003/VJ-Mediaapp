package com.example.social_mediavj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.social_mediavj.Model.User
import com.example.social_mediavj.databinding.ActivityAccountSettingBinding
import com.example.social_mediavj.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.HashMap

class AccountSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountSettingBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private var checker = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // setContentView(R.layout.activity_account_setting)
        auth = Firebase.auth
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        binding.logoutBtn.setOnClickListener{
            auth.signOut()
            val intent = Intent(this@AccountSettingActivity, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.saveInforProfileBtn.setOnClickListener{
            if (checker == "clicked"){

            }else{
                updateUserInfoOnly()
            }
        }

        userInfo()
    }

    private fun updateUserInfoOnly() {

        when{
            TextUtils.isEmpty(binding.fullNameAprofileFrag.text.toString()) -> Toast.makeText(this, "Please write full name first.", Toast.LENGTH_LONG).show()
            binding.UserNameAprofileFrag.text.toString() == "" ->  Toast.makeText(this, "Please write user name first.", Toast.LENGTH_LONG).show()
            binding.BioAprofileFrag.text.toString() == "" -> Toast.makeText(this, "Please write your bio first.", Toast.LENGTH_LONG).show()
            else -> {
                val usersRef = FirebaseDatabase.getInstance().reference.child("Users")

                val userMap = HashMap<String, Any>()
                userMap["upfullname"] =  binding.fullNameAprofileFrag.text.toString().lowercase(Locale.ROOT)
                userMap["upusername"] = binding.UserNameAprofileFrag.text.toString().lowercase(Locale.ROOT)
                userMap["bio"] = binding.BioAprofileFrag.text.toString().lowercase(Locale.ROOT)

                usersRef.child(firebaseUser.uid).updateChildren(userMap)
                Toast.makeText(this, "Account Information has been updated successfully", Toast.LENGTH_LONG)
                    .show()

                val intent = Intent(this@AccountSettingActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun userInfo(){
        val usersRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue<User>(User::class.java)

                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(binding.AprofileImageViewFrag)
                    binding.fullNameAprofileFrag.setText(user!!.getFullname())
                    binding.UserNameAprofileFrag.setText(user!!.getUsername())
                    binding.BioAprofileFrag.setText(user!!.getBio())
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}