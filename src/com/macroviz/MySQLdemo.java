package com.macroviz;

import java.sql.*;
import java.sql.SQLException;

public class MySQLdemo {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/cake_house?serverTimezone=UTC&useSSL=false";
    static final String USER = "admin";
    static final String PASS = "admin";
    public static void main(String[] args) {
    	Connection conn = null;
        Statement stmt = null;
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	System.out.println("�s���ƾڮw...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            //�d��
            System.out.println(" ��Ҥ�Statement��H...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM users";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                // �q�L�r�q�˯�
                int id  = rs.getInt("id");
                String name = rs.getString("name");
                String mail = rs.getString("email");
    
                // ��X�ƾ�
                System.out.print("ID: " + id);
                System.out.print(", �ϥΪ�: " + name);
                System.out.print(", �q�l�H�c: " + mail);
                System.out.print("\n");
            }
            // �����Z����
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // �B�z JDBC ���~
            se.printStackTrace();
        }catch(Exception e){
            // �B�z Class.forName ���~
            e.printStackTrace();
        }finally{
            // �����귽
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// ���\������
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }
}
