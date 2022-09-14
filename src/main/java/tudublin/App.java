package tudublin;

public class App
{
    public static void main( String[] args )
    {
        try {
            ModuleRepository moduleRepository = new ModuleRepository();

            // (1) create table structure
            moduleRepository.createTable();

            // (2) insert data into table

            //- ----- TODO ---------
            //- ----- TODO ---------
            //- ----- TODO ---------
            //- ----- TODO ---------
            //- ----- TODO ---------

            // (3) query and loop through array of modules from DB table
            System.out.println("---- MODULES -----");
            Module[] modules = moduleRepository.findAll(Module.class);
            for (Module module : modules) {
                System.out.println(module);
            }
        } catch (Exception e) {
            System.out.println("main() :: Exception occurred!!!!!!!! " + e.getMessage());
        }

        System.out.println("finished!-------");
        System.exit(0);
    }
}


