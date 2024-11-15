package Data.Models

data class User(
    val userId: Long,
    val mechanicId: Long?,
    val customerId: Long?,
    var name: String,  // `var` makes this mutable and provides both getter and setter
    var email: String,
    var phone: String,
    var userType: String,
    var password: String
)
