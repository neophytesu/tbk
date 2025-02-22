package com.su.tbk.service.impl

import com.su.tbk.service.ContractService
import org.hyperledger.fabric.gateway.Contract
import org.hyperledger.fabric.gateway.Network
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ContractServiceImpl : ContractService {
    @Autowired
    private lateinit var contract: Contract

    @Autowired
    private lateinit var network: Network
}