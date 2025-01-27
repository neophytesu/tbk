package com.su.tbk.core

import com.su.tbk.common.AuditDataType
import com.su.tbk.common.OperationType
import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.MappedJdbcTypes
import org.apache.ibatis.type.MappedTypes
import java.sql.*


@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes(Array<String>::class)
class StringTypeHandler : BaseTypeHandler<Array<String>>() {
    override fun setNonNullParameter(ps: PreparedStatement?, i: Int, parameter: Array<String>?, jdbcType: JdbcType?) {
        val c = ps?.connection
        val inArray = c?.createArrayOf("VARCHAR", parameter)
        ps?.setArray(i, inArray)
    }

    override fun getNullableResult(rs: ResultSet?, columnName: String?): Array<String> {
        val outputArray = rs?.getArray(columnName)
        return convertToStringArray(outputArray)
    }

    override fun getNullableResult(rs: ResultSet?, columnIndex: Int): Array<String> {
        val outputArray = rs?.getArray(columnIndex)
        return convertToStringArray(outputArray)
    }

    override fun getNullableResult(cs: CallableStatement?, columnIndex: Int): Array<String> {
        val outputArray = cs?.getArray(columnIndex)
        return convertToStringArray(outputArray)
    }

    private fun convertToStringArray(outputArray: java.sql.Array?): Array<String> {
        outputArray?.let {
            val array = outputArray.array
            return if (array is Array<*>) {
                array.filterIsInstance<String>().toTypedArray()
            } else {
                emptyArray()
            }
        }
        return emptyArray()
    }
}

@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes(AuditDataType::class)
class AuditDataTypeHandler : BaseTypeHandler<AuditDataType>() {

    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: AuditDataType, jdbcType: JdbcType?) {
        ps.setObject(i, parameter.type,Types.OTHER)
    }

    override fun getNullableResult(rs: ResultSet, columnName: String): AuditDataType? {
        val type = rs.getString(columnName)
        return if (rs.wasNull()) null else AuditDataType.getByType(type)
    }

    override fun getNullableResult(rs: ResultSet, columnIndex: Int): AuditDataType? {
        val type = rs.getString(columnIndex)
        return if (rs.wasNull()) null else AuditDataType.getByType(type)
    }

    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): AuditDataType? {
        val type = cs.getString(columnIndex)
        return if (cs.wasNull()) null else AuditDataType.getByType(type)
    }
}
@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes(OperationType::class)
class OperateTypeHandler : BaseTypeHandler<OperationType>() {

    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: OperationType, jdbcType: JdbcType?) {
        ps.setObject(i, parameter.code,Types.OTHER)
    }

    override fun getNullableResult(rs: ResultSet, columnName: String): OperationType? {
        val type = rs.getInt(columnName)
        return if (rs.wasNull()) null else OperationType.getByCode(type)
    }

    override fun getNullableResult(rs: ResultSet, columnIndex: Int): OperationType? {
        val type = rs.getInt(columnIndex)
        return if (rs.wasNull()) null else OperationType.getByCode(type)
    }

    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): OperationType? {
        val type = cs.getInt(columnIndex)
        return if (cs.wasNull()) null else OperationType.getByCode(type)
    }
}