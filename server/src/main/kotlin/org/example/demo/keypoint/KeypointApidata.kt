package org.example.demo.keypoint

import kotlinx.serialization.Serializable

@Serializable
data class KeypointResponse(
    val id: Int,
    val name: String,
    val description: String,
    val teacher: String
)

@Serializable
data class KeypointRequest(
    val name: String,
    val description: String,
    val teacher: String
)

@Serializable
data class KeypointsRequest(
    val name: String,
)