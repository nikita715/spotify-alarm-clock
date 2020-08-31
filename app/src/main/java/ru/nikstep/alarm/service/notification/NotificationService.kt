package ru.nikstep.alarm.service.notification

/**
 * Sends UI notifications to user
 */
interface NotificationService {

    fun notify(text: String)

}