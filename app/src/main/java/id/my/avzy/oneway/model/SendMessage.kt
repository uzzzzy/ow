package id.my.avzy.oneway.model

data class SendMessage(
    val to: String?,
    val notification: NotificationBody
)

data class NotificationBody(
    val title: String,
    val body: String
)
