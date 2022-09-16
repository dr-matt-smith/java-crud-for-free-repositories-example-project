package tudublin;

public class App
{
    public static void main( String[] args )
    {
        try {
            ModuleRepository moduleRepository = new ModuleRepository();

            // (1) create table structure
//            moduleRepository.createTable();

            // (2) insert data into table

            Module m1 = new Module();
            m1.setDescription("interactive multimedia");
            moduleRepository.insert(m1);
            //- ----- TODO ---------
            //- ----- TODO ---------
            //- ----- TODO ---------
            //- ----- TODO ---------
            //- ----- TODO ---------

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
        } catch (Exception e) {
            System.out.println("main() :: Exception occurred!!!!!!!! " + e.getMessage());
        }

        System.out.println("finished!-------");
        System.exit(0);
    }
}


