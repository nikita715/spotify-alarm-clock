package ru.nikstep.alarm.service.log

/**
 * UI logging service
 */
interface LogService {
    /**
     * Show UI message
     */
    fun showMessage(id: Int, vararg properties: Any)
}