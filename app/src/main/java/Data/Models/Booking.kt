package Data.Models

data class Booking(
    val bookingId: Long, // Optional primary key, if needed
    val serviceId: Long? = null, // Optional foreign key, if used
    val mechanicId: Long? = null, // Optional foreign key, if used
    val vehicleId: Long? = null, // Optional foreign key, if used
    val bookingDate: String,
    val status: String
)