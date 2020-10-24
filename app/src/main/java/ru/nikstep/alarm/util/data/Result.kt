package ru.nikstep.alarm.util.data

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.nikstep.alarm.util.data.Status.ERROR
import ru.nikstep.alarm.util.data.Status.LOADING
import ru.nikstep.alarm.util.data.Status.SUCCESS

data class Result<out T>(val status: Status, val data: T? = null, val exception: Throwable? = null) {
    companion object {
        fun <T> loading(): Result<T> =
            Result(status = LOADING)

        fun <T> success(data: T): Result<T> =
            Result(status = SUCCESS, data = data)

        fun <T> error(exception: Throwable?): Result<T> =
            Result(status = ERROR, exception = exception)
    }
}

fun <T> LiveData<Result<T>>.observeResult(
    lifecycleOwner: LifecycleOwner,
    successBlock: (T) -> Unit,
    loadingBlock: () -> Unit = {},
    errorBlock: (Throwable?) -> Unit = { exception ->
        Log.e("Result", "Error at the observe", exception)
    }
) = observe(lifecycleOwner) {
    it?.let { resource ->
        when (resource.status) {
            LOADING -> {
                loadingBlock.invoke()
            }
            SUCCESS -> {
                resource.data?.let(successBlock)
            }
            ERROR -> {
                errorBlock.invoke(resource.exception)
            }
        }
    }
}

fun <T> emitLiveData(resourceFunction: suspend () -> T): LiveData<Result<T>> =
    liveData(Dispatchers.IO) {
        emit(Result.loading<T>())
        try {
            emit(Result.success(data = resourceFunction.invoke()))
        } catch (e: Exception) {
            emit(Result.error<T>(exception = e))
        }
    }