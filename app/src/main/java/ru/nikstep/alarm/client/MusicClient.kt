package ru.nikstep.alarm.client

/**
 * Client of an external music service
 */
interface MusicClient<T> {
    /**
     * Play the item from [data]
     */
    fun play(data: T)

    /**
     * Stop the music
     */
    fun stop()
}