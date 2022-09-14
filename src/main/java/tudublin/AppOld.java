package tudublin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AppOld
{

    public static void main( String[] args )
    {
//        setupTables();

        test1();


        System.out.println("finished!-------");
        System.exit(0);
    }

    public static void setupTables()
    {
        try {
            System.out.println("---- MODULES -----");
            ModuleRepository moduleRepository = new ModuleRepository();
            moduleRepository.createTable();
            Module[] modules = moduleRepository.findAll(Module.class);
            for(Module module: modules){
                System.out.println(module);
            }



        } catch (Exception e) {
            System.out.println("main() :: Exception! " + e.getMessage());
        }
    }


    public static void test1()
    {
        try {
            System.out.println("---- MODULES -----");
            ModuleRepository moduleRepository = new ModuleRepository();
            Module[] modules = moduleRepository.findAll(Module.class);
            for(Module module: modules){
                System.out.println(module);
            }

//            System.out.println("---- PRODUCTS -----");
//            ProductRepository productRepository = new ProductRepository();
//            Product[] products = productRepository.findAll(Product.class);
//            for(Product product: products){
//                System.out.println(product);
//            }


        } catch (Exception e) {
            System.out.println("main() :: Exception! " + e.getMessage());
        }

//        ProductRepository2 productRepository = new ProductRepository2();
//        System.out.println(productRepository);
//        productRepository.fred();

//
//        System.out.println( "Hello World!" );

//        DatabaseManager dataBaseManager = new DatabaseManager();
//        Connection conn = dataBaseManager.getDbh();
//        testDbConnection(conn);
//


//        Object objects[] = moduleRepository.findAll();
//        System.out.println(objects[0]);


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

            System.out.println("-1- before RS loop ----");


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
            System.out.println("-2- after RS loop ----");

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


