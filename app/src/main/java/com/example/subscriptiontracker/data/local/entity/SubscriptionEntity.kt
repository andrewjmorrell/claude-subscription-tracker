package com.example.subscriptiontracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.subscriptiontracker.domain.model.BillingCycle
import com.example.subscriptiontracker.domain.model.Category
import com.example.subscriptiontracker.domain.model.Subscription
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val amount: Double,
    val billingCycle: BillingCycle,
    val category: Category,
    val nextBillingDate: LocalDate,
    val notes: String = "",
    val trialEndDate: LocalDate? = null,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun toDomain() = Subscription(id, name, amount, billingCycle, category, nextBillingDate, notes, trialEndDate, isActive, createdAt, updatedAt)

    companion object {
        fun fromDomain(s: Subscription) = SubscriptionEntity(s.id, s.name, s.amount, s.billingCycle, s.category, s.nextBillingDate, s.notes, s.trialEndDate, s.isActive, s.createdAt, s.updatedAt)
    }
}
