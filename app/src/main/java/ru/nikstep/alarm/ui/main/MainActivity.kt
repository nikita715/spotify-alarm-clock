package ru.nikstep.alarm.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import ru.nikstep.alarm.BuildConfig
import ru.nikstep.alarm.R
import ru.nikstep.alarm.databinding.ActivityMainBinding
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.ui.alarm.AlarmActivity
import ru.nikstep.alarm.ui.alarmlog.AlarmLogActivity
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.ui.common.onNavItemSelectedListener
import ru.nikstep.alarm.ui.main.alarms.AlarmItemTouchHelperCallback
import ru.nikstep.alarm.ui.main.alarms.AlarmListAdapter
import ru.nikstep.alarm.ui.notifications.NotificationsActivity
import ru.nikstep.alarm.util.data.observeResult
import ru.nikstep.alarm.util.preferences.getAppPreference
import ru.nikstep.alarm.util.preferences.setAppPreference
import ru.nikstep.alarm.util.startActivityWithIntent
import ru.nikstep.alarm.util.viewmodel.viewModelOf

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        createNotificationChannel()

        if (viewModel.hasAccessToken().not()) {
            val savedAccessToken = getAppPreference<String>(R.string.saved_spotify_access_token)
            val savedAccessTokenTimeout = getAppPreference<Long>(R.string.saved_spotify_access_token_timeout)
            if (savedAccessToken != null && savedAccessTokenTimeout != null && savedAccessTokenTimeout != -1L
                && savedAccessTokenTimeout > System.currentTimeMillis()
            ) {
                viewModel.setAccessToken(savedAccessToken)
            } else {
                val request = getAuthenticationRequest()
                AuthorizationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request)
            }
        }

        buildNewAlarmButtonListener()

        viewModel.getAlarms().observeResult(this, loadingBlock = {
            binding.mainContainer.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }, successBlock = { alarms ->
            val listAdapter = AlarmListAdapter(alarms, onAlarmLineClick)
            buildAlarmList(listAdapter)
            buildSwipeAlarmListener(listAdapter)
            binding.progressBar.visibility = View.GONE
            binding.mainContainer.visibility = View.VISIBLE
        })

        val notificationsMenuItem: MenuItem = binding.topAppBar.menu.findItem(R.id.notificationsPage)
        when (getAppPreference<Boolean>(R.string.saved_notifications_key)) {
            true -> Unit
            false -> notificationsMenuItem.setIcon(R.drawable.baseline_notifications_none_white_24dp)
        }
        notificationsMenuItem.setOnMenuItemClickListener {
            startActivityWithIntent(this, NotificationsActivity::class.java)
            true
        }

        val alarmLogsMenuItem: MenuItem = binding.topAppBar.menu.findItem(R.id.alarmLogPage)
        alarmLogsMenuItem.setOnMenuItemClickListener {
            startActivityWithIntent(this, AlarmLogActivity::class.java)
            true
        }

        val bottomNavigation: BottomNavigationView = binding.bottomNavigation
        bottomNavigation.setOnNavigationItemSelectedListener(onNavItemSelectedListener(this))
        bottomNavigation.menu.findItem(R.id.alarmPage).isChecked = true

        invalidateOptionsMenu()
    }

    private fun createNotificationChannel() {
        val manager = getSystemService(NotificationManager::class.java)
        if (manager.getNotificationChannel(CHANNEL_ID) == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Spotify Alarm Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            serviceChannel.enableLights(true)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun getAuthenticationRequest(): AuthorizationRequest = AuthorizationRequest.Builder(
        BuildConfig.SPOTIFY_CLIENT_ID, AuthorizationResponse.Type.TOKEN, BuildConfig.SPOTIFY_REDIRECT_URI
    )
        .setShowDialog(false)
        .setScopes(arrayOf("user-read-email", "playlist-read-private"))
        .setCampaign("your-campaign-token")
        .build()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            response.accessToken?.let { token ->
                viewModel.setAccessToken(token)
                setAppPreference<String>(R.string.saved_spotify_access_token, response.accessToken)
                setAppPreference<Long>(
                    R.string.saved_spotify_access_token_timeout,
                    System.currentTimeMillis() + 500 * response.expiresIn
                )
            } ?: Log.e("MainActivity", "Spotify access token is empty")
        }
    }

    private fun buildNewAlarmButtonListener() {
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, AlarmActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_alarm_activity, R.anim.close_main_activity)
        }
    }

    private fun buildAlarmList(listAdapter: AlarmListAdapter) {
        val alarmList = binding.alarmList
        alarmList.setHasFixedSize(true)
        alarmList.layoutManager = LinearLayoutManager(this)
        alarmList.adapter = listAdapter

        ItemTouchHelper(AlarmItemTouchHelperCallback { viewHolder ->
            listAdapter.removeItem(viewHolder.adapterPosition)
            viewModel.removeAlarm(viewHolder.itemView.tag as Long)
        }).attachToRecyclerView(alarmList)
    }

    private fun buildSwipeAlarmListener(listAdapter: AlarmListAdapter) {
        val mainContainer = binding.mainContainer
        mainContainer.setOnRefreshListener {
            viewModel.getAlarms().observeResult(this, successBlock = { alarms ->
                listAdapter.updateItems(alarms)
                mainContainer.isRefreshing = false
            }, errorBlock = {
                mainContainer.isRefreshing = false
            })
        }
    }

    private val onAlarmLineClick: (i: Alarm) -> Unit = { alarm ->
        startActivityWithIntent(applicationContext, AlarmActivity::class.java, ALARM_ID_EXTRA to alarm.id)
    }

    /**
     * Temporary debug method
     */
    fun nextSong(view: View) {
        val playlist = binding.playlistNameInput.text.toString()
        viewModel.play(playlist)
    }

    companion object {
        const val AUTH_TOKEN_REQUEST_CODE = 0x10
        const val CHANNEL_ID = "SPOTIFY_ALARM_SERVICE_CHANNEL"
        const val ALARM_ID_EXTRA = "alarmId"
    }

    override fun initViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun initViewModel(): MainViewModel = viewModelOf(viewModelProvider)
}
