package com.mgorski.discountasciiwarehouse.di

import android.app.Application
import com.mgorski.discountasciiwarehouse.asciiitemlist.suggestions.SuggestionsStorage
import com.mgorski.discountasciiwarehouse.network.AsciiItemsProvider
import com.mgorski.discountasciiwarehouse.network.NetworkCacheModule
import com.mgorski.discountasciiwarehouse.network.RetrofitModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, RetrofitModule::class, NetworkCacheModule::class))
interface AppComponent {

    companion object {
        lateinit var instance: AppComponent

        fun init(application: Application) {
            instance = DaggerAppComponent.builder()
                    .appModule(AppModule(application))
                    .build()
        }
    }

    fun getAsciiItemsProvider(): AsciiItemsProvider

    fun getSuggestionsStorage(): SuggestionsStorage
}