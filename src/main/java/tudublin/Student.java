package tudublin;

public class Student
{
    private int id;
    private String name;
    private String address;

    private int courseId;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                ", address='" + this.address + '\'' +
                "\n\t " + this.getCourse()
                + '}';
    }

    public void setCourse(Course course){
        this.courseId = course.getId();
    }

    public Course getCourse()
    {
        CourseRepository courseRepository = new CourseRepository();
        Course course = courseRepository.find(Course.class, this.courseId);

        return course;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
