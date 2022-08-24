package tudublin;

import mattsmithdev.pdocrudrepo.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class App 
{

    public static void main( String[] args )
    {
        ProductRepository productRepository = new ProductRepository();
        System.out.println(productRepository);
        productRepository.fred();


        System.out.println( "Hello World!" );

        DatabaseManager dataBaseManager = new DatabaseManager();
        Connection conn = dataBaseManager.getDbh();
        testDbConnection(conn);

        System.out.println("finished!-------");
        System.exit(0);
    }


    /**
     new form Java
     */

    public static void testDbConnection(Connection conn)
    {
//
//        Connection conn = null;
//        try {
//            conn =
//                    DriverManager.getConnection("jdbc:mysql://" + MYSQL_HOST + ":" + MYSQL_PORT + "/" + MYSQL_DATABASE + "?" +
//                            "user=" + MYSQL_USER + "&password=" + MYSQL_PASSWORD);
//
//            // Do something with the Connection
//
//        } catch (SQLException ex) {
//            // handle any errors
//            System.out.println("SQLException: " + ex.getMessage());
//            System.out.println("SQLState: " + ex.getSQLState());
//            System.out.println("VendorError: " + ex.getErrorCode());
//        }
//
//
//// assume that conn is an already created JDBC connection (see previous examples)

        Statement statement = null;
        ResultSet resultset = null;

        try {
            String sql = "SELECT * from product";
            statement = conn.createStatement();
            resultset = statement.executeQuery(sql);

//            // or alternatively, if you don't know ahead of time that
//            // the query will be a SELECT...
//
//            if (statement.execute("SELECT foo FROM bar")) {
//                rs = statement.getResultSet();
//            }

            // Now do something with the ResultSet ....
            while (resultset.next()) {

                Product product = new Product();

                int id = resultset.getInt("id");
                product.setId(id);
                String description = resultset.getString("description");
                product.setDescription(description);

                Double price = resultset.getDouble("id");
                product.setPrice(price);
                // etc.

                System.out.println(product);
            }
        }
        catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (resultset != null) {
                try {
                    resultset.close();
                } catch (SQLException sqlEx) { } // ignore

                resultset = null;
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) { } // ignore

                statement = null;
            }

        }

    }

}


