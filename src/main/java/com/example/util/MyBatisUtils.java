package com.example.util;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;

/**
 * Simple helper that loads {@code mybatis-config.xml} once and hands out
 * SqlSessions.
 */
public final class MyBatisUtils {

    private static final SqlSessionFactory SQL_SESSION_FACTORY;

    static {
        try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            DataSource ds = null;
            try {
                ds = com.example.config.DataSourceProvider.getDataSource();
            } catch (Throwable t) {
                // DataSourceProvider 可能尚未初始化，忽略并回退到配置文件
                ds = null;
            }

            if (ds != null) {
                // 使用已初始化的 DataSource 构建 Environment，并将其注入到 MyBatis Configuration
                // 注意：必须先 parse() 才能让 configuration 加载 mappers 等配置
                XMLConfigBuilder parser = new XMLConfigBuilder(Resources.getResourceAsReader("mybatis-config.xml"));
                org.apache.ibatis.session.Configuration configuration = parser.parse();

                TransactionFactory transactionFactory = new JdbcTransactionFactory();
                Environment env = new Environment("development", transactionFactory, ds);
                configuration.setEnvironment(env);
                SQL_SESSION_FACTORY = new SqlSessionFactoryBuilder().build(configuration);
            } else {
                // 退回到从配置文件读取 DataSource 的默认方式
                SQL_SESSION_FACTORY = new SqlSessionFactoryBuilder().build(reader);
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError("初始化 MyBatis SqlSessionFactory 失败: " + e.getMessage());
        }
    }

    private MyBatisUtils() {
    }

    /**
     * Opens a SqlSession with auto-commit enabled to simplify CRUD helpers.
     */
    public static SqlSession openSession() {
        if (SQL_SESSION_FACTORY == null) {
            throw new IllegalStateException("SqlSessionFactory 尚未初始化");
        }
        return SQL_SESSION_FACTORY.openSession(true);
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        if (SQL_SESSION_FACTORY == null) {
            throw new IllegalStateException("SqlSessionFactory 尚未初始化");
        }
        return SQL_SESSION_FACTORY;
    }
}
