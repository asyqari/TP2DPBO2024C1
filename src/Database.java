import java.sql.*;

public class Database {
    private Connection connection;
    private Statement statement;

//    constructor
    public Database(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3305/db_mahasiswa","root","");
            statement =  connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    digunakan untuk SELECT
    public ResultSet selectQuery(String sql){
        try {
            statement.executeQuery(sql);
            return statement.getResultSet();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    digunakan untuk INSERT UPDATE DELETE
    public int modifyQuery(String sql){
        try {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    getter
    public Statement getStatement(){return statement;}

}
