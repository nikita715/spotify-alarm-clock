package ru.nikstep.alarm.service.notification

import android.app.Notification

/**
 * Creates UI notifications for a user
 */
interface NotificationService {

    fun buildAlarmNotification(): Notification

}