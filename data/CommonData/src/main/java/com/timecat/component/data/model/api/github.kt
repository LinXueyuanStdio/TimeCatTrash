package com.timecat.component.data.model.api

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
data class GitFile(
    var content:String,
    var message:String,
    var committer:SmallCommitter
)
data class SmallCommitter (
    val name : String,
    val email : String
)

data class GitFileResponse (
    val content : Content,
    val commit : Commit
)

data class _links (

    val self : String,
    val git : String,
    val html : String
)

data class Author (

    val date : String,
    val name : String,
    val email : String
)

data class Commit (

    val sha : String,
    val node_id : String,
    val url : String,
    val html_url : String,
    val author : Author,
    val committer : Committer,
    val message : String,
    val tree : Tree,
    val parents : List<Parents>,
    val verification : Verification
)

data class Content (

    val name : String,
    val path : String,
    val sha : String,
    val size : Int,
    val url : String,
    val html_url : String,
    val git_url : String,
    val download_url : String,
    val type : String,
    val _links : _links
)

data class Committer (

    val date : String,
    val name : String,
    val email : String
)


data class Parents (

    val url : String,
    val html_url : String,
    val sha : String
)


data class Verification (

    val verified : Boolean,
    val reason : String,
    val signature : String,
    val payload : String
)

data class Tree (

    val url : String,
    val sha : String
)