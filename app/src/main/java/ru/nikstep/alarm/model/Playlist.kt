package ru.nikstep.alarm.model

class Playlist(
    override val id: Long,
    val name: String,
    val externalId: String
) : Identifiable