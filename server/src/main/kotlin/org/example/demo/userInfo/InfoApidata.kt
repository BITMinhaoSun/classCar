package org.example.demo.userInfo

import kotlinx.serialization.Serializable

@Serializable
data class SearchInfoRequest(
    val name: String,
)


@Serializable
data class SearchInfoResponse(
    val name: String,
    val school: String,
    val phone_number: String,
    val e_mail: String,
    val avatar: Int,
)
@Serializable
data class UpdateInfoRequest(
    val name: String,
    val school: String,
    val phone_number: String,
    val e_mail: String,
    val avatar: Int,
)
@Serializable
data class UpdateInfoResponse(
    val name: String,
    val school: String,
    val phone_number: String,
    val e_mail: String,
    val avatar: Int,
)
