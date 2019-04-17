/*
 * Copyright (c) 2019. All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.wso2.sample.dbconn;

import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnectionFromJNDI extends AbstractMediator {

    private static final Log log = LogFactory.getLog(DBConnectionFromJNDI.class);
    private Object dbConObject;

    public DBConnectionFromJNDI() {
        try {
            dbConObject = new InitialContext().lookup("jdbc/WSO2ExternalDB");

        } catch (NamingException e) {
            log.info("Error while getting datasource configurations");
        }
    }

    public boolean mediate(MessageContext context) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            DataSource sampleDataSource = (DataSource) dbConObject;
            conn = sampleDataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT version()");
            if (rs.next()) {
                String version = rs.getString(1);
                context.setProperty("DatabaseResult", version);
                log.info("Database Version : " + version);
            }
        } catch (SQLException e) {
            log.info("Unable to get DB connection.");
            e.printStackTrace();
        } finally {
            closeConnection(conn);
            closeStatement(stmt);
            closeResultSet(rs);
        }
        return true;
    }

    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.info("Error while closing connection.");
            }
        }

    }

    private void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("Error while closing statement.");
            }
        }

    }

    private void closeResultSet(ResultSet reset) {
        if (reset != null) {
            try {
                reset.close();
            } catch (SQLException e) {
                log.info("Error while closing result set.");
            }
        }
    }
}
