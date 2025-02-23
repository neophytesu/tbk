package com.su.tbk.common

data class CreateDidDocumentRequest(
    val did: String,
    val publicKey1: String,
    val publicKey2: String,
    val privateKey1: String,
    val privateKey2: String
)

data class DeleteDidDocumentRequest(
    val id: String,
    val publicKey1: String,
    val publicKey2: String,
    val privateKey1: String,
    val privateKey2: String
)
data class UpdateDidDocumentRequest(
    val id: String,
    val publicKey1: String,
    val publicKey2: String,
    val privateKey1: String,
    val privateKey2: String,
    val serviceEndpoint:String
)