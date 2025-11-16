package com.zela.app.repository;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.zela.app.Db.DbConfig;

public class StepRepo {

    private final DbConfig dbConfig;

    public StepRepo(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public Connection getConnection() throws SQLException {
        return dbConfig.getConnection();
    }

    private int getTypeId(String typeName, Connection c) throws SQLException {
        String query = "SELECT id FROM type WHERE name = ?";
        try (PreparedStatement ps = c.prepareStatement(query)) {
            ps.setString(1, typeName.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt("id");
            }
        }

        String insert = "INSERT INTO type (name) VALUES (?) RETURNING id";
        try (PreparedStatement ps = c.prepareStatement(insert)) {
            ps.setString(1, typeName.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt("id");
            }
        }

        throw new SQLException("Impossible de créer/récupérer le type : " + typeName);
    }

    public <T> void save(T entity) throws SQLException {
        Class<?> clazz = entity.getClass(); // recupere la classe de lobjet passe en parametre
        String typeName = clazz.getSimpleName();

        try (Connection c = getConnection()) {
            int typeId = getTypeId(typeName, c); // recupere l'id du type dans la table type

            int entityId;
            try (PreparedStatement ps = c.prepareStatement("SELECT nextval('entity_seq') AS id");
                    ResultSet rs = ps.executeQuery()) {
                rs.next();
                entityId = rs.getInt("id");
            }
            String sql = "INSERT INTO value (field, value, type_id, entity_id) VALUES (?, ?, ?,?)";

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true); // permet d'accéder aux champs privés du modele
                Object value;
                try {
                    value = field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, field.getName());
                    ps.setString(2, value != null ? value.toString() : null);
                    ps.setInt(3, typeId);
                    ps.setInt(4, entityId);
                    ps.executeUpdate();
                }
            }

            System.out.println(typeName + " inséré dans la table 'value' (type_id=" + typeId + ")");
        }
    }

    public <T> void saveEntities(List<T> entities) throws SQLException {
        for (T entity : entities) {
            save(entity);
        }
        // entities.forEach(this::save);
    }

    public <T> void updateAllEntities(T entity, int entityId) throws SQLException {
        Class<?> clazz = entity.getClass();
        String typeName = clazz.getSimpleName();

        try (Connection c = getConnection()) {
            int typeId = getTypeId(typeName, c);

            String sql = "DELETE FROM value WHERE entity_id IN ( SELECT entity_id FROM value WHERE field='actif' AND value='0'); ";
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object value;
                try {
                    value = field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, value != null ? value.toString() : null);
                    ps.setString(2, field.getName());
                    ps.setInt(3, typeId);
                    ps.executeUpdate();
                }
            }

            System.out.println(typeName + " mis à jour dans la table 'value' (type_id=" + typeId + ")");
        }
    }

    public int findEntityByField(String fieldName) throws SQLException {
        String sql = "SELECT entity_id FROM value WHERE field = ?";
        try (Connection c = getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, fieldName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("entity_id");
                } else {
                    throw new SQLException("Aucun entity trouvé pour " + fieldName);
                }
            }
        }
    }

    public void updateFieldValue(String fieldName, String newValue, int entityId) throws SQLException {
        String sql = "UPDATE value SET value = ? WHERE field = ? AND entity_id = ?";

        try (Connection c = getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, newValue);
            ps.setString(2, fieldName);
            ps.setInt(3, entityId);

            int updatedRows = ps.executeUpdate();
            System.out.println("Nombre de lignes mises à jour : " + updatedRows);
        }
    }

    public <T> void deleteEntityByFieldValue(T entity) throws SQLException {
        Class<?> clazz = entity.getClass();

        try (Connection c = getConnection()) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object value;
                try {
                    value = field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                if (value == null)
                    continue; // ignore null

                // Supprimer toutes les lignes avec le même entity_id pour ce field/value
                String sql = "DELETE FROM value WHERE entity_id IN (SELECT entity_id FROM value WHERE field = ? AND value = ? )";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, field.getName());
                    ps.setString(2, value.toString());
                    int deletedRows = ps.executeUpdate();
                    System.out.println("Supprimé " + deletedRows + " lignes pour entity_id avec field '"
                            + field.getName() + "' et value '" + value + "'");
                }
            }
        }
    }
}
