package com.su.tbk.service

import com.su.tbk.common.CreateDidDocumentRequest
import com.su.tbk.common.DeleteDidDocumentRequest
import com.su.tbk.common.UpdateDidDocumentRequest


interface ContractService {
    fun creatDidDocument(request: CreateDidDocumentRequest): Boolean
    fun getDidDocument(did: String): String?
    fun deleteDidDocument(request: DeleteDidDocumentRequest): Boolean
    fun updateDidDocument(request: UpdateDidDocumentRequest): Boolean
}