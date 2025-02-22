package com.su.tbk.config

import org.hyperledger.fabric.gateway.*
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

@Configuration
class HyperLedgerFabricGatewayConfig(
    val hyperLedgerFabricProperties: HyperLedgerFabricProperties
) {
    @Bean
    fun gateway(): Gateway {
        val certificate =
            Files.newBufferedReader(Paths.get(hyperLedgerFabricProperties.certificatePath), StandardCharsets.UTF_8)
                .use { Identities.readX509Certificate(it) }
        val privateKey =
            Files.newBufferedReader(Paths.get(hyperLedgerFabricProperties.privateKeyPath), StandardCharsets.UTF_8)
                .use { Identities.readPrivateKey(it) }
        val wallet = Wallets.newInMemoryWallet()
        wallet.put("user1", Identities.newX509Identity("Org1MSP", certificate, privateKey))
        val gateway = Gateway.createBuilder()
            .identity(wallet, "user1")
            .networkConfig(Paths.get(hyperLedgerFabricProperties.networkConfigPath))
            .connect()
        return gateway
    }

    @Bean
    fun network(gateway: Gateway): Network {
        return gateway.getNetwork(hyperLedgerFabricProperties.channelName)
    }

    @Bean
    fun contract(network: Network): Contract {
        return network.getContract(hyperLedgerFabricProperties.contractName)
    }
}

@ConfigurationProperties(prefix = "fabric")
data class HyperLedgerFabricProperties @ConstructorBinding constructor(
    val networkConfigPath: String,
    val certificatePath: String,
    val privateKeyPath: String,
    val channelName: String,
    val contractName: String
)
