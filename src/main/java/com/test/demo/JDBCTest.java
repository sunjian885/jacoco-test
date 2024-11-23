package com.test.demo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JDBCTest {

    public static void main(String[] args) throws Exception{
        List<Map<String, Object>> selectResult = new ArrayList<Map<String, Object>>();
        try {
            //加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //提供JDBC连接的URL
            String url = "jdbc:mysql://192.168.4.82:3306/jy_biz_open_eval?user=root&password=zszc";
            //创建数据库的连接
            Connection con = null;
            try {
                con = DriverManager.getConnection(url);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            //sql语句
            String sql="SELECT open_start_time,section_id,section_name FROM jy_biz_open_eval.tp_open_section WHERE section_name LIKE \"%1109%\";";
            //创建一个statement执行者
            Statement statement = con.createStatement();
            //执行SQL语句
            ResultSet result = statement.executeQuery(sql);
            ResultSetMetaData md = result.getMetaData();

            //处理返回结果
            int columns = md.getColumnCount();
            System.out.println("++++++"+ columns);

            //循环输出结果集
            while (result.next()){
                Map<String, Object> row = new HashMap<String, Object>();
                for (int i = 1; i <= columns; i++) {
                    row.put(md.getColumnName(i),result.getObject(i));
                }
                selectResult.add(row);
            }
            System.out.println("====="+selectResult);
            //关闭JDBC对象
            con.close();
            result.close();
            statement.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
