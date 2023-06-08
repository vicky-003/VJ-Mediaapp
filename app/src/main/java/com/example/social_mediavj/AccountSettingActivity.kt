package com.example.social_mediavj

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.social_mediavj.Model.User
import com.example.social_mediavj.databinding.ActivityAccountSettingBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import java.util.*
import kotlin.collections.HashMap

class AccountSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountSettingBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private var checker = ""
    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storageProfilePicRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // setContentView(R.layout.activity_account_setting)
        auth = Firebase.auth
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageProfilePicRef = FirebaseStorage.getInstance().reference.child("Profile Pictures")


        binding.logoutBtn.setOnClickListener{
            auth.signOut()
            val intent = Intent(this@AccountSettingActivity, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.changeImageTextBtn.setOnClickListener{
            checker = "clicked"

            CropImage.activity()
                .setAspectRatio(1, 1)
                .start(this@AccountSettingActivity)
        }

        binding.saveInforProfileBtn.setOnClickListener{
            if (checker == "clicked"){

                uploadImageAndUpdateInfo()

            }else{
                updateUserInfoOnly()
            }
        }

        userInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode == Activity.RESULT_OK  &&  data != null)
        {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            binding.AprofileImageViewFrag.setImageURI(imageUri)
        }
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

    private fun uploadImageAndUpdateInfo()
    {
        when
        {
            imageUri == null -> Toast.makeText(this, "Please select image first.", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(binding.fullNameAprofileFrag.text.toString()) -> Toast.makeText(this, "Please write full name first.", Toast.LENGTH_LONG).show()
            binding.UserNameAprofileFrag.text.toString() == "" -> Toast.makeText(this, "Please write user name first.", Toast.LENGTH_LONG).show()
            binding.BioAprofileFrag.text.toString() == "" -> Toast.makeText(this, "Please write your bio first.", Toast.LENGTH_LONG).show()

            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Account Settings")
                progressDialog.setMessage("Please wait, we are updating your profile...")
                progressDialog.show()

                val fileRef = storageProfilePicRef!!.child(firebaseUser!!.uid + ".jpg")

                var uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)

                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                    if (!task.isSuccessful)
                    {
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener (OnCompleteListener<Uri> { task ->
                    if (task.isSuccessful)
                    {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        val ref = FirebaseDatabase.getInstance().reference.child("Users")

                        val userMap = HashMap<String, Any>()
                        userMap["upfullname"] =  binding.fullNameAprofileFrag.text.toString().lowercase(Locale.ROOT)
                        userMap["upusername"] = binding.UserNameAprofileFrag.text.toString().lowercase(Locale.ROOT)
                        userMap["bio"] = binding.BioAprofileFrag.text.toString().lowercase(Locale.ROOT)
                        userMap["image"] = myUrl

                        ref.child(firebaseUser.uid).updateChildren(userMap)

                        Toast.makeText(this, "Account Information has been updated successfully.", Toast.LENGTH_LONG).show()

                        val intent = Intent(this@AccountSettingActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        progressDialog.dismiss()
                    }
                    else
                    {
                        progressDialog.dismiss()
                    }
                } )
            }
        }
    }

}