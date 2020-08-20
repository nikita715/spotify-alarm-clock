package ru.nikstep.alarm.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class BaseActivity<VM : ViewModel, VB : ViewBinding> : AppCompatActivity() {

    @Inject
    protected lateinit var viewModelProvider: ViewModelProvider.Factory

    protected val viewModel by lazy { initViewModel() }

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = initViewBinding()
    }

    abstract fun initViewBinding(): VB

    abstract fun initViewModel(): VM
}