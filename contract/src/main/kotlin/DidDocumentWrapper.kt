import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.hyperledger.fabric.contract.annotation.DataType
import org.hyperledger.fabric.contract.annotation.Property
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@DataType
class DidDocumentWrapper(@Property var didDocument: DidDocument) {

    fun toJSONString(): String {
        return gson.toJson(didDocument)
    }

    companion object {
        val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()
        fun fromJSONString(json: String): DidDocument {
            val didDocument = gson.fromJson(json, DidDocument::class.java)
            return didDocument
        }
    }
}
data class DidDocument(
    val `@context`: String="https://www.w3.org/ns/did/v1",
    var id:String,
    var version:Int=1,
    var created: LocalDateTime =LocalDateTime.now(),
    var updated: LocalDateTime =LocalDateTime.now(),
    val publicKey:List<PublicKey>,
    val authentication:List<String> = listOf("$id#keys-1"),
    val recovery:List<String> = listOf("$id#keys-2"),
    val service:List<Service> = listOf(Service(id="$id#resolver")),
    val proof:Proof,
)
{
    constructor(did:String,publicKey1:String,publicKey2:String,signatureValue: String):this(
        id=did,
        publicKey = listOf(PublicKey(id="$did#keys-1", type="Secp256k1", pubicKeyHex = publicKey1),
            PublicKey(id="$did#keys-2", type = "Secp256k2", pubicKeyHex = publicKey2)),
        proof = Proof(creator="$did#keys-1", signatureValue=signatureValue)
        )
}
data class PublicKey(
    var id:String,
    val type:String="Secp256k1",
    var pubicKeyHex:String?=null
)
data class Service(
    var id:String,
    val type:String="DIDResolve",
    var serviceEndpoint:String="https://su/did/resolve"
)
data class Proof(
    val type:String="Secp256k1",
    var creator:String,
    var signatureValue:String?=null
)
class LocalDateTimeAdapter:TypeAdapter<LocalDateTime>(){
    private val formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    override fun write(out: JsonWriter?, value: LocalDateTime?) {
        out?.value(value?.format(formatter))
    }

    override fun read(`in`: JsonReader?): LocalDateTime {
        return LocalDateTime.parse(`in`!!.nextString(), formatter)
    }
}
