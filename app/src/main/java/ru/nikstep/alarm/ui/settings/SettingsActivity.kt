package ru.nikstep.alarm.ui.settings

import android.media.AudioManager
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import ru.nikstep.alarm.R
import ru.nikstep.alarm.databinding.ActivitySettingsBinding
import ru.nikstep.alarm.databinding.SettingDiscreteSliderItemBinding
import ru.nikstep.alarm.databinding.SettingItemBinding
import ru.nikstep.alarm.databinding.SettingSliderItemBinding
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.ui.settings.holder.Setting
import ru.nikstep.alarm.ui.settings.holder.SettingType
import ru.nikstep.alarm.util.preferences.getAppPreference
import ru.nikstep.alarm.util.preferences.setAppPreference
import ru.nikstep.alarm.util.viewmodel.viewModelOf
import kotlin.math.roundToInt

class SettingsActivity : BaseActivity<SettingsViewModel, ActivitySettingsBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        showSettings(binding.settingsList, createSettings())
    }

    private fun showSettings(settingsListView: RecyclerView, settings: List<Setting>) {
        settingsListView.setHasFixedSize(true)
        settingsListView.layoutManager = LinearLayoutManager(this)
        settingsListView.adapter = SettingsListAdapter(settings)
    }

    private fun createSettings(): List<Setting> = listOf(
        Setting(0, type = SettingType.ONE_LINE, description = "Remove all alarms") { setting, itemBinding ->
            (itemBinding as SettingItemBinding).let { settingBinding ->
                settingBinding.root.tag = setting.id
                settingBinding.settingText.text = setting.description
                settingBinding.root.setOnClickListener {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Are you sure?")
                        .setNegativeButton("No") { dialog, which ->
                            dialog.cancel()
                        }
                        .setPositiveButton("Yes") { dialog, which ->
                            viewModel.deleteAllAlarms()
                            dialog.cancel()
                            Snackbar.make(binding.root, "All alarms are deleted", Snackbar.LENGTH_LONG).show()
                        }
                        .show()
                }
            }
        },
        Setting(1, type = SettingType.SLIDER, title = "Alarm volume level") { setting, itemBinding ->
            (itemBinding as SettingSliderItemBinding).let { settingBinding ->
                val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
                settingBinding.root.tag = setting.id
                settingBinding.settingText.text = setting.title
                settingBinding.slider.let { slider ->
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM).let { maxVolume ->
                        slider.valueTo = maxVolume.toFloat()
                        slider.stepSize = (maxVolume / 50).toFloat()
                    }
                    slider.labelBehavior = LabelFormatter.LABEL_GONE
                    slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                        override fun onStartTrackingTouch(slider: Slider) {}
                        override fun onStopTrackingTouch(slider: Slider) {
                            audioManager.setStreamVolume(
                                AudioManager.STREAM_ALARM,
                                slider.value.roundToInt(), AudioManager.FLAG_SHOW_UI
                            )
                        }
                    })
                }
            }
        },
        Setting(2, type = SettingType.DISCRETE_SLIDER, title = "Alarm reminder time") { setting, itemBinding ->
            (itemBinding as SettingDiscreteSliderItemBinding).let { settingBinding ->
                settingBinding.root.tag = setting.id
                settingBinding.settingText.text = setting.title
                settingBinding.slider.let { slider ->
                    slider.valueFrom = 0f
                    slider.valueTo = 60f
                    slider.stepSize = 5f
                    slider.value = getAppPreference<Int>(R.string.preference__alarm_reminder_minutes)?.toFloat()
                        ?: DEFAULT_ALARM_REMINDER_MINUTES.toFloat()
                    slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                        override fun onStartTrackingTouch(slider: Slider) {}
                        override fun onStopTrackingTouch(slider: Slider) {
                            slider.isEnabled = false
                            slider.value.roundToInt().let { reminderMinutes ->
                                setAppPreference(R.string.preference__alarm_reminder_minutes, reminderMinutes)
                                Snackbar.make(
                                    binding.root,
                                    "Reminder set at $reminderMinutes minutes",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
//                            viewModel.updateAlarmReminders()
                            slider.isEnabled = true
                        }
                    })
                }
            }
        }
    )

    override fun initViewBinding(): ActivitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)

    override fun initViewModel(): SettingsViewModel = viewModelOf(viewModelProvider)

    companion object {
        const val DEFAULT_ALARM_REMINDER_MINUTES = 30
    }
}