package ru.nikstep.alarm.model.type

enum class AlarmChangeType {
    CREATE,
    UPDATE,
    DELETE;

    companion object {
        const val EXTRA_NAME = "ALARM_CHANGE_EXTRA"
        const val TIME_EXTRA_NAME = "ALARM_CHANGE_EXTRA__TIME"
    }
}