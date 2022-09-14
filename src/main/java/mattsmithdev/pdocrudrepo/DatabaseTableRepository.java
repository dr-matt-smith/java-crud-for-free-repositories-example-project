package mattsmithdev.pdocrudrepo;


import com.mysql.cj.jdbc.result.ResultSetImpl;
import tudublin.Module;

import java.lang.reflect.Array;
import java.sql.*;
import java.util.*;
import java.util.stream.*;
import java.lang.reflect.*;

/**
 * for the future
 * @TODO: SQLITE
 * https://www.sqlitetutorial.net/sqlite-java/create-table/
 *
 * @TODO: DB testing
 * https://blog.testproject.io/2021/03/08/jdbctemplate-for-java-automated-database-tests/
 *
 * @TODO: ConnectorJ version in POM needs to match MySQL server version
 * 8 or more usually ...
 *
 * @TODO: Spring framework for simpler DB
 * https://www.digitalocean.com/community/tutorials/spring-jdbctemplate-example
 */

public class DatabaseTableRepository
{
    /**
     * the (fully package namespaced) name of the class corresponding to the database table to be worked with
     * e.g. mycompany.Product
     */
    private String qualifiedClassName;

    /**
     * the Entity class name (no package/namespace)
     * e.g. Product
     */
    private String shortClassName;

    /**
     * the name of the database table to be worked with all lowercase)
     * e.g. product
     */
    private String tableName;

    /**
     * DatabaseTableRepository constructor.
     * param array params
     *
     * possible params:
     *      'namespace' e.g. 'MyNameSpace'
     *      'dbClass' e.g 'Movie'
     *      'tableName' e.g. 'movie'
     *
     * assumption:
     *      namespace of dbTable is same as namespace of repository class
     *      repository class is dbTable name with suffix 'Repository', e.g. Movie, MovieRepository
     *      table name is lower case version of class name, e.g. table 'movie' for class 'Movie'
     */
    public DatabaseTableRepository()
    {
        // e.g.
        // My\NameSpace\EntityRepository
        //
        // defaults are as follows:
        // namespace = My\NameSpace - entity class in same namespace as repository class
        // className = Entity - entity name is repository class less, less the word 'Repository'
        // tableName = entity - table name is same as entity class name, but all in loser case
        //
        // IF the above 3 defaults are true,
        // THEN the repository class does not need a constructor at all :-)

        // (1) create default values
        // namespace
        Class<?> clazz = this.getClass();

        // e.g. tudubln.ModuleRepository
        String qualifiedRepositoryClassName = clazz.getName();
        String suffix = "Repository";

        // e.g. tudublin.Module
        this.qualifiedClassName = qualifiedRepositoryClassName.substring(0, qualifiedRepositoryClassName.lastIndexOf(suffix));

        // e.g. ModuleRepository
        String shortRepositoryClassName = clazz.getSimpleName();
        // e.g. Module
        this.shortClassName = shortRepositoryClassName.substring(0, shortRepositoryClassName.lastIndexOf(suffix));

        // e.g. module
        this.tableName = this.shortClassName.toLowerCase();

    }




//    /**
//     * return name of entity class - current class name less "Repository"
//     * @param clazz
//     * @return
//     *
//     * e.g. ModuleRepository . Module
//     *
//     * throws Exception if class name does NOT end with Repository (and starts with at least one letter before Repository)
//     */
//    public String getShortClassName(Class<?> clazz ) throws Exception
//    {
//        String suffix = "Repository";
//        int suffixLength = suffix.length();
//
//        String className = clazz.getSimpleName();
//        int length = className.length();
//
//        if(length < (suffixLength+1)){
//            throw new Exception("class name must be longer than 'Repository'!");
//        }
//
//        if(!className.endsWith(suffix)){
//            throw new Exception("class name must end with 'Repository'");
//        }
//
//        return className.substring(0, className.lastIndexOf(suffix));
//    }


    public String getQualifiedClassName()
    {
        return qualifiedClassName;
    }

    public void setQualifiedClassName(String qualifiedClassName)
    {
        this.qualifiedClassName = qualifiedClassName;
    }

    public String getShortClassName()
    {
        return shortClassName;
    }

    public void setShortClassName(String shortClassName)
    {
        this.shortClassName = shortClassName;
    }

    /**
     * @return string
     */
    public String getTableName()
    {
        return this.tableName;
    }

