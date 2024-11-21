package Data.Models

data class Appointment(
    val bookingId: Long,
    val serviceId: Long,
    val mechanicId: Long,
    val vehicleId: Long,
    val customerId: Long,
    val bookingDate: String,
    val bookingStatus: String
)