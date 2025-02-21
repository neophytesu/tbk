data class DidCreation(val did:String, val publicKey1:String, val publicKey2:String, val signature:String)
data class DidRead(val did: String)
data class DidUpdate(val did:String, val publicKey1:String?, val publicKey2:String?,val serviceEndpoint:String?, val signature:String)
data class DidRevoke(val did:String,val signature: String)