package ru.nikstep.alarm.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import ru.nikstep.alarm.AlarmApp
import ru.nikstep.alarm.di.module.AppModule
import ru.nikstep.alarm.di.module.RepositoryModule
import ru.nikstep.alarm.di.module.ViewModelFactoryModule
import ru.nikstep.alarm.di.module.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        RepositoryModule::class,
        ViewModelFactoryModule::class,
        ViewModelModule::class,
        AppModule::class
    ]
)
interface AppComponent : AndroidInjector<AlarmApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun create(application: Application): Builder
        fun build(): AppComponent
    }

    override fun inject(app: AlarmApp)
}