package com.su

import org.hyperledger.fabric.shim.Chaincode
import org.hyperledger.fabric.shim.ChaincodeBase
import org.hyperledger.fabric.shim.ChaincodeStub

class TestChainCode : ChaincodeBase() {
    override fun init(p0: ChaincodeStub?): Chaincode.Response {
        return Chaincode.Response(Chaincode.Response.Status.SUCCESS, "OK", byteArrayOf())
    }

    override fun invoke(p0: ChaincodeStub?): Chaincode.Response {
        return Chaincode.Response(Chaincode.Response.Status.SUCCESS, "Hello, world!", byteArrayOf())
    }
}