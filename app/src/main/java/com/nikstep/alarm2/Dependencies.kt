package com.nikstep.alarm2

object Dependencies {
    private val dependencies = mutableMapOf<Class<out Any>, Any>()

    fun <T> get(clazz: Class<T>): T? = dependencies[clazz] as T

    fun put(obj: Any) = dependencies.put(obj.javaClass, obj)

    fun <T> remove(clazz: Class<T>) = dependencies.remove(clazz)
}