package com.su.tbk.core

import cn.hutool.core.codec.Base58
import freemarker.template.Configuration
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
import org.bouncycastle.jcajce.provider.digest.RIPEMD160
import org.bouncycastle.jcajce.provider.digest.SHA256
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.File
import java.io.StringWriter
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.Security
import java.security.spec.ECGenParameterSpec

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

//生成BaseDID文档
fun generateBaseDidDocument(masterPublicKey: String, slavePublicKey: String): String {
    val cfg = Configuration(Configuration.VERSION_2_3_33)
    cfg.setDirectoryForTemplateLoading(File("src/main/resources/templates"))
    cfg.defaultEncoding = "UTF-8"
    val template = cfg.getTemplate("BaseDidDocument.json.ftl")
    val dataMap = mapOf("key1" to masterPublicKey, "key2" to slavePublicKey)
    /*
    val out = FileWriter("src/main/resources/output/base_did_document.json")
    template.process(dataMap, out)
    */
    val out = StringWriter()
    template.process(dataMap, out)
    return out.toString()
}

//使用Secp256k1生成秘钥
fun generateKeys(): Pair<BigInteger, BigInteger> {
    val keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC")
    val ecGenParameterSpec = ECGenParameterSpec("secp256k1")
    keyPairGenerator.initialize(ecGenParameterSpec, SecureRandom())
    val keyPair = keyPairGenerator.generateKeyPair()
    val privateKey = keyPair.private as BCECPrivateKey
    val publicKey = keyPair.public as BCECPublicKey
    val privateKeyVal = privateKey.d
    val publicKeyBytes = publicKey.q.getEncoded(false)
    return Pair(BigInteger(1, publicKeyBytes), privateKeyVal)
}

fun main() {
    Security.addProvider(BouncyCastleProvider())
    val keyPair = generateKeys()
    val didDocument = generateBaseDidDocument(keyPair.first.toString(16), keyPair.second.toString(16))
    println(generateDid(didDocument))
}