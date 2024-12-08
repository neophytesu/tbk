package com.su.tbk.core

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.SIG
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

//有效期为一小时
const val JWT_TTL = 60 * 60 * 1000L

//秘钥明文
const val JWT_KEY = "Z2F1Y2hlbmVzc3l1cnV5dWNoZW5naHVhbmd5dWNoZW5n"

fun getUUID(): String {
    val token = UUID.randomUUID().toString().replace("-", "")
    return token
}

//生成jwt
fun createJWT(subject: String): String {
    val builder = getJwtBuilder(subject, null, getUUID())
    return builder.compact()
}

fun createJWT(subject: String, ttlMillis: Long): String {
    val builder = getJwtBuilder(subject, ttlMillis, getUUID())
    return builder.compact()
}

private fun getJwtBuilder(subject: String, ttlMillis: Long?, uuid: String): JwtBuilder {
    val signatureAlgorithm = SIG.HS256
    val secretKey = generalKey()
    val nowMillis = System.currentTimeMillis()
    val ttl = ttlMillis ?: JWT_TTL
    val expMillis = nowMillis + ttl
    val exp = Date(expMillis)
    return Jwts.builder()
        .id(uuid)
        .subject(subject)
        .issuer("su")
        .signWith(secretKey, signatureAlgorithm)
        .expiration(exp)
}

//生成token
fun createJWT(id: String, subject: String, ttlMillis: Long): String {
    val builder = getJwtBuilder(subject, ttlMillis, id)
    return builder.compact()
}

//生成加密后的秘钥
fun generalKey(): SecretKey {
    val encodedKey = Base64.getDecoder().decode(JWT_KEY)
    val key = SecretKeySpec(encodedKey, 0, encodedKey.size, "HmacSHA256")
    return key
}

//解析JWT
fun parseJWT(jwt: String): Claims {
    val secretKey = generalKey()
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(jwt)
        .payload
}







