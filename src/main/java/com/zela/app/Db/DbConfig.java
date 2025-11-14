package com.zela.app.Db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DbConfig {
    private final String url;
    private final String user;
    private final String password;

    public DbConfig(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public static DbConfig fromResource(String resource) throws IOException {
        Properties p = new Properties();
        try (InputStream is = DbConfig.class.getClassLoader().getResourceAsStream(resource)) {
            if (is == null)
                throw new IOException("Resource not found" + resource);
            p.load(is);
        }
        String url = p.getProperty("db.url");
        String user = p.getProperty("db.user");
        String pass = p.getProperty("db.password");
        return new DbConfig(url, user, pass);
    }

    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        conn.setAutoCommit(true);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET search_path TO stepper_db");
        }
        return conn;
    }
}
