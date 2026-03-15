package com.example.subscriptiontracker.data.local.converter

import androidx.room.TypeConverter
import com.example.subscriptiontracker.domain.model.BillingCycle
import com.example.subscriptiontracker.domain.model.Category
import java.time.LocalDate
import java.time.LocalDateTime

class Converters {
    @TypeConverter fun fromLocalDate(d: LocalDate?): String? = d?.toString()
    @TypeConverter fun toLocalDate(s: String?): LocalDate? = s?.let { LocalDate.parse(it) }
    @TypeConverter fun fromLocalDateTime(d: LocalDateTime?): String? = d?.toString()
    @TypeConverter fun toLocalDateTime(s: String?): LocalDateTime? = s?.let { LocalDateTime.parse(it) }
    @TypeConverter fun fromBillingCycle(c: BillingCycle): String = c.name
    @TypeConverter fun toBillingCycle(s: String): BillingCycle = BillingCycle.valueOf(s)
    @TypeConverter fun fromCategory(c: Category): String = c.name
    @TypeConverter fun toCategory(s: String): Category = Category.valueOf(s)
}
