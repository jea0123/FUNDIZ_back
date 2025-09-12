package com.example.funding.config;

import com.example.funding.handler.NullSafeTypeHandler;
import com.example.funding.handler.NumericTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * MyBatis Configuration Class
 * This class customizes MyBatis settings, including type handlers for null safety and numeric types.
 * @author 장민규
 */
@Configuration
@MapperScan("com.example.funding.mapper")
public class MyBatisConfig {

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            // Long -> NullSafeTypeHandler
            configuration.getTypeHandlerRegistry()
                    .register(Long.class, new NullSafeTypeHandler<>(Long.class));
            configuration.getTypeHandlerRegistry()
                    .register(long.class, new NullSafeTypeHandler<>(Long.class));

            // Integer
            configuration.getTypeHandlerRegistry()
                    .register(Integer.class, new NullSafeTypeHandler<>(Integer.class));
            configuration.getTypeHandlerRegistry()
                    .register(int.class, new NullSafeTypeHandler<>(Integer.class));

            // String
            configuration.getTypeHandlerRegistry()
                    .register(String.class, new NullSafeTypeHandler<>(String.class));

            // Date
            configuration.getTypeHandlerRegistry()
                    .register(Date.class, new NullSafeTypeHandler<>(Date.class));

            // NumericTypeHandler
            configuration.getTypeHandlerRegistry()
                    .register(Long.class, JdbcType.NUMERIC, new NumericTypeHandler<>(Long.class));
            configuration.getTypeHandlerRegistry()
                    .register(long.class, JdbcType.NUMERIC, new NumericTypeHandler<>(Long.class));

            // Integer
            configuration.getTypeHandlerRegistry()
                    .register(Integer.class, JdbcType.NUMERIC, new NumericTypeHandler<>(Integer.class));
            configuration.getTypeHandlerRegistry()
                    .register(int.class, JdbcType.NUMERIC, new NumericTypeHandler<>(Integer.class));
        };
    }
}
