package com.onysakura.tools.pcr.repository

import com.onysakura.tools.utils.StringUtils
import org.springframework.jdbc.core.RowMapper
import java.lang.reflect.Field
import java.sql.ResultSet

class JdbcRowMapperWrapper<T>(val clazz: Class<T>) : RowMapper<T> {

    override fun mapRow(rs: ResultSet, rowNum: Int): T {
        val t: T = clazz.getDeclaredConstructor().newInstance() as T
        for (field: Field in clazz.declaredFields) {
            val any: Any = rs.getObject(StringUtils.humpToUnderline(field.name))
            clazz.getDeclaredMethod(getMethodName(field.name), field.type).invoke(t, any)
        }
        return t
    }

    fun getMethodName(field: String): String {
        return "set" + field.substring(0, 1).toUpperCase() + field.substring(1)
    }
}