    /**
     * param string tableName
     */
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }


    public Object[] findAll2()
    {
        DatabaseManager dataBaseManager = new DatabaseManager();
        Connection connection = dataBaseManager.getDbh();

        String sql = "SELECT * from :table";
        PreparedStatement statement;

        try {
            sql = sql.replace(":table", this.tableName);
            statement = connection.prepareStatement(sql);
//            statement.setString(1, this.tableName);
            statement.execute(sql);

            sql = statement.toString();
            ResultSet resultset = statement.executeQuery();

            // ?? success ?? what value of "i"

        } catch (Exception e) {
            System.out.println("Database error (trying to SELECT from table):: " + this.tableName + "\n" + e.getMessage());
            System.out.println("SQL = " + sql);
        }

        Object[] objects = new Object[1];
        objects[0] = new Object();

        return objects;
    }


    /**
     * https://stackoverflow.com/questions/2127318/java-how-can-i-do-dynamic-casting-of-a-variable-from-one-type-to-another
     *
     */
    private <T> T castObject(Class<T> clazz, Object object) {
        return (T) object;
    }

    /**
     *
     * @return
     */
    public <T> T[] entityObjects(Class<T> clazz, Object[] objects)
    {
        int size = objects.length;
        T[] entities = (T[])Array.newInstance(clazz, size);

        for(int i = 0; i < size; i++ ){
            entities[i] = (T)objects[i];
        }

        return entities;
    }

    public <T> T[] findAll(Class<T> clazz) throws Exception
    {


//        ArrayList<Object> objects = new ArrayList<Object>();

        Object[] objects = new Object[1000];
        DatabaseManager dataBaseManager = new DatabaseManager();
        Connection connection = dataBaseManager.getDbh();

        String sql = "SELECT * from :table";
        PreparedStatement statement;

        try {
            sql = sql.replace(":table", this.tableName);
            statement = connection.prepareStatement(sql);
//            statement.setString(1, this.tableName);
            statement.execute(sql);

//            System.out.println("SQL = " + sql);


            sql = statement.toString();
            ResultSet resultset = statement.executeQuery();
            //----- RS to objects ----
            ArrayList<T> objectArrayList = new ArrayList<T>();

            DatabaseUtility dbUtility = new DatabaseUtility();
            while(resultset.next()){
                T object = clazz.getDeclaredConstructor().newInstance();

                // "set" each field from RS
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    Object fieldType = field.getType();
                    String setterMethodName = dbUtility.setterMethodName(fieldName);
                    Method setterMethod;


//                    String mySQLtype = dbUtility.dbDataType(fieldType);

                    if(fieldType.equals(Double.TYPE))
                    {
                        double value = resultset.getDouble(fieldName);
                        setterMethod = clazz.getMethod(setterMethodName, double.class);
                        setterMethod.invoke(object, value);
                    }

                    if(fieldType.equals(Float.TYPE))
                    {
                        float value = resultset.getFloat(fieldName);
                        setterMethod = clazz.getMethod(setterMethodName, float.class);
                        setterMethod.invoke(object, value);
                    }

                    if(fieldType.equals(Boolean.TYPE))
                    {
                        int valueInt = resultset.getInt(fieldName);
                        boolean value = (valueInt == 1);
                        setterMethod = clazz.getMethod(setterMethodName, boolean.class);
                        setterMethod.invoke(object, value);
                    }

                    if(fieldType.equals(Integer.TYPE))
                    {
                        int value = resultset.getInt(fieldName);
                        setterMethod = clazz.getMethod(setterMethodName, int.class);
                        setterMethod.invoke(object, value);
                    }

                    if(fieldType.equals(String.class))
                    {
                        String value = resultset.getString(fieldName);
                        setterMethod = clazz.getMethod(setterMethodName, String.class);
                        setterMethod.invoke(object, value);
                    }


                }



                objectArrayList.add(object);
            }

            objects = objectArrayList.toArray();




        } catch (Exception e) {
            System.out.println("Database error (trying to SELECT from table):: " + this.tableName + "\n" + e.getMessage());
            System.out.println("SQL = " + sql);
        }


        // convert ArrayList to Array
//        return objects.toArray(new Object[0]);
        return entityObjects(clazz, objects);
    }

