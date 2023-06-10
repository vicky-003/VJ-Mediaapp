package com.example.social_mediavj.Model

class Comment{

    private var comment: String = ""
    private var publisher: String =""
    private var commentId: String = ""

    constructor()

    constructor(comment: String, publisher: String, commentId: String){
        this.comment = comment
        this.publisher = publisher
        this.commentId = commentId
    }

   /* fun getPostid(): String
    {
        return postid
    }
    fun setPostid(postid: String)
    {
        this.postid = postid
    }*/

    fun getComment(): String {
        return comment
    }

    fun setComment(comment: String) {
        this.comment = comment
    }

    fun getPublisher(): String {
        return publisher
    }

    fun setPublisher(publisher: String) {
        this.publisher = publisher
    }

    fun getCommentId(): String {
        return commentId
    }

    fun setCommentId(commentId: String) {
        this.commentId = commentId
    }
}