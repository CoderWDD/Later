package com.example.notification.entity

data class MsgResponse(
    val code: MsgResponseCode = MsgResponseCode.START,
    val msg: String = "",
)


enum class MsgResponseCode(code: Int){
    MESSAGE(0),
    DONE(1),
    ERROR(2),
    CONTINUE(3),
    ROLE(4),
    START(5),
}