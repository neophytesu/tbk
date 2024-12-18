{
  "@context": "https://w3id.org/did/v1",
  "publicKey": [
    {
      "id": "#keys-1",
      "type": "Secp256k1",
      "publicKeyHex": "${key1}"
    },
    {
      "id": "#keys-2",
      "type": "Secp256k1",
      "publicKeyHex": "${key2}"
    }
  ],
  "authentication": ["#key-1"],
  "recovery": ["#key-2"]
}