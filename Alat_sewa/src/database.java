import java.sql.Connection;
import java.sql.SQLException;
import com.mysql.cj.jdbc.MysqlDataSource;

public class database {
    public void connect(){
        MysqlDataSource datasource = new MysqlDataSource();
        String url = "jdbc:mysql://localhost:3306/databasetugasbesar";
        String usernamedb = "root";
        String passworddb = "";

        datasource.setURL(url);
        datasource.setUser(usernamedb);
        datasource.setPassword(passworddb);

        try {
            Connection conn = datasource.getConnection();
            System.out.println("Berhasil");
            conn.close();
        } catch (SQLException ex) {
            System.out.println("tidak berhasil" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        database kon = new database();
        kon.connect();
    }
}
