package com.example.social_mediavj.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.example.social_mediavj.Model.User
import com.example.social_mediavj.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
import java.text.FieldPosition

class UserAdapter(
    private val context: Context,
    private val userList: List<User>,
    private val isFragment: Boolean
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var usernameTextView: TextView = itemView.findViewById(R.id.user_name_search)
        var fullnameTextView: TextView = itemView.findViewById(R.id.user_full_name_search)
        var userProfileImage: CircleImageView = itemView.findViewById(R.id.user_profile_image_search)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.usernameTextView.text = user.upusername
        holder.fullnameTextView.text = user.upfullname
        Picasso.get().load(user.getImage()).placeholder(R.drawable.profile).into(holder.userProfileImage)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}


/*
class UserAdapter (private var mContext: Context, private var mUser: List<User>,
                   private var isFragment: Boolean) : RecyclerView.Adapter<UserAdapter.ViewHolder>()
{

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var usernameTextView: TextView = itemView.findViewById(R.id.user_name_search)
        var fullnameTextView: TextView = itemView.findViewById(R.id.user_full_name_search)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
    val user = mUser[position]

        holder.usernameTextView.text = user.getUsername()
        holder.fullnameTextView.text = user.getFullname()
        Picasso.get().load(user.getImage()).placeholder(R.drawable.profile).into(holder.userProfileImage)
    }

   */
/* class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView){
        var usernameTextView: TextView = itemView.findViewById(R.id.user_name_search)
        var userfullnameTextView: TextView = itemView.findViewById(R.id.user_full_name_search)
        var userProfileImage: CircleImageView = itemView.findViewById(R.id.user_profile_image_search)
        var followButton: Button = itemView.findViewById(R.id.follow_btn_search)
    }*//*




}*/
