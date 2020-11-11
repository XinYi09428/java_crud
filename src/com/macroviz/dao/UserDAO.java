package com.macroviz.dao;
import com.macroviz.model.User;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
//資料庫連線
public class UserDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/cake_data?serverTimezone=UTC&useSSL=false";
    private String jdbcUsername = "admin";
    private String jdbcPass = "admin";
    private static final String INSERT_USER_SQL = "INSERT INTO users" + "  (name, email, created_at) VALUES " +
            " (?, ?, ?);";

    private static final String SELECT_USER_BY_ID = "select * from users where id =?";
    private static final String SELECT_ALL_USERS = "select * from users order by `created_at` desc";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ? ,updated_at= ?where id = ?;";

    public UserDAO(){}

    protected Connection getConnection(){
        Connection connection = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPass);

        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return connection;
    }
    public void insertUser(User user) throws SQLException {
        System.out.println(INSERT_USER_SQL);
        // try-with-resource statement will auto close the connection.
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {

        	preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCreated_at());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
    public User selectUser(int id){
        User user = null;
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            ResultSet rsUser = preparedStatement.executeQuery();
            while (rsUser.next()) {
                String name = rsUser.getString("name");
                String email = rsUser.getString("email");
                String created_at = rsUser.getString("created_at");
                String updated_at = rsUser.getString("updated_at");
                System.out.println(created_at);
                user = new User(id, name, email,created_at,updated_at);

            }
        }catch(SQLException e){
            printSQLException(e);
        }
        return user;
    }
    public List< User > selectAllUsers(){
        List< User > users = new ArrayList<>();

        try(Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);){
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String created_at = rs.getString("created_at");
                String updated_at = rs.getString("updated_at");
                users.add(new User(id, name, email,created_at,updated_at));
            }

        }catch(SQLException e){
            printSQLException(e);
        }
        return users;
    }
    public boolean deleteUser(int id) throws SQLException{
        boolean rowDeleted;
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);){
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }
    public boolean updateUser(User user) throws SQLException{
        boolean rowUpdated;
        try( Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);){
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getUpdated_at());
            statement.setInt(4, user.getId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }
        private void printSQLException(SQLException ex) {
            for (Throwable e: ex) {
                if (e instanceof SQLException) {
                    e.printStackTrace(System.err);
                    System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                    System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                    System.err.println("Message: " + e.getMessage());
                    Throwable t = ex.getCause();
                    while (t != null) {
                        System.out.println("Cause: " + t);
                        t = t.getCause();
                    }
                }
            }
        }
}
