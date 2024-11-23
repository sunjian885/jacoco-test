package com.common.database;

import java.util.List;
import java.util.Map;

public class TDDLOperate {

    public TDDLOperate() {
    }

    public static int insert(String insertSql, Long uid) throws Exception {
        String connString = DBInfo.getConn();
        String rSql = convertSql(uid, insertSql);
        return DBOperate.insert(connString, rSql);
    }

    public static int execute(String executeSql, Long uid) throws Exception {
        String connString = DBInfo.getConn();
        String rSql = convertSql(uid, executeSql);
        return DBOperate.execute(connString, rSql);
    }

    public static List<Map<String, Object>> select(String selectSql, Long uid) throws Exception {
        String connString = DBInfo.getConn();
        String rSql = convertSql(uid, selectSql);
        return DBOperate.select(connString, rSql);
    }

    public static Map<String, Object> selectOne(String selectSql, Long uid) throws Exception {
        String connString = DBInfo.getConn();
        String rSql = convertSql(uid, selectSql);
        return DBOperate.selectOne(connString, rSql);
    }

    public static int insert(String connString, String insertSql, Long uid) throws Exception {
        String rSql = convertSql(uid, insertSql);
        return DBOperate.insert(connString, rSql);
    }

    public static int execute(String connString, String executeSql, Long uid) throws Exception {
        String rSql = convertSql(uid, executeSql);
        return DBOperate.execute(connString, rSql);
    }

    public static List<Map<String, Object>> select(String connString, String selectSql, Long uid) throws Exception {
        String rSql = convertSql(uid, selectSql);
        return DBOperate.select(connString, rSql);
    }

    public static Map<String, Object> selectOne(String connString, String selectSql, Long uid) throws Exception {
        String rSql = convertSql(uid, selectSql);
        return DBOperate.selectOne(connString, rSql);
    }

    private static String convertSql(Long uid, String sql) {
        long d_num = uid / 100L % 100L;
        String t_num = lpad(4, (int)(uid % 10000L));
        String[] tables = new String[]{"account_channel", "credit_his", "user", "user_device_binding", "user_ext", "user_relation", "user_reverse_relation"};
        String[] sqls;
        if (sql.indexOf(" where ") <= 0) {
            sql = sql + "  ";
            sqls = tables;
            int var7 = tables.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String table = sqls[var8];
                if (sql.indexOf(" " + table + " ") > 0) {
                    sql = sql.replace(" " + table + " ", " tddl_" + d_num + "." + table + "_" + t_num + " ");
                } else if (sql.indexOf(" tddl." + table + " ") > 0) {
                    sql = sql.replace(" tddl." + table + " ", " tddl_" + d_num + "." + table + "_" + t_num + " ");
                }
            }
        } else {
            sqls = sql.split(" where ");
            String t_sql = sqls[0] + "  ";
            String[] var13 = tables;
            int var14 = tables.length;

            for(int var10 = 0; var10 < var14; ++var10) {
                String table = var13[var10];
                if (t_sql.indexOf(" " + table + " ") > 0) {
                    t_sql = t_sql.replace(" " + table + " ", " tddl_" + d_num + "." + table + "_" + t_num + " ");
                } else if (t_sql.indexOf(" tddl." + table + " ") > 0) {
                    t_sql = t_sql.replace(" tddl." + table + " ", " tddl_" + d_num + "." + table + "_" + t_num + " ");
                }
            }

            sql = t_sql + "  where  " + sqls[1];
        }

        System.out.println(sql);
        return sql;
    }

    private static String lpad(int length, int number) {
        String f = "%0" + length + "d";
        return String.format(f, number);
    }
}
