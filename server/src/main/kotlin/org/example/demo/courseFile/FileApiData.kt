package org.example.demo.courseFile

import kotlinx.serialization.Serializable

@Serializable
data class UploadFileRequest(
    val baseName: String,
    val extension: String,
    val file: ByteArray
)

@Serializable
data class DownloadFileResponse(
    val file: ByteArray
)

@Serializable
data class FileDescriptionResponse(
    val id: Int,
    val baseName: String,
    val extension: String
)