package ru.nikstep.alarm.di.module

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.nikstep.alarm.BuildConfig
import ru.nikstep.alarm.api.SpotifyApiClient
import ru.nikstep.alarm.api.model.Playlists
import ru.nikstep.alarm.api.model.SpotifyPlaylistImage
import ru.nikstep.alarm.api.model.SpotifyUser
import ru.nikstep.alarm.client.spotify.SpotifyClient
import ru.nikstep.alarm.database.AlarmDao
import ru.nikstep.alarm.database.AlarmLogDao
import ru.nikstep.alarm.database.AppDatabase
import ru.nikstep.alarm.database.PlaylistDao
import ru.nikstep.alarm.service.LoginService
import ru.nikstep.alarm.service.SpotifyApiService
import ru.nikstep.alarm.service.alarm.AlarmController
import ru.nikstep.alarm.service.alarm.AndroidAlarmController
import ru.nikstep.alarm.service.alarm.AndroidAlarmManager
import ru.nikstep.alarm.service.data.AlarmDataService
import ru.nikstep.alarm.service.data.AlarmLogDataService
import ru.nikstep.alarm.service.data.DatabaseAlarmDataService
import ru.nikstep.alarm.service.data.DatabaseAlarmLogDataService
import ru.nikstep.alarm.service.data.DatabasePlaylistDataService
import ru.nikstep.alarm.service.data.PlaylistDataService
import ru.nikstep.alarm.service.file.CoverStorage
import ru.nikstep.alarm.service.log.LogService
import ru.nikstep.alarm.service.log.ToastLogService
import ru.nikstep.alarm.service.notification.AndroidNotificationService
import ru.nikstep.alarm.service.notification.NotificationService
import ru.nikstep.alarm.util.database.DATABASE_NAME
import ru.nikstep.alarm.util.database.getMigrations
import ru.nikstep.alarm.util.json.PlaylistJsonAdapter
import ru.nikstep.alarm.util.json.SpotifyPlaylistImageJsonAdapter
import ru.nikstep.alarm.util.json.SpotifyUserJsonAdapter


@Module
object DependencyModule {

    @Provides
    @Reusable
    fun roomDatabase(application: Application): AppDatabase =
        Room.databaseBuilder(application, AppDatabase::class.java, DATABASE_NAME)
            .addMigrations(*getMigrations())
            .allowMainThreadQueries().build()

    @Provides
    @Reusable
    fun converterFactory(): Converter.Factory {
        val moshi = Moshi.Builder()
            .add(Playlists::class.java, PlaylistJsonAdapter())
            .add(SpotifyUser::class.java, SpotifyUserJsonAdapter())
            .add(SpotifyPlaylistImage::class.java, SpotifyPlaylistImageJsonAdapter())
            .build()
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Reusable
    fun callAdapterFactory(): CallAdapter.Factory = CoroutineCallAdapterFactory()

    @Provides
    @Reusable
    fun okHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                Log.i("api-call", chain.request().toString())
                chain.proceed(chain.request())
            }).build()

    @Provides
    @Reusable
    fun spotifyRetrofit(
        converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(callAdapterFactory)
        .client(okHttpClient)
        .baseUrl(BuildConfig.SPOTIFY_API_BASE)
        .build()

    @Provides
    @Reusable
    fun loginService(): LoginService = LoginService()

    @Provides
    @Reusable
    fun spotifyApiService(spotifyApiClient: SpotifyApiClient, loginService: LoginService): SpotifyApiService =
        SpotifyApiService(spotifyApiClient, loginService)

    @Provides
    @Reusable
    fun spotifyApiClient(spotifyRetrofit: Retrofit): SpotifyApiClient =
        spotifyRetrofit.create(SpotifyApiClient::class.java)

    @Provides
    @Reusable
    fun alarmDao(appDatabase: AppDatabase) =
        appDatabase.alarmDao()

    @Provides
    @Reusable
    fun alarmDataService(alarmDao: AlarmDao): AlarmDataService =
        DatabaseAlarmDataService(alarmDao)

    @Provides
    @Reusable
    fun playlistDao(appDatabase: AppDatabase) =
        appDatabase.playlistDao()

    @Provides
    @Reusable
    fun playlistService(playlistDao: PlaylistDao): PlaylistDataService =
        DatabasePlaylistDataService(playlistDao)

    @Provides
    @Reusable
    fun alarmLogDao(appDatabase: AppDatabase) =
        appDatabase.alarmLogDao()

    @Provides
    @Reusable
    fun alarmLogDataService(alarmLogDao: AlarmLogDao): AlarmLogDataService =
        DatabaseAlarmLogDataService(alarmLogDao)

    @Provides
    @Reusable
    fun coverStorage(application: Application) =
        CoverStorage(application)

    @Provides
    @Reusable
    fun spotifyClient(application: Application) =
        SpotifyClient(application)

    @Provides
    @Reusable
    fun alarmManager(application: Application) =
        AndroidAlarmManager(application)

    @Provides
    @Reusable
    fun logService(application: Application): LogService =
        ToastLogService(application)

    @Provides
    @Reusable
    fun notificationService(application: Application): NotificationService =
        AndroidNotificationService(application)

    @Provides
    @Reusable
    fun alarmController(
        alarmManager: AndroidAlarmManager,
        alarmDataService: AlarmDataService,
        playlistDataService: PlaylistDataService,
        alarmLogDataService: AlarmLogDataService,
        spotifyClient: SpotifyClient,
        logService: LogService
    ): AlarmController =
        AndroidAlarmController(
            alarmManager,
            alarmDataService,
            playlistDataService,
            alarmLogDataService,
            spotifyClient,
            logService
        )
}