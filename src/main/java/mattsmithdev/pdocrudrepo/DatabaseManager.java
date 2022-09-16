package mattsmithdev.pdocrudrepo;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;


public class DatabaseManager
{
    static final int SQL_EXCEPTION_CODE_DATABASE_EXISTS = 1007;

    private String host;
    private String port;
    private String user;
    private String pass;
    private String dbname;
    private Connection dbh;
    private String error;

    public DatabaseManager(boolean silent)
    {
        this.loadCredentialsFromDotEnv();

        boolean success = false;
        Connection conn = null;
        Statement statement = null;
        ResultSet resultset = null;

//        try {
//
//            conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/?" +
//                    "user=" + this.user + "&password=" + this.pass);
//
//            // truy to create schema
//            statement = conn.createStatement();
//            int Result= statement.executeUpdate("CREATE DATABASE " + this.dbname);
//
//        } catch (SQLException ex) {
//            // handle any errors
//            System.out.println("SQLException: " + ex.getMessage());
//            System.out.println("SQLState: " + ex.getSQLState());
//            System.out.println("VendorError: " + ex.getErrorCode());
//        }


        // -- first -- create DB connection with no DB selected, and try to create it --
        try {
            // DSN - the Data Source Name - requred by the PDO to connect
            String dsn = "jdbc:mysql://" + this.host + ":" + this.port + "/?" +
                    "user=" + this.user + "&password=" + this.pass;
            conn = DriverManager.getConnection(dsn);
            
            String sql = "CREATE DATABASE " + this.dbname;
            statement = conn.createStatement();
            statement.executeUpdate(sql);



        } catch (SQLException e) {
            if(this.databaseExistsSQLException(e)){
                // database already exists - all good
                if(!silent){
                    System.out.println("(DatabaseManager) using database '" + this.dbname + "'");
                }
                success = true;

            } else {
                // some other error
                this.error = e.getMessage();
                System.out.println("sorry - a database error occurred - please contact the site administrator ...");
                System.out.println("<br>");
                System.out.println(e.getMessage());
            }
        }

        if(!success){
            System.out.println("database '" + this.dbname + "' did not exist, so new schema created \n");
        }

        // -- second -- now DB exists should be able to connect to it
        try {
            // DSN - the Data Source Name - requred by the PDO to connect
            String dsn = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.dbname + "?" +
                    "user=" + this.user + "&password=" + this.pass;
            this.dbh = DriverManager.getConnection(dsn);
            
        } catch (SQLException e) {
            this.error = e.getMessage();
            System.out.println("sorry - a database error occured - please contact the site administrator ...");
            System.out.println("<br>");
            System.out.println(e.getMessage());
        }
    }



    private boolean databaseExistsSQLException(SQLException e)
    {
        int errorCode = e.getErrorCode();
        return (SQL_EXCEPTION_CODE_DATABASE_EXISTS == errorCode);
    }

    private void loadCredentialsFromDotEnv()
    {
        Dotenv dotenv = Dotenv.load();
        this.user = dotenv.get("MYSQL_USER");
        this.pass = dotenv.get("MYSQL_PASSWORD");
        this.host = dotenv.get("MYSQL_HOST");
        this.port = dotenv.get("MYSQL_PORT");
        this.dbname = dotenv.get("MYSQL_DATABASE");

//        if(!this.host){
//            throw new Exception("\n\n ********** missing MYSQL_HOST environment value - or perhaps missing .env file altogether ...\n\n");
//        }

        
    }

    public Connection getDbh()
    {
        return this.dbh;
    }

    public String getError()
    {
        return this.error;
    }

}
