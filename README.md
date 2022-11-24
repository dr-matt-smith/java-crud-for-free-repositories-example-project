# java-crud-for-free-repositories-example-project

Java/PHP - databasing your code

---------------------------------------------
(1) Download copy of starter-project from Github
---------------------------------------------

download an unZIP a copy of this started project from my public Github repositories

Java
	https://github.com/dr-matt-smith/java-crud-for-free-repositories-example-project

PHP
	https://github.com/dr-matt-smith/2022---php-db-create-read-sample-project	

---------------------------------------------
(2) Update ".env" settings for your MySQL setup
---------------------------------------------
edit the ".env" file to have your MySQL database credentials
you can also change the name of the database to be created freom "movies20" to something else


---------------------------------------------
(3) locate the main.php script / setup / do a test run of this sample
---------------------------------------------

Java
	/src/main/java/tudublin/

	you can compile your Jasva project with "composer build"
	you can run your Java project wiht "composer go"

PHP
	/db/main.php

	ONCE ONLY: run "composer update" to create a "vendor" folder with downloaded required libraries
	
	to run the main script type "php db/main.php"
	
	
you'll be editing this main class/script to create / store in DB / read and print your own objects ...

---------------------------------------------
(4) create "Tudublin" package/namespaced entity classes
---------------------------------------------

(a)
create / copy in your entity classes into the appropriate "src" folder

for Java:
	folder "/src/main/java/tudublin/" 
	package "tudublin"

for PHP: 
	folder "/src/" 
	namespace "Tudublin"

(b)
Note - my libraries don't yet work with enumerations
so just delete any enumeration classes, and change enumation types to strings for this database version of your code

---------------------------------------------
(5) create Repository classes for each of your entity classes
---------------------------------------------
the library works by providing a powerful superclass offering DB CRUD methods

for each entity class
- add appropriate 'import' or 'use' statement after the package/namespace statement
- for each entity create a class <Entity>Repository that subclasses (extends) the appropriat e


	Java
		import mattsmithdev\pdoCrudRepo\DatabaseTableRepository;	

		public class <MyEntity>Repository extends DatabaseTableRepository;	

	PHP
		use Mattsmithdev\PdoCrudRepo\DatabaseTableRepository;

		class <MyEntity>Repository extends DatabaseTableRepository;	

	
e.g. for the Movie entity class we create a MovieRepository class as follows:

Java
	packge Tudublin;

	import mattsmithdev.pdocrudrepo.DatabaseTableRepository;
	
	public class MovieRepository extends DatabaseTableRepository
	{
		// just inheritied methods for now
	}


PHP
	<?php
	namespace Tudublin;

	use Mattsmithdev\PdoCrudRepo\DatabaseTableRepository;

	class MovieRepository extends DatabaseTableRepository
	{
		// just inheritied methods for now
	}


---------------------------------------------
(6) add an "int ($)id" to every entity
---------------------------------------------
the db-crud library requires _every_ entity to have an integer "id" primary key property
this allows the MySQL database to manage AUTO INCREMENTED primary keys for each database table row

---------------------------------------------
(7) for associated object properties change them to objectId properties (for foreign keys)
---------------------------------------------
we have manually convert object associations into foreign key "id" properties

(--a--)
change object properties to "<object>Id" properties
	
e.g. If the Movie entity is associated to a Directory entity with a "director" property in Movie
we change this to "int ($)directorId"

(--b--)
add/generate get/set methods for the <objectId> properties
	
e.g. for our Movie.directorId/Movie->directorId example: 
	we add getDirectorId() and setDirectorId()
	

(--c--)
delete any old set<Entity>(...) method
	
e.g. for our Movie.directorId/Movie->directorId example: 
	we delete method setDirector(...)
	

(--d--)
replace the contents / create a getEntity() method for the associated object

we can use the related entity's Repository class, with the foreign key ID to retrieve the associated object

e.g. for our MMovie.directorId/Movie->directorId example: 
	we replace the oldl getDirector() method with the following,using the foreign key ID
	
	
Java
    public Director getDirector()
    {
        DirectorRepository directorRepository = new DirectorRepository();
        Director director = directorRepository.find(this.directorId);

        return director;
    }
	
	
PHP	
    public function getDirector(): Director
    {
        $directorRepository = new DirectorRepository();
        $director = $directorRepository->find($this->directorId);

        return $director;
    }

---------------------------------------------
(8) for associated object properties update your (__)toString()
---------------------------------------------	

update the (__)toString() method for classes with associated object to make use of this.get<Object>()/$this->get<Object>() method

e.g. for the toString method for class Movie, with the directorId: 

Java
	... other toString() stuff here ...
	" director = " + this.getDirector()

PHP
	... other __toString() stuff here ...
	" director = " . $this->getDirector()
	


---------------------------------------------
(9) create/update your Main.java/main.php to insert and then retrieve objects from the databasew
---------------------------------------------	

You can now create and use repository objects in your main class/script to store objects in the database and retrieve them

The general sequence is:

(--a--) create objects for each repositry class
(--b--) reset database tables with <entity>Repository.resetTable()/->resetTable()
(--c--) create all your objects for the entity classes
(--d--) insert the objects into the database with <entity>Repository.insert(<object>)/->insert(<object>)
(--e--) retreive an array of all objecss for _each_ entity with <arrayVariale> = <entity>Repository.findAll()/->insert(<object>)
(--f--) use a for/foreach-loop to loop to print out each object via its toString

Examples:

Java
    LecturerRepository lecturerRepository = new LecturerRepository();
    lecturerRepository.resetTable();

    Lecturer lecturer1 = new Lecturer();
    lecturer1.setName("matt");
    lecturerRepository.insert(lecturer1);

    Lecturer lecturer2 = new Lecturer();
    lecturer2.setName("jojo");
    lecturerRepository.insert(lecturer2);

    System.out.println("-- lecturers --");
    Lecturer[] lecturers = lecturerRepository.findAll(Lecturer.class);
    for (Lecturer lecturer : lecturers) {
        System.out.println(lecturer);
    }

PHP
	<?php
	require_once __DIR__ . '/../vendor/autoload.php';

	use Tudublin\DirectorRepository;
	use Tudublin\Director;

	$director1 = new Director();
	$director1->setName("Matt");
	$directorRepository->insert($director1);

	$director2 = new Director();
	$director2->setName("Jojo");
	$directorRepository->insert($director2);

	$directors = $directorRepository->findAll();
	foreach ($directors as $director) {
	    print $director;
	}





---------------------------------------------
(10) you can add a helper method "createAndInsert(...)" in your repository class
---------------------------------------------	

personally, I like to create and insert it into the database all in one go (a bit like a constructor)

so I add methods like this to my repository classes, which create an object, set the properties and insert the object into the database

Java
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
	

PHP
	<?php
	namespace Tudublin;

	use Mattsmithdev\PdoCrudRepo\DatabaseTableRepository;

	class DirectorRepository extends DatabaseTableRepository
	{
	    public function createAndInsert(string $name)
	    {
	        $d = new Director();
	        $d->setName($name);

	        $this->insert($d);
	    }
	}
	
So in our Main/main we can create and insert a new object using the repository object all in one statement:

Java
    lecturerRepository.createAndInsert("Jojo");	
	
PHP
	$directorRepository->createAndInsert("Jojo");
	