//
//    public Module[] resultSetToObjects(ResultSet rs, Class<T> clazz) throws Exception
//    {
//        Class<?> clazz2 = Class.forName(this.qualifiedClassName);
//
//        ArrayList<Module> objects = new ArrayList<Module>();
//
//
//        while(rs.next()){
//            Module module = new Module();
//            module.setId(rs.getInt("id"));
//            module.setDescription(rs.getString("description"));
//
//            objects.add(module);
//        }
//
//        return objects.toArray(new Module[0]);
//    }


    public void find(int id)
    {
        DatabaseManager dataBaseManager = new DatabaseManager();
        Connection connection = dataBaseManager.getDbh();

        try {
            String sql = "SELECT * from :table WHERE id=:id";
            sql = sql.replace(":table", this.tableName);
            sql = sql.replace(":id", ""+id);

            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, this.tableName);
//            statement.setInt(2, id);
            statement.execute(sql);
            ResultSet resultset = statement.executeQuery();

            // ?? success ?? what value of "i"

        } catch (Exception e) {
            System.out.println("Database error (trying to TRUNCATE table):: \n" + e.getMessage());

        }

    }


    /**
     * delete record for given ID - return true/false depending on delete success
     * @param id
     *
     * @return bool
     */

    public void delete(int id)
    {
        DatabaseManager dataBaseManager = new DatabaseManager();
        Connection connection = dataBaseManager.getDbh();

        try {
            String sql = "DELETE from :table WHERE id=:id";
            sql = sql.replace(":id", ""+id);
            sql = sql.replace(":table", this.tableName);
            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, this.tableName);
//            statement.setInt(2, id);
            statement.execute(sql);
            int i = statement.executeUpdate();

            // ?? success ?? what value of "i"

        } catch (Exception e) {
            System.out.println("Database error (trying to TRUNCATE table):: \n" + e.getMessage());

        }

    }



    /**
     * delete all records- return true/false depending on delete success
     *
     * @return bool
     */

    public void deleteAll()
    {
        DatabaseManager dataBaseManager = new DatabaseManager();
        Connection connection = dataBaseManager.getDbh();

        try {
            String sql = "TRUNCATE TABLE " + this.tableName;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.execute(sql);
            int i = statement.executeUpdate();

            // ?? success ?? what value of "i"

        } catch (Exception e) {
            System.out.println("Database error (trying to TRUNCATE table):: \n" + e.getMessage());

        }


    }


