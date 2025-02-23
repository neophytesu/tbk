package com.su.tbk.service.impl

import com.google.gson.Gson
import com.su.tbk.common.*
import com.su.tbk.core.generateBaseDidDocument
import com.su.tbk.core.recoverPrivateKey
import com.su.tbk.core.sign
import com.su.tbk.service.ContractService
import org.hyperledger.fabric.gateway.Contract
import org.hyperledger.fabric.gateway.Network
import org.hyperledger.fabric.sdk.Peer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.*

@Service
class ContractServiceImpl : ContractService {
    @Autowired
    private lateinit var contract: Contract

    @Autowired
    private lateinit var network: Network

    fun object2JsonString(obj: Any): String {
        val gson = Gson()
        return gson.toJson(obj)
    }
    override fun creatDidDocument(request: CreateDidDocumentRequest): Boolean {
        val didBaseDocument = generateBaseDidDocument(request.publicKey1, request.publicKey2)
        val privateKey = recoverPrivateKey(request.privateKey1)
        val signature = sign(didBaseDocument.toByteArray(StandardCharsets.UTF_8), privateKey)
        val didCreation = DidCreation(request.did, request.publicKey1, request.publicKey2, signature)
        try {
            contract.createTransaction("createDidDocument")
                .setEndorsingPeers(network.channel.getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                .submit(object2JsonString(didCreation))
        } catch (ex: Exception) {
            return false
        }
        return true
    }

    override fun getDidDocument(did: String): String {
        val didRead = DidRead(did)
        val result: String?
        try {
            result = contract.evaluateTransaction("readDidDocument", object2JsonString(didRead)).contentToString()
        } catch (ex: Exception) {
            return ""
        }
        return result
    }

    override fun deleteDidDocument(request: DeleteDidDocumentRequest): Boolean {
        val didBaseDocument = generateBaseDidDocument(request.publicKey1, request.publicKey2)
        val privateKey = recoverPrivateKey(request.privateKey1)
        val signature = sign(didBaseDocument.toByteArray(StandardCharsets.UTF_8), privateKey)
        val didRevoke = DidRevoke(request.id, signature)
        try {
            contract.createTransaction("deleteDidDocument")
                .setEndorsingPeers(network.channel.getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                .submit(object2JsonString(didRevoke))
        } catch (ex: Exception) {
            return false
        }
        return true
    }

    override fun updateDidDocument(request: UpdateDidDocumentRequest): Boolean {
        val didBaseDocument = generateBaseDidDocument(request.publicKey1, request.publicKey2)
        val privateKey = recoverPrivateKey(request.privateKey1)
        val signature = sign(didBaseDocument.toByteArray(StandardCharsets.UTF_8), privateKey)
        val didRevoke =
            DidUpdate(request.id, request.publicKey1, request.publicKey2, request.serviceEndpoint, signature)
        try {
            contract.createTransaction("updateDidDocument")
                .setEndorsingPeers(network.channel.getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                .submit(object2JsonString(didRevoke))
        } catch (ex: Exception) {
            return false
        }
        return true
    }
}