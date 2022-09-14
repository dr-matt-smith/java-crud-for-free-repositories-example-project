package tudublin;

import mattsmithdev.pdocrudrepo.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class App 
{

    public static void main( String[] args )
    {
//        setupTables();
        listModules();

        System.out.println("finished!-------");
        System.exit(0);
    }

    public static void setupTables()
    {
        try {
            System.out.println("---- MODULES -----");
            ModuleRepository moduleRepository = new ModuleRepository();
            moduleRepository.createTable();
        } catch (Exception e) {
            System.out.println("main() :: Exception! " + e.getMessage());
        }
    }


    public static void listModules() {
        try {
            System.out.println("---- MODULES -----");
            ModuleRepository moduleRepository = new ModuleRepository();
            Module[] modules = moduleRepository.findAll(Module.class);
            for (Module module : modules) {
                System.out.println(module);
            }

        } catch (Exception e) {
            System.out.println("main() :: Exception! " + e.getMessage());
        }
    }
}


