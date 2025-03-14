package com.su.tbk.core

import cn.hutool.core.codec.Base58
import cn.hutool.core.codec.Base64
import freemarker.template.Configuration
import freemarker.template.Template
import org.bouncycastle.crypto.ec.CustomNamedCurves
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
import org.bouncycastle.jcajce.provider.digest.RIPEMD160
import org.bouncycastle.jcajce.provider.digest.SHA256
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.File
import java.io.StringWriter
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.ECGenParameterSpec
import java.security.spec.ECParameterSpec
import java.security.spec.ECPrivateKeySpec
import java.time.Instant

//获取模版
fun getTemplate(templateName: String): Template {
    val cfg = Configuration(Configuration.VERSION_2_3_33)
    cfg.setDirectoryForTemplateLoading(File("src/main/resources/templates"))
    cfg.defaultEncoding = "UTF-8"
    return cfg.getTemplate(templateName)
}

//根据dataMap生成模版输出字符串
fun generateString(template: Template, dataMap: Map<String, String>): String {
    val out = StringWriter()
    template.process(dataMap, out)
    return out.toString()
}

//DID生成
fun generateDid(didDocument: String): String {
    //使用SHA256
    val sha256 = SHA256.Digest()
    val hashBytes1 = sha256.digest(didDocument.toByteArray(StandardCharsets.UTF_8))
    //使用ripeMd160
    val ripeMd160 = RIPEMD160.Digest()
    val hashBytes2 = ripeMd160.digest(hashBytes1)
    //使用base58编码
    val methodSpecificId = Base58.encode(hashBytes2)
    return "did:cpp:$methodSpecificId"
}

//使用Secp256k1生成秘钥
fun generateKeys(): KeyPair {
    val keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC")
    val ecGenParameterSpec = ECGenParameterSpec("secp256k1")
    keyPairGenerator.initialize(ecGenParameterSpec, SecureRandom())
    return keyPairGenerator.generateKeyPair()
}

//从十六进制私钥复原回私钥对象
fun recoverPrivateKey(privateKeyHex: String): PrivateKey {
    val privateKeyVal = BigInteger(privateKeyHex, 16)
    val curve = CustomNamedCurves.getByName("secp256r1")
    val parameterSpec = org.bouncycastle.jce.spec.ECParameterSpec(curve.curve, curve.g, curve.n, curve.h)
    val privateKeySpec = org.bouncycastle.jce.spec.ECPrivateKeySpec(privateKeyVal, parameterSpec)
    val keyFactory = KeyFactory.getInstance("EC", BouncyCastleProvider())
    val privateKey = keyFactory.generatePrivate(privateKeySpec) as BCECPrivateKey
    return privateKey
}

//生成16进制秘钥对值
fun generateHexKeys(keyPair: KeyPair): Pair<String, String> {
    val privateKey = keyPair.private as BCECPrivateKey
    val publicKey = keyPair.public as BCECPublicKey
    val privateKeyVal = privateKey.d
    val publicKeyBytes = publicKey.q.getEncoded(false)
    return Pair(BigInteger(1, publicKeyBytes).toString(16), privateKeyVal.toString(16))
}

//使用Secp256k1生成数字签名
fun sign(message: ByteArray, privateKey: PrivateKey): String {
    val signature = Signature.getInstance("SHA256withECDSA", "BC")
    signature.initSign(privateKey)
    signature.update(message)
    val signedBytes = signature.sign()
    return Base64.encode(signedBytes)
}

//生成BaseDID文档
fun generateBaseDidDocument(masterPublicKey: String, slavePublicKey: String): String {
    val template = getTemplate("BaseDidDocument.json.ftl")
    val dataMap = mapOf("key1" to masterPublicKey, "key2" to slavePublicKey)
    return generateString(template, dataMap)
}

//生成DID Document
fun generateDidDocument(did: String, masterPublicKey: String, slavePublicKey: String, signatureValue: String): String {
    val template = getTemplate("DidDocument.json.ftl")
    val nowInstant = Instant.now()
    val isoString = nowInstant.toString()
    val timestamp = nowInstant.toEpochMilli().toString()
    val dataMap =
        mapOf(
            "did" to did,
            "Key1Hex" to masterPublicKey,
            "Key2Hex" to slavePublicKey,
            "nowTime" to isoString,
            "timeStamp" to timestamp,
            "signature" to signatureValue
        )
    return generateString(template, dataMap)
}

//生成Did请求
fun generateDidRequest(did: String, didDocument: String): String {
    val template = getTemplate("CreateDidRequest.json.ftl")
    val timestamp = Instant.now().toEpochMilli().toString()
    val dataMap =
        mapOf(
            "did" to did,
            "timeStamp" to timestamp,
            "document" to didDocument
        )
    return generateString(template, dataMap)
}

//更新Did请求
fun updateDidRequest(did: String, didDocument: String, signatureValue: String): String {
    val template = getTemplate("UpdateDidRequest.json.ftl")
    val timestamp = Instant.now().toEpochMilli().toString()
    val dataMap =
        mapOf(
            "did" to did,
            "timeStamp" to timestamp,
            "document" to didDocument,
            "signature" to signatureValue
        )
    return generateString(template, dataMap)
}

//注销Did请求
fun revokeDidRequest(did: String, signatureValue: String): String {
    val template = getTemplate("RevokeDidRequest.json.ftl")
    val timestamp = Instant.now().toEpochMilli().toString()
    val dataMap =
        mapOf(
            "did" to did,
            "timeStamp" to timestamp,
            "signature" to signatureValue
        )
    return generateString(template, dataMap)
}

fun main() {
    Security.addProvider(BouncyCastleProvider())
    val keyPair1 = generateKeys()
    val keyPair2 = generateKeys()
    val publicKey1Hex = generateHexKeys(keyPair1).first
    val publicKey2Hex = generateHexKeys(keyPair2).first
    val didDocument = generateBaseDidDocument(publicKey1Hex, publicKey2Hex)
    val signatureValue = sign(didDocument.toByteArray(StandardCharsets.UTF_8), keyPair1.private)
    val did = generateDid(didDocument)
    val didDocumentStr = generateDidDocument(did, publicKey1Hex, publicKey2Hex, signatureValue)
    val didRequest = generateDidRequest(did, didDocumentStr)
    println(didRequest)
}