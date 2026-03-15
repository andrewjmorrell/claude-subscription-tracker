package com.example.subscriptiontracker.domain.model

/** Subscription categories. */
enum class Category(val label: String) {
    STREAMING("Streaming"),
    MUSIC("Music"),
    GAMING("Gaming"),
    PRODUCTIVITY("Productivity"),
    CLOUD_STORAGE("Cloud Storage"),
    NEWS("News"),
    FITNESS("Fitness"),
    EDUCATION("Education"),
    SOFTWARE("Software"),
    FOOD_DELIVERY("Food Delivery"),
    SHOPPING("Shopping"),
    FINANCE("Finance"),
    OTHER("Other")
}
