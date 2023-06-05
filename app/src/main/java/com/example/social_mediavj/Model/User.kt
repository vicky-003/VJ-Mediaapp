package com.example.social_mediavj.Model

class User {

    private var  upusername: String = ""
    private var  upfullname: String = ""
    private var  bio: String = ""
    private var  image: String = ""
    private var  uid: String = ""

    constructor()

    constructor( upusername: String,  upfullname: String, bio: String, image: String, uid: String){
        this.upusername = upusername
        this.upfullname =upfullname
        this.bio = bio
        this.image = image
        this.uid = uid
    }

    fun getUsername(): String{
        return upusername
    }
    fun setUsername(upusername: String){
        this.upusername = upusername
    }

    fun getFullname(): String{
        return upfullname
    }
    fun setFullname(upfullname: String){
        this.upfullname = upfullname
    }

    fun getBio(): String{
        return bio
    }
    fun setbio(bio: String){
        this.bio = bio
    }

    fun getImage(): String{
        return image
    }
    fun setimage(image: String){
        this.image = image
    }

    fun getUid(): String{
        return uid
    }
    fun setUid(uid: String){
        this.uid = uid
    }
}