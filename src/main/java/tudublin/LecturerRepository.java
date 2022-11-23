package tudublin;

import mattsmithdev.pdocrudrepo.DatabaseTableRepository;

public class LecturerRepository extends DatabaseTableRepository
{
    public void createAndInsert(String name)
    {
        Lecturer lecturer = new Lecturer();
        lecturer.setName(name);
        this.insert(lecturer);
    }
}
