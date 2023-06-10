package com.example.social_mediavj.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.social_mediavj.Model.User
import com.example.social_mediavj.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CommentAdapter(
    private val mContext: Context,
    private val mComment: List<com.example.social_mediavj.Model.Comment>,
    private val Postid: String
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>()
{

    private var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mComment.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser

        val comment = mComment[position]

    //    Picasso.get().load(comment.getCommentImage()).into(holder.commentButton)

        Picasso.get().load(comment.getComment()).into(holder.commentImage)

        publisherInfo(holder.commentImage, holder.username, holder.comment)

    }

    private fun publisherInfo(commentImage: CircleImageView, username: TextView, comment: TextView) {
        val userRef = FirebaseDatabase.getInstance().reference.child("Users")

        userRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue<User>(User::class.java)

                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.comment).into(commentImage)
                    username.text = user!!.getUsername()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var commentImage: CircleImageView
        var username: TextView
        var comment: TextView

        init {
            commentImage = itemView.findViewById(R.id.comment_image_profile)
            username = itemView.findViewById(R.id.comment_username)
            comment = itemView.findViewById(R.id.post_comment) }

    }


   /* override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mComment.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        firebaseUser = FirebaseAuth.getInstance().currentUser

        val comment = mComment[position]

        Picasso.get().load(comment.getPostimage()).into(holder.postImage)
        publisherInfo(holder.profileImage, holder.userName, holder.publisher, post.getPublisher())


        holder.comment.text = comment.comment
        getUserInfo(holder.image_profile, holder.username, comment.publisher)

        holder.username.setOnClickListener {

            val intent = Intent(mContext, MainActivity::class.java)
            intent.putExtra("publisherid", comment.publisher)
            mContext.startActivity(intent)
        }

        holder.image_profile.setOnClickListener {

            val intent = Intent(mContext, MainActivity::class.java)
            intent.putExtra("publisherid", comment.publisher)
            mContext.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            if (comment.publisher == firebaseUser.uid) {

                val alertDialog = AlertDialog.Builder(mContext).create()
                alertDialog.setTitle("Do you want to delete?")
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No"
                ) { dialog, which ->
                    dialog.dismiss()
                }
                alertDialog.setButton(
                    AlertDialog.BUTTON_POSITIVE, "Yes"
                ) { dialog, which ->
                    FirebaseDatabase.getInstance().getReference("Comments")
                        .child(postid).child(comment.commentid)
                        .removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    dialog.dismiss()
                }
                alertDialog.show()
            }
            return@setOnLongClickListener true
        }
    }



    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image_profile: ImageView = itemView.findViewById(R.id.image_profile)
        val username: TextView = itemView.findViewById(R.id.username)
        val comment: TextView = itemView.findViewById(R.id.comment)
    }

    private fun getUserInfo(imageView: ImageView, username: TextView, publisherid: String) {
        val reference = FirebaseDatabase.getInstance().reference
            .child("Users").child(publisherid)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                Glide.with(mContext).load(user.imageurl).into(imageView)
                username.text = user.username
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }*/
}