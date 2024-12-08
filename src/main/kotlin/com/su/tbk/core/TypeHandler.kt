package com.su.tbk.core

import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.MappedJdbcTypes
import org.apache.ibatis.type.MappedTypes
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

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