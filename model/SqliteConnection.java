package pendu.model;
import java.sql.*;

/**
 * Created by Ibrahim on 21/04/2017.
 */
public class SqliteConnection {
    public static Connection Connector() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection =DriverManager.getConnection("jdbc:sqlite:Pendu.sqlite");
            return connection;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
