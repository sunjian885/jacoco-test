package com.common.database;


import com.mysql.cj.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBOperate {

    private static String connectString = "";

    public DBOperate() {
    }

    public static int insert(String insertSql) throws Exception {
        if (StringUtils.isNullOrEmpty(connectString)) {
            connectString = DBInfo.getConn();
        }

        return insert(connectString, insertSql);
    }

    public static int execute(String executeSql) throws Exception {
        if (StringUtils.isNullOrEmpty(connectString)) {
            connectString = DBInfo.getConn();
        }

        return execute(connectString, executeSql);
    }

    public static List<Map<String, Object>> select(String selectSql) throws Exception {
        if (StringUtils.isNullOrEmpty(connectString)) {
            connectString = DBInfo.getConn();
        }

        return select(connectString, selectSql);
    }

    public static Map<String, Object> selectOne(String selectSql) throws Exception {
        if (StringUtils.isNullOrEmpty(connectString)) {
            connectString = DBInfo.getConn();
        }

        return selectOne(connectString, selectSql);
    }

    public static int insert(String connString, String insertSql) throws Exception {
        Connection conn = null;
        setConnection(connString);
        int ret_id = 0;

        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(insertSql);
            ResultSet res = stmt.executeQuery("select LAST_INSERT_ID()");
            if (res.next()) {
                ret_id = res.getInt(1);
            }
        } catch (Exception var9) {
            System.err.print("[Database.insert error]" + var9.toString());
            throw new Exception("[Database.insert error]" + var9.toString());
        } finally {
            if (null != conn) {
                conn.close();
            }

        }

        return ret_id;
    }

    public static int execute(String connString, String insertSql) throws Exception {
        Connection conn = null;
        setConnection(connString);
        boolean var3 = false;

        int ret_id;
        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            ret_id = stmt.executeUpdate(insertSql);
        } catch (Exception var8) {
            System.err.print("[Database.update error]" + var8.toString());
            throw new Exception("[Database.update error]" + var8.toString());
        } finally {
            if (null != conn) {
                conn.close();
            }

        }

        return ret_id;
    }

    public static List<Map<String, Object>> select(String connString, String selectSql) throws Exception {
        Connection conn = null;
        setConnection(connString);
        ArrayList ll = new ArrayList();

        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectSql);
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();

            while(rs.next()) {
                Map<String, Object> row = new HashMap();

                for(int i = 1; i <= columns; ++i) {
                    row.put(md.getColumnName(i), rs.getObject(i));
                }

                ll.add(row);
            }
        } catch (Exception var13) {
            System.err.print("[Database.select error]" + var13.toString());
            throw new Exception("[Database.select error]" + var13.toString());
        } finally {
            if (null != conn) {
                conn.close();
            }

        }

        return ll;
    }

    public static Map<String, Object> selectOne(String connString, String selectSql) throws Exception {
        List<Map<String, Object>> ll = select(connString, selectSql);
        return (Map)(ll.size() > 0 ? (Map)ll.get(0) : new HashMap());
    }

    private static Connection getConnection() throws Exception {
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(connectString);
            return conn;
        } catch (Exception var2) {
            throw new Exception("数据库连接失败" + var2.getMessage());
        }
    }

    public static void setConnection(String conn) {
        connectString = conn;
    }
}
