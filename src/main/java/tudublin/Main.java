package tudublin;

public class Main
{
    public static void main(String[] args) {
        Student[] students = null;

        // ** 1 ** create objects
        Course c1 = new Course();
        c1.setName("BSc Computing");
        c1.setCode("TU5775");
        c1.setSchool("Informatics & Cybersecurity");

        Course c2 = new Course();
        c2.setName("BSc Cybersecurity");
        c2.setCode("TU 799");
        c2.setSchool("Informatics & Cybersecurity");

        Student s1 = new Student();
        s1.setName("Matt Smith");
        s1.setAddress("10 High street, Blackrock");

        Student s2 = new Student();
        s2.setName("Freda Murphy");
        s2.setAddress("5 the long road, Galway");

        try {
            StudentRepository studentRepository = new StudentRepository();

            // ** 2 ** create Repository objects and reset DB tables
            studentRepository.resetTable();

            CourseRepository courseRepository = new CourseRepository();
            courseRepository.resetTable();

            // ** 3 ** insert objects into DB
            courseRepository.insert(c1);
            courseRepository.insert(c2);

            // (don't set an association, until the associated object has been inserted)
            s1.setCourse(c2);
            s2.setCourse(c1);

            // (don't insert an object until its associations have been set)
            studentRepository.insert(s1);
            studentRepository.insert(s2);


            students = studentRepository.findAll(Student.class);


        } catch (Exception e) {
            System.out.println("main() :: Exception occurred!!!!!!!! " + e.getMessage());
        }

        // ** 5 ** print out objects
        System.out.println("Student 1 ("
                + s1.getName()
                + ") is registered for course: " + s1.getCourse().getName());

        System.out.println(c1);
        System.out.println(c2);
        System.out.println(s1);

        System.out.println("-- students --");
        System.out.println("-- students --");
        System.out.println("-- students --");
        System.out.println("-- students --");

        for (Student student : students) {
            System.out.println(student);
        }

        System.exit(0);
    }
}
