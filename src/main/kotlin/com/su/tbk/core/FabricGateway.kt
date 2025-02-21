package com.su.tbk.core

import org.hyperledger.fabric.gateway.Gateway
import org.hyperledger.fabric.gateway.Wallets
import kotlin.io.path.Path

fun startFabricGateway() {
    val walletDirectory= Path("wallet")
    val wallet=Wallets.newFileSystemWallet(walletDirectory)
    val networkConfigFile= Path("connection.json")
    val builder=Gateway.createBuilder()
        .identity(wallet,"user1")
        .networkConfig(networkConfigFile)
    builder.connect().use { gateway->
        val network=gateway.getNetwork("mychannel")
        val contract=network.getContract("fabcar")
        val createCarResult=contract.createTransaction("createCar").submit("CAR10", "VW", "Polo", "Grey", "Mary")
        println(String(createCarResult, Charsets.UTF_8))
        val queryAllCarsResult=contract.evaluateTransaction("queryAllCars")
        println(String(queryAllCarsResult, Charsets.UTF_8))
    }
}