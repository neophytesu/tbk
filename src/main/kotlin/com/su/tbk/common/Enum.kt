package com.su.tbk.common

enum class AuditDataType(val type: String) {
    DID("DID");

    companion object {
        fun getByType(type: String): AuditDataType? {
            return entries.find { it.type == type }
        }
    }
}

enum class OperationType(val code: Int) {
    CREATE(0),
    DELETE(1),
    UPDATE(2),
    SELECT(3);

    companion object {
        fun getByCode(code: Int): OperationType? {
            return entries.find { it.code == code }
        }
    }
}