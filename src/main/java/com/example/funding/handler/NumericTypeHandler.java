package com.example.funding.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>NUMERIC 타입 핸들러</p>
 * <p>Long과 Integer 타입을 지원.</p>
 * <p>데이터베이스의 NUMERIC 타입을 Java의 Long 또는 Integer로 매핑.</p>
 *
 * @since 2025-09-12
 * @author 장민규
 */
@MappedJdbcTypes(JdbcType.NUMERIC)
@MappedTypes({Long.class, Integer.class})
public class NumericTypeHandler<T extends Number> extends BaseTypeHandler<T> {

    private final Class<T> type;

    public NumericTypeHandler(Class<T> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        if (parameter instanceof Long) {
            ps.setLong(i, parameter.longValue());
        } else if (parameter instanceof Integer) {
            ps.setInt(i, parameter.intValue());
        } else {
            ps.setObject(i, parameter);
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        BigDecimal bd = rs.getBigDecimal(columnName);
        return convert(bd);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        BigDecimal bd = rs.getBigDecimal(columnIndex);
        return convert(bd);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        BigDecimal bd = cs.getBigDecimal(columnIndex);
        return convert(bd);
    }

    @SuppressWarnings("unchecked")
    private T convert(BigDecimal bd) {
        if (bd == null) return null;
        if (type == Long.class) {
            return (T) Long.valueOf(bd.longValue());
        } else if (type == Integer.class) {
            return (T) Integer.valueOf(bd.intValue());
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
}