package com.example.subscriptiontracker.domain.repository

import com.example.subscriptiontracker.domain.model.Subscription
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    fun getAll(): Flow<List<Subscription>>
    fun getActive(): Flow<List<Subscription>>
    suspend fun getById(id: Long): Subscription?
    fun search(query: String): Flow<List<Subscription>>
    suspend fun insert(subscription: Subscription): Long
    suspend fun update(subscription: Subscription)
    suspend fun delete(subscription: Subscription)
    suspend fun deleteById(id: Long)
}
