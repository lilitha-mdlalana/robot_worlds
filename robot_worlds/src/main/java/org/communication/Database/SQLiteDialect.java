package org.communication.Database;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;

import java.sql.Types;

public class SQLiteDialect extends Dialect {

    public SQLiteDialect() {
        super();
        registerColumnType(Types.INTEGER, "integer");
        registerColumnType(Types.VARCHAR, 255, "varchar($l)");
        registerColumnType(Types.VARCHAR, "text");
        registerColumnType(Types.TIMESTAMP, "datetime");
        registerColumnType(Types.BOOLEAN, "integer");
    }



    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new IdentityColumnSupportImpl();
    }


    @Override
    public String getAddColumnString() {
        return "add column";
    }

    @Override
    public boolean dropConstraints() {
        return false;
    }

    @Override
    public String getDropForeignKeyString() {
        return "";
    }


}
