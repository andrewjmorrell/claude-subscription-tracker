package com.example.subscriptiontracker.data.repository

import com.example.subscriptiontracker.data.local.dao.SubscriptionDao
import com.example.subscriptiontracker.data.local.entity.SubscriptionEntity
import com.example.subscriptiontracker.domain.model.Subscription
import com.example.subscriptiontracker.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionRepositoryImpl @Inject constructor(
    private val dao: SubscriptionDao
) : SubscriptionRepository {
    override fun getAll(): Flow<List<Subscription>> = dao.getAll().map { list -> list.map { it.toDomain() } }
    override fun getActive(): Flow<List<Subscription>> = dao.getActive().map { list -> list.map { it.toDomain() } }
    override suspend fun getById(id: Long): Subscription? = dao.getById(id)?.toDomain()
    override fun search(query: String): Flow<List<Subscription>> = dao.search(query).map { list -> list.map { it.toDomain() } }
    override suspend fun insert(subscription: Subscription): Long = dao.insert(SubscriptionEntity.fromDomain(subscription))
    override suspend fun update(subscription: Subscription) = dao.update(SubscriptionEntity.fromDomain(subscription))
    override suspend fun delete(subscription: Subscription) = dao.delete(SubscriptionEntity.fromDomain(subscription))
    override suspend fun deleteById(id: Long) = dao.deleteById(id)
}
