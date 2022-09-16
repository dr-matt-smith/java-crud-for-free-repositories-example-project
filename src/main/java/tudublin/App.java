package tudublin;

import jdk.jpackage.internal.MacDmgBundler;

public class App {
    static final String DIVIDER = " ------------- ";
    public static void main(String[] args) {
        try {
            ModuleRepository moduleRepository = new ModuleRepository();
            moduleRepository.setSilent();

            // (1) create/reset schema and table structure, using repo.resetTable()
            moduleRepository.resetTable();
            printAllDatabaseModules( "(1) after table reset (so should be no modules...)");

            // (2) insert data into table, using repo.insert(<object>)
            Module m1 = new Module();
            m1.setDescription("year 2 - OOAD");
            moduleRepository.insert(m1);

            Module m2 = new Module();
            m2.setDescription("year 2 - GUI Programming");
            moduleRepository.insert(m2);

            printAllDatabaseModules("(2) after 2 modules inserted (OOAD and GUI)");

            // (3) demo array of objects to be inserted into DB, with repo.insertMany(<arrayOfObjects>)
            Module[] modulesForInsert = new Module[2];
            Module m3 = new Module();
            m3.setDescription("year 3 - Web Framework Development");
            Module m4 = new Module();
            m4.setDescription("Year 4 - Derivation of Algorithms");
            modulesForInsert[0] = m3;
            modulesForInsert[1] = m4;

            moduleRepository.insertMany(modulesForInsert);
            printAllDatabaseModules( "(3) after 2 more modules inserted via array (WebF and DerOfAlg)");

            // (4) demo update, with repo.update(<object>)
            m1.setDescription("OOAD - new and improved for 2022!");
            moduleRepository.update(m1);
            printAllDatabaseModules( "(4) module 1 (OOAD) with improved description");

            // (5) query modules in DB table
            System.out.println("---- MODULES -----");
            Module[] modulesFound = moduleRepository.findAll(Module.class);
            int moduleCount = modulesFound.length;
            printMessage("(5) I found " + moduleCount + " modules with repo.findAll(<entityClass>)");

            // (6) find a particular module by ID, with repo.find(<id>)
            Module moduleWithId3 = moduleRepository.find(Module.class, 2);
            printMessage("(6) this module found with moduleRepository.find(Module.class, 2)");
            System.out.println(moduleWithId3);

        } catch (Exception e) {
            System.out.println("main() :: Exception occurred!!!!!!!! " + e.getMessage());
        }

        System.out.println("finished!-------");
        System.exit(0);
    }

    public static void printAllDatabaseModules(String message) {
        ModuleRepository moduleRepository = new ModuleRepository();
        try {
            Module[] modules = moduleRepository.findAll(Module.class);

            printMessage(message);
            if (modules.length < 1) {
                System.out.println("-- empty recordset -- no Module records found in database");
            } else {
                for (Module module : modules) {
                    System.out.println(module);
                }
            }
        } catch (Exception e) {
            System.out.println("an exception occurred");
        }
    }

    public static void printMessage(String message) {
        System.out.println(DIVIDER + message +  DIVIDER);
    }
}