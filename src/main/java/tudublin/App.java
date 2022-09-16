package tudublin;

import jdk.jpackage.internal.MacDmgBundler;

public class App
{
    public static void main( String[] args )
    {
        try {
            ModuleRepository moduleRepository = new ModuleRepository();
            moduleRepository.setSilent();

            // (1) create table structure
            moduleRepository.resetTable();

            // (2) insert data into table

            Module m1 = new Module();
            m1.setDescription("interactive year 3 AI");
            moduleRepository.insert(m1);

            Module m2 = new Module();
            m2.setDescription("OOP");

            Module m3 = new Module();
            m3.setDescription("GUI programming");

            Module[] modulesForInsert = new Module[2];
            modulesForInsert[0] = m2;
            modulesForInsert[1] = m3;
            moduleRepository.insertMany(modulesForInsert);

            // demo update
            m3.setDescription("GUI programming - version 2");
            moduleRepository.update(m3);

            // (3) query modules in DB table
            System.out.println("---- MODULES -----");
            Module[] modules = moduleRepository.findAll(Module.class);

            // (4) print contents of 'modules' array
            if(modules.length < 1){
                System.out.println("-- empty recordset -- no Module receords found in database");
            } else {
                for (Module module : modules) {
                    System.out.println(module);
                }
            }

            // test find(id)
            System.out.println("--- find object with id = 2---");
            Module moduleWithId3 = moduleRepository.find(Module.class, 2);
            System.out.println(moduleWithId3);

        } catch (Exception e) {
            System.out.println("main() :: Exception occurred!!!!!!!! " + e.getMessage());
        }

        System.out.println("finished!-------");
        System.exit(0);
    }
}


