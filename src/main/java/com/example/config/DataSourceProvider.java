package com.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.sql.DataSource;

/**
 * Provides a single HikariCP DataSource for the application lifecycle.
 */
@WebListener
public class DataSourceProvider implements ServletContextListener {

    private static HikariDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        HikariConfig config = new HikariConfig();

        String jdbcUrl = getInitParameter(context, "jdbc.url",
                "jdbc:mysql://localhost:3306/blog_sys?useSSL=false&serverTimezone=UTC");
        String username = getInitParameter(context, "jdbc.username", "root");
        String password = getInitParameter(context, "jdbc.password", "123456");

        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setPoolName("BlogHikariPool");
        // Connection validation and keep-alive to avoid MySQL closing idle connections
        config.setConnectionTestQuery("SELECT 1");
        // 回收空闲连接的时间（毫秒），例如 10 分钟
        config.setIdleTimeout(10 * 60 * 1000);
        // 连接的最长生存时间（毫秒），例如 30 分钟，确保过期后重建
        config.setMaxLifetime(30 * 60 * 1000);
        // 定期发送保活，避免连接被后端超时断开（JDBC 驱动和 HikariCP 会处理）
        config.setKeepaliveTime(5 * 60 * 1000);
        // Don't fail the webapp startup if DB is temporarily unreachable;
        // allow pool to start without initial connection and recover later.
        config.setInitializationFailTimeout(-1);
        try {
            dataSource = new HikariDataSource(config);
            context.setAttribute(DataSourceProvider.class.getName(), dataSource);
        } catch (Throwable t) {
            // Log the error to the servlet context so startup can continue and not fail the listener
            context.log("HikariDataSource initialization failed: " + t.getMessage(), t);
            dataSource = null;
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    private String getInitParameter(ServletContext context, String key, String defaultValue) {
        String value = context.getInitParameter(key);
        return value != null ? value : defaultValue;
    }

    public static DataSource getDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource has not been initialised yet.");
        }
        return dataSource;
    }
}