//    public function searchByColumn(columnName, searchText)
//    {
////        columnName = filter_var(columnName, FILTER_SANITIZE_STRING);
//
//        db = new DatabaseManager();
//        connection = db.getDbh();
//
//        // wrap wildcard '%' around the serach text for the SQL query
//        searchText = '%' . searchText . '%';
//
//        sql = 'SELECT * from :table WHERE :column LIKE :searchText';
//        sql = str_replace(':table', this.tableName, sql);
//        sql = str_replace(':column', columnName, sql);
//
//        statement = connection.prepare(sql);
////        statement.bindParam(':column', columnName, \PDO::PARAM_STR);
//        statement.bindParam(':searchText', searchText, \PDO::PARAM_STR);
//        statement.setFetchMode(\PDO::FETCH_CLASS, this.classNameForDbRecords);
//        statement.execute();
//
//        objects = statement.fetchAll();
//
//        return objects;
//    }


    /**
     * insert new record into the DB table
     * returns new record ID if insertion was successful, otherwise -1
     */
    public void insert(Object object)
    {
//        db = new DatabaseManager();
//        connection = db.getDbh();
//
//        objectAsArrayForSqlInsert = DatatbaseUtility::objectToArrayLessId(object);
//        fields = array_keys(objectAsArrayForSqlInsert);
//        insertFieldList = DatatbaseUtility::fieldListToInsertString(fields);
//        valuesFieldList = DatatbaseUtility::fieldListToValuesString(fields);
//
//        sql = 'INSERT into :table :insertFieldList :valuesFieldList';
//        sql = str_replace(':table', this.tableName, sql);
//        sql = str_replace(':insertFieldList', insertFieldList, sql);
//        sql = str_replace(':valuesFieldList', valuesFieldList, sql);
//
//        statement = connection.prepare(sql);
//        statement.execute(objectAsArrayForSqlInsert);
//        queryWasSuccessful = (statement.rowCount() > 0);
//        if(queryWasSuccessful) {
//            return connection.lastInsertId();
//        } else {
//            return -1;
//        }
    }

    /**
     * given an array of object, loop through them and insert them each into the DB table
     */
    public void insertMany(Object[] objects)
    {
        for(Object object: objects){
            this.insert(object);
        }
    }


    /**
     * insert new record into the DB table
     * returns new record ID if insertion was successful, otherwise -1
     *
     * @param object
     *
     * @return bool
     */
    public void update(Object object)
    {
//        int id = object.getId();
//
//        DatabaseManager dataBaseManager = new DatabaseManager();
//        Connection connection = dataBaseManager.getDbh();
//
//        objectAsArrayForSqlInsert = DatatbaseUtility::objectToArrayLessId(object);
//        fields = array_keys(objectAsArrayForSqlInsert);
//        updateFieldList = DatatbaseUtility::fieldListToUpdateString(fields);
//
//        String sql = "UPDATE :table SET :updateFieldList WHERE id=:id";
//        sql = sql.replace(":table", this.tableName);
//        sql = sql.replace(":updateFieldList", updateFieldList);
//
//
//        try {
//            statement = connection.createStatement();
//            statement.execute(sql);
//
//        } catch (Exception e) {
//            System.out.println("Database error:: \n" + e.getMessage());
//
//        }
//
//
//
//        statement = connection.prepare(sql);
//        Statement statement = null;
//
//        // add 'id' to parameters array
//        objectAsArrayForSqlInsert['id'] = id;
//
//        queryWasSuccessful = statement.execute(objectAsArrayForSqlInsert);



    }


    /**
     * drop the table associated with this repository
     *
     * @return bool
     */
    public void dropTable()
    {
        DatabaseManager dataBaseManager = new DatabaseManager();
        Connection connection = dataBaseManager.getDbh();

        try {
            String sql = "DROP TABLE IF EXISTS " + this.tableName;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.execute(sql);
            int i = statement.executeUpdate();

    // ?? success ?? what value of "i"

        } catch (Exception e) {
            System.out.println("Database error (trying to DROP table):: \n" + e.getMessage());

        }

    }

    /**
     * create the table associated with this repository
     *
     * SQL - optional SQL CREATE statement
     * DEFAULT: Look for a constant CREATE_TABLE_SQL defined in the entity class associated with this repository
     *
     * @return bool
     *
EXAMPLE OF SQL needed in Entity class:
 const CREATE_TABLE_SQL =
    <<<HERE
    CREATE TABLE IF NOT EXISTS movie (
    id integer PRIMARY KEY AUTO_INCREMENT,
    title text,
    price float,
    category text
    )
    HERE;

     */

    public void createTable() throws Exception
    {
        String sql = "";
            try {
                sql = this.getSqlToCreateTable();
            } catch (Exception e) {
                System.out.println("DatabaseTableRepository.createTable() :: Database error \n" + e.getMessage());
            }

        DatabaseManager dataBaseManager = new DatabaseManager();
        Connection connection = dataBaseManager.getDbh();

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.execute(sql);
            int i = statement.executeUpdate();

            // ?? success ?? what value of "i"
        } catch (Exception e) {
            System.out.println("*** sorry - a database error occurred ***\n");
            System.out.println("when trying to CREATE table:: \n" + e.getMessage());
            System.out.println("\n SQL = " + sql + "\n");

        }
    }

    public String getSqlToCreateTable() throws Exception
    {
        // try to infer from types of entity class properties
        String sql = this.inferSqlFromPropertyTypes();

        if(!sql.isEmpty()){
            return sql;
        }

        throw new Exception("cannot find or infer SQL to create table for class " + this.qualifiedClassName);
    }


    public void resetTable() throws Exception
    {

        this.dropTable();
        this.createTable();
        this.deleteAll();
    }

    /**
     * @return string
     * @throws \ReflectionException
     *
     * return SQL table creation string such as:
     *  CREATE TABLE IF NOT EXISTS movie (
     *      id integer PRIMARY KEY AUTO_INCREMENT,
     *      title text,
     *      price float,
     *      category text
     *  )
     */
    public String inferSqlFromPropertyTypes() throws Exception
    {
        LinkedHashMap<String, String> sqlTypesMap = new LinkedHashMap<>();

        DatabaseUtility dbUtility = new DatabaseUtility();
        String sql = "";

        Class<?> clazz = Class.forName(this.qualifiedClassName);
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            String propertyName = field.getName();
            Object propertyType = field.getType();

            String mySQLtype = dbUtility.dbDataType(propertyType);

            // if not 'id' add to map
            if("id" != propertyName){
                sqlTypesMap.put(propertyName, mySQLtype);
            }
        }

        sql = "CREATE TABLE IF NOT EXISTS "
            + this.tableName
            + " ("
            + "id integer PRIMARY KEY AUTO_INCREMENT, "
            + dbUtility.dbPropertyTypeList(sqlTypesMap)
            + ")";
        return sql;
    }

    public static LinkedHashMap<String, Object> objectToMapLessId(Object object)
    {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        List<?> list; // from your method
        for (Field field : fields) {
            String fieldName = field.getName();

            // ignore id field
            if(fieldName != "id"){
                try {
                    Method getter = clazz.getMethod(DatabaseUtility.getterName(fieldName));
                    Object fieldValue = getter.invoke(object);
                    map.put(fieldName, fieldValue);
                } catch (Exception e) {
                    System.out.println("exception occurred objectToArrayMapLessId");
                }
            }
        }


        return map;
    }


}
