package com.example.rushapp.data.model

data class User(
    val userId: Long? = null, // Immutable, can be left null or use default value
    val mechanicId: Long? = null, // Immutable
    val customerId: Long? = null, // Immutable
    var name: String, // Mutable
    var userType: String, // Mutable if necessary
    var email: String = "", // Mutable
    var phone: String = "", // Mutable
    var password: String = "" // Mutable
)
