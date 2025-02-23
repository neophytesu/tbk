import com.google.gson.Gson
import org.hyperledger.fabric.contract.Context
import org.hyperledger.fabric.contract.ContractInterface
import org.hyperledger.fabric.contract.annotation.Contract
import org.hyperledger.fabric.contract.annotation.Default
import org.hyperledger.fabric.contract.annotation.Transaction
import java.time.LocalDateTime

@Contract(name = "DidDocumentContract")
@Default
class DidDocumentContract : ContractInterface {
    private fun jsonString2Object(str: String, clz: Class<*>): Any {
        val gson = Gson()
        return gson.fromJson(str, clz)
    }

    private fun object2JsonString(obj: Any): String {
        val gson = Gson()
        return gson.toJson(obj)
    }

    //检查DID是否存在
    @Transaction
    fun didDocumentExists(ctx: Context, did: String): Boolean {
        val buffer = ctx.stub.getState(did)
        return buffer?.isNotEmpty() ?: false
    }

    //创建DID文档
    @Transaction
    fun createDidDocument(ctx: Context, didCreationString: String) {
        val didCreation = jsonString2Object(didCreationString, DidCreation::class.java) as DidCreation
        val did = didCreation.did
        val exists = didDocumentExists(ctx, did)
        if (exists) {
            throw RuntimeException("The DID Document $did already exists")
        }
        val document = DidDocument(
            did = did,
            publicKey1 = didCreation.publicKey1,
            publicKey2 = didCreation.publicKey2,
            signatureValue = didCreation.signature
        )
        val documentWrapper = DidDocumentWrapper(document)
        ctx.stub.putState(did, documentWrapper.toJSONString().toByteArray(Charsets.UTF_8))
    }

    //查询DID文档
    @Transaction
    fun readDidDocument(ctx: Context, didReadString: String): DidDocument {
        val didRead = jsonString2Object(didReadString, DidRead::class.java) as DidRead
        val did = didRead.did
        val exists = didDocumentExists(ctx, did)
        if (!exists) {
            throw RuntimeException("The DID Document $did not exists")
        }
        return DidDocumentWrapper.fromJSONString(ctx.stub.getState(did).toString(Charsets.UTF_8))
    }

    //更新DID文档
    @Transaction
    fun updateDidDocument(ctx: Context, didUpdateString: String) {
        val didUpdate = jsonString2Object(didUpdateString, DidUpdate::class.java) as DidUpdate
        val did = didUpdate.did
        val didRead = DidRead(did = did)
        val didDocument = readDidDocument(ctx, object2JsonString(didRead))
        if (didUpdate.signature !in didDocument.recovery) {
            throw RuntimeException("signature:${didUpdate.signature} not in recovery list")
        }
        didUpdate.publicKey1?.let { didDocument.publicKey[0].pubicKeyHex = it }
        didUpdate.publicKey2?.let { didDocument.publicKey[1].pubicKeyHex = it }
        didUpdate.serviceEndpoint?.let { didDocument.service[0].serviceEndpoint = it }
        didDocument.updated = LocalDateTime.now()
        ctx.stub.putState(did, DidDocumentWrapper(didDocument).toJSONString().toByteArray(Charsets.UTF_8))
    }

    //删除DID文档
    @Transaction
    fun deleteDidDocument(ctx: Context, didRevokeString: String) {
        val didRevoke = jsonString2Object(didRevokeString, DidRevoke::class.java) as DidRevoke
        val did = didRevoke.did
        val didDocument = readDidDocument(ctx, object2JsonString(DidRead(did = did)))
        if (didRevoke.signature !in didDocument.recovery) {
            throw RuntimeException("signature:${didRevoke.signature} not in recover list")
        }
        ctx.stub.delState(did)
    }
}