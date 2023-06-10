package com.example.social_mediavj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.social_mediavj.Adapter.CommentAdapter
import com.example.social_mediavj.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class CommentsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var commentList: List<com.example.social_mediavj.Model.Comment>

    private lateinit var addcomment: EditText
    private lateinit var image_profile: ImageView
    private lateinit var post: TextView

    private lateinit var postid: String
    private lateinit var publisherid: String

    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.comments_toolbar)
        supportActionBar?.title = "Comments"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val intent = intent
        postid = intent.getStringExtra("postid").toString()
        publisherid = intent.getStringExtra("publisherid").toString()

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = mLayoutManager
        commentList = mutableListOf()
        commentAdapter = CommentAdapter(this, commentList, postid)
        recyclerView.adapter = commentAdapter

        post = findViewById(R.id.post)
        addcomment = findViewById(R.id.add_comment)
        image_profile = findViewById(R.id.image_profile)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        post.setOnClickListener {
            if (addcomment.text.isEmpty()) {
                Toast.makeText(this, "You can't send empty message", Toast.LENGTH_SHORT).show()
            } else {
                addComment()
            }
        }

        getImage()
        readComments()

    }

    private fun addComment() {

        val commentRef = FirebaseDatabase.getInstance().reference.child("Comments").child(postid)

        val commentid = commentRef.push().key

        val hashMap = hashMapOf(
            "comment" to addcomment.text.toString(),
            "publisher" to firebaseUser.uid,
            "commentid" to commentid
        )

        if (commentid != null) {
            commentRef.child(commentid).setValue(hashMap)
        }
        addNotification()
        addcomment.setText("")

    }

    private fun addNotification() {
        val notiRef = FirebaseDatabase.getInstance().reference.child("Notifications").child(publisherid)

        val hashMap = hashMapOf(
            "userid" to firebaseUser.uid,
            "text" to "commented: ${addcomment.text.toString()}",
            "postid" to postid,
            "ispost" to true
        )

        notiRef.push().setValue(hashMap)
    }

    private fun getImage() {
        val imageRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
        imageRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
              //  Glide.with(applicationContext).load(user.imageUrl).into(image_profile)
                Picasso.get().load(user!!.getImage()).placeholder(R.drawable.comment).into(image_profile)

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun readComments() {
        val reference = FirebaseDatabase.getInstance().reference.child("Comments").child(postid)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentList.toString()
                for (snapshot in dataSnapshot.children) {
                    val comment = snapshot.getValue(com.example.social_mediavj.Model.Comment::class.java)
                    commentList.toString()
                }

                commentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }
}