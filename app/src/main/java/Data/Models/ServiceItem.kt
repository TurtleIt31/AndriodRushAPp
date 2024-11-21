package com.example.rushapp.models

data class ServiceItem(
    val serviceId: Long,
    val mechanicId: Long,
    val vehicleId: Long,
    val date: String,
    val serviceType: String,
    val serviceDescription: String
)