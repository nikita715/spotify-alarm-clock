package ru.nikstep.alarm.api.model

import ru.nikstep.alarm.model.Identifiable

class Track(override val id: Long, val name: String, val duration: Long, val externalId: String) : Identifiable