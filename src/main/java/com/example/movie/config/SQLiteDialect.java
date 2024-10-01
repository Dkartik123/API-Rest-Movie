package com.example.movie.config;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.type.SqlTypes;

import java.sql.Types;

public class SQLiteDialect extends Dialect {

    public SQLiteDialect() {
        super(DatabaseVersion.make(3, 41)); // Define SQLite version here
    }

    // Correctly register column types
    @Override
    protected String columnType(int sqlTypeCode) {
        switch (sqlTypeCode) {
            case SqlTypes.BOOLEAN:
                return "integer";
            case SqlTypes.BLOB:
                return "blob";
            case SqlTypes.CLOB:
                return "clob";
            case SqlTypes.FLOAT:
            case SqlTypes.DOUBLE:
                return "real";
            case SqlTypes.INTEGER:
            case SqlTypes.BIGINT:
                return "integer";
            case SqlTypes.VARCHAR:
            case SqlTypes.CHAR:
                return "text";
            default:
                return super.columnType(sqlTypeCode);
        }
    }

    // Override the identity column support for SQLite
    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new SQLiteIdentityColumnSupport();
    }

    @Override
    public boolean hasAlterTable() {
        return false;
    }

    @Override
    public boolean dropConstraints() {
        return false;
    }

    @Override
    public String getAddColumnString() {
        return "add column";
    }

    @Override
    public String getForUpdateString() {
        return "";
    }

    @Override
    public boolean supportsOuterJoinForUpdate() {
        return false;
    }

    @Override
    public boolean supportsIfExistsBeforeTableName() {
        return true;
    }

    @Override
    public boolean supportsCascadeDelete() {
        return false;
    }
}
