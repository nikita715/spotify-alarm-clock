package ru.nikstep.alarm.ui.settings.holder

import androidx.viewbinding.ViewBinding
import ru.nikstep.alarm.model.Identifiable

class Setting(
    override val id: Long,
    val title: String? = null,
    val description: String? = null,
    val type: SettingType,
    val itemBuilder: (Setting, ViewBinding) -> Unit
) : Identifiable