package id.my.avzy.oneway.model

data class ChatState(
    val isEnteringToken: Boolean = false,
    val remoteToken: String = "",
    val messageText: String = ""
)
