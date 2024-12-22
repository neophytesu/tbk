{
  "@context": "https://w3id.org/did/v1",
  "id": "${did}",
  "version": 1,
  "created": "${nowTime}",
  "updated": "${nowTime}",
  "publicKey": [
    {
      "id": "${did}#keys-1",
      "type": "Secp256k1",
      "publicKeyHex": "02b97c30de767f084ce3080168ee293053ba33b235d7116a3263d29f1450936b71"
    },
    {
      "id": "${did}#keys-2",
      "type": "Secp256k1",
      "publicKeyHex": "e3080168ee293053ba33b235d7116a3263d29f1450936b71"
    }
  ],
  "authentication": ["${did}#key-1"],
  "recovery": ["${did}#key-2"],
  "service": [
    {
      "id": "${did}#resolver",
      "type": "DIDResolve",
      "serviceEndpoint": "https://did.baidu.com"
    }
  ],
  "proof": {
    "type": "Secp256k1",
    "creator": "${did}#keys-1",
    "signatureValue": "${signature}"
  }
}