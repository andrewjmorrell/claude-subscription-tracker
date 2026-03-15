package com.example.subscriptiontracker.di

import com.example.subscriptiontracker.data.repository.SettingsRepositoryImpl
import com.example.subscriptiontracker.data.repository.SubscriptionRepositoryImpl
import com.example.subscriptiontracker.domain.repository.SettingsRepository
import com.example.subscriptiontracker.domain.repository.SubscriptionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun bindSubs(impl: SubscriptionRepositoryImpl): SubscriptionRepository
    @Binds @Singleton abstract fun bindSettings(impl: SettingsRepositoryImpl): SettingsRepository
}
