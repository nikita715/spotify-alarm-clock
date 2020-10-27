package ru.nikstep.alarm.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import ru.nikstep.alarm.BuildConfig
import ru.nikstep.alarm.R
import ru.nikstep.alarm.databinding.ActivityMainBinding
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.model.type.AlarmChangeType
import ru.nikstep.alarm.ui.alarm.AlarmActivity
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.ui.base.buildTopAppBar
import ru.nikstep.alarm.ui.common.onNavItemSelectedListener
import ru.nikstep.alarm.ui.main.alarms.AlarmItemTouchHelperCallback
import ru.nikstep.alarm.ui.main.alarms.AlarmListAdapter
import ru.nikstep.alarm.util.data.observeResult
import ru.nikstep.alarm.util.preferences.getAppPreference
import ru.nikstep.alarm.util.preferences.setAppPreference
import ru.nikstep.alarm.util.showSnackbar
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
            val listAdapter = AlarmListAdapter(alarms, onAlarmLineClick, onSwitchAlarmClick)
            buildAlarmList(listAdapter)
            buildSwipeAlarmListener(listAdapter)
            binding.progressBar.visibility = View.GONE
            binding.mainContainer.visibility = View.VISIBLE
        })

        val bottomNavigation: BottomNavigationView = binding.bottomNavigation
        bottomNavigation.setOnNavigationItemSelectedListener(onNavItemSelectedListener(this))
        bottomNavigation.menu.findItem(R.id.alarmPage).isChecked = true

        checkExtras()

        buildTopAppBar(binding.topAppBar)

        invalidateOptionsMenu()
    }

    private fun checkExtras() {
        val alarmChangeType = intent.getSerializableExtra(AlarmChangeType.EXTRA_NAME) as AlarmChangeType?
        if (alarmChangeType != null) {
            val alarmTime = intent.getStringExtra(AlarmChangeType.TIME_EXTRA_NAME)
            val message = when (alarmChangeType) {
                AlarmChangeType.CREATE -> "Alarm scheduled at $alarmTime"
                AlarmChangeType.UPDATE -> "Alarm rescheduled at $alarmTime"
                AlarmChangeType.DELETE -> "Alarm is removed"
            }
            showSnackbar(binding.mainCoordinatorLayout, message)
        }
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
            viewModel.removeAlarm(viewHolder.itemView.tag as Long).observeResult(this, successBlock = {
                listAdapter.removeItem(viewHolder.adapterPosition)
                showSnackbar(binding.mainCoordinatorLayout, "Alarms is removed")
            }, errorBlock = {
                showSnackbar(binding.mainCoordinatorLayout, "Error during the removal of the alarm")
            })
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
                showSnackbar(binding.mainCoordinatorLayout, "Error during the update of alarms")
            })
        }
    }

    private val onAlarmLineClick: (i: Alarm) -> Unit = { alarm ->
        startActivityWithIntent(applicationContext, AlarmActivity::class.java, ALARM_ID_EXTRA to alarm.id)
    }

    private val onSwitchAlarmClick: (alarm: Alarm, switch: SwitchMaterial) -> Unit = { alarm, switch ->
        switch.isEnabled = false
        if (switch.isChecked) {
            viewModel.enableAlarm(alarm)
        } else {
            viewModel.disableAlarm(alarm)
        }.observeResult(this, successBlock = { receivedAlarm ->
            if (receivedAlarm == null || receivedAlarm.active != switch.isChecked) {
                showSnackbar(binding.mainCoordinatorLayout, "Unable to change the status")
            } else {
                showSnackbar(
                    binding.mainCoordinatorLayout, "Alarm at ${receivedAlarm.getTimeAsString()}" +
                            " is ${if (receivedAlarm.active) "activated" else "deactivated"}"
                )
            }
            switch.isEnabled = true
        }, errorBlock = {
            switch.isChecked = switch.isChecked.not()
            switch.isEnabled = true
        })
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
