package tudublin;

public class Main {
    static final String DIVIDER = " ------------- ";
    public static void main(String[] args) {
        try {
            // lecturers
            LecturerRepository lecturerRepository = new LecturerRepository();
            lecturerRepository.resetTable();

            Lecturer lecturer1 = new Lecturer();
            lecturer1.setName("matt");
            lecturerRepository.insert(lecturer1);

            lecturerRepository.createAndInsert("Jojo");

            System.out.println("-- lecturers --");
            Lecturer[] lecturers = lecturerRepository.findAll(Lecturer.class);
            for (Lecturer lecturer : lecturers) {
                System.out.println(lecturer);
            }

            // modules
            ModuleRepository moduleRepository = new ModuleRepository();
            moduleRepository.resetTable();

            Module m1 = new Module();
            m1.setDescription("year 2 - OOAD");
            m1.setLecturerId(1);
            moduleRepository.insert(m1);

            Module m2 = new Module();
            m2.setDescription("year 2 - GUI Programming");
            m2.setLecturerId(2);
            moduleRepository.insert(m2);

            System.out.println("-- modules --");
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
