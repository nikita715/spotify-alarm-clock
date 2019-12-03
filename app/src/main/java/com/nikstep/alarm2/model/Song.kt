package com.nikstep.alarm2.model

data class Song(
    val id: Int,
    val active: Boolean,
    val title: String,
    val singer: String,
    val duration: Int,
    val fileName: String
) {
    fun buildFullName() = "$singer - $title"
}