package tudublin;

public class Module
{
    private int id;
    private String description;
    private int lecturerId;

    public Lecturer getLecturer()
    {
        Lecturer lecturer = null;
        LecturerRepository lecturerRepository = new LecturerRepository();

        try {
            lecturer = lecturerRepository.find(Lecturer.class, this.lecturerId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
    }
        return lecturer;
    }


    public int getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(int lecturerId) {
        this.lecturerId = lecturerId;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String toString()
    {
        return "MODULE: id = " + this.id + " / description = " + this.description
                + " / lecturer = " + this.getLecturer();
    }


}
