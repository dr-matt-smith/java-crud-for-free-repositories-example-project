package mattsmithdev.pdocrudrepo;

import java.util.*;
import java.util.stream.*;
import java.lang.reflect.*;

public class DatabaseUtility
{

    /**
     * reflection
     * https://stackoverflow.com/questions/14374878/using-reflection-to-set-an-object-property
     *
     * setting object value with set<PropertyName>() method using reflection
     *
     * Class<?> classHandle = Class.forName(className);
     * Field field=classHandle.getDeclaredField("firstName");
     * Method setter=classHandle.getMethod("setFirstName", field.getType());
     * setter.invoke(myObject, "new value for first name");
     */

    /**
     * convert an Object into an associate array, and remove the first element (the 'id')
     * e.g. convert from this:
     *  Itb\Message Object
     *  (
     *      [id:Itb\Message:public] => (null or whatever)
     *      [text:Itb\Message:public] => hello there
     *      [user:Itb\Message:public] => matt
     *      [timestamp:Itb\Message:public] => 1456340266
     *  )
     *
     * to this:
     * Array
     * (
     *      [text] => hello there
     *      [user] => matt
     *      [timestamp] => 1456341484
     * )
     *
     * this is a convenient way to INSERT objects into autoincrement tables (so we don't want to pass the ID value - we want the DB to choose a new ID for us...)
     *
     * param $object
     *
     * return array
     */
//    public static Map<String, String> objectToArrayMapLessId(Object object)
    public static String objectToArrayMapLessId(Object object)
    {
        List<String> strings = new LinkedList<>();

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        List<?> list; // from your method
        for (Field field : fields) {
            String fieldName = field.getName();

//            Field field = clazz.getField("fieldName"); //Note, this can throw an exception if the field doesn't exist.

            try {
                Method getter = clazz.getMethod(getterName(fieldName));
                Object fieldValue = getter.invoke(object);


//                Object fieldValue = field.get(object);\
                if(fieldName != "id") {
                    strings.add(fieldName + " = " + fieldValue);
                }

            } catch (Exception e) {
                System.out.println("exception occurred objectToArrayMapLessId");
                strings.add("exception for field: " + field.getName() + "\n" + e.getMessage());
            }
        }


        String sql = String.join(", ", strings);
        return sql;
    }


    /**
     * given an object, return a hashmap of field name (String) and value for SQL insert (String)
     * (ignoring the 'id' property)
     *
     * e.g.
     * input:
     *  object Moodule with properties:
     *      id = 1
     *      price = 9.99
     *      description = "hammer"
     *
     *  outputs:
     *          "price" => "9.99",
     *          "description" => "'hammer'",
     */

    public static LinkedHashMap<String, String> objectToMapLess(Object object)
    {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        List<?> list; // from your method
        for (Field field : fields) {
            String fieldName = field.getName();

           try {
                Method getter = clazz.getMethod(getterName(fieldName));
                String fieldValue = getter.invoke(object)+"";
                if(field.getType() == String.class){
                    fieldValue = "'" + fieldValue + "'";
                }

                map.put(fieldName, fieldValue);
            } catch (Exception e) {
                System.out.println("exception occurred objectToArrayMapLessId");
            }
        }

        return map;
    }

    /**
     * given an object reference, invoke it's getId() method and return that integer
     */
    public static <T> int getId(T object)
    {
        int id = -1;
        try {
            Class<?> clazz = object.getClass();
            Method getter = clazz.getMethod("getId");
            id = (int)getter.invoke(object);

        } catch (Exception e) {
            System.out.println("DatabaseUtilities.getId() - error, can't invoke getId() on object");
            System.out.println(object);
            System.out.println(e.getMessage());
        }

        return id;
    }

    /**
     * given an object reference and an integer, invoke setId() with the integer
     */
    public static <T> boolean setId(T object, int id)
    {
        try {
            Class<?> clazz = object.getClass();
            Method setterMethod = clazz.getDeclaredMethod("setId", int.class);
            Object[] args = {id};
            setterMethod.invoke(object, args);

        } catch (Exception e) {
            System.out.println("DatabaseUtilit  ies.getId() - error, can't invoke getId() on object");
            System.out.println(object);
            System.out.println(e.getMessage());
        }

        return (id == DatabaseUtility.getId(object));
    }

    public static String hashMapValuesString(LinkedHashMap<String, String> objectAsHashMap)
    {
        // all values in hashmap into String List
        List<String> strings = new LinkedList<>(objectAsHashMap.values());

        String sql = String.join(", ", strings);
        return "VALUES (" + sql + ")";
    }

    /**
     * given a hashmap of field names (String) and values return field=value string
     * suitale for use in an SQL UPDATE statement
     *
     * e.g.
     * input:
     *  hashmap<String, String> =
     *          "price" => "9.99",
     *          "description" => "'hammer'"
     *
     *  outputs:
     *          price = 9.99, description = 'hammer'
     */
    public static String objectMapToUpdateString(LinkedHashMap<String, String> objectAsHashMap)
    {
        List<String> strings = new LinkedList<>();

        for (Map.Entry<String, String> entry : objectAsHashMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            strings.add(key + " = " + value);
        }

        String sql = String.join(", ", strings);
        return sql;
    }



    /**
     * given an object, return a hashmap of field name (String) and value for SQL insert (String)
     * (ignoring the 'id' property)
     *
     * e.g.
     * input:
     *  object Moodule with properties:
     *      id = 1
     *      price = 9.99
     *      description = "hammer"
     *
     *  outputs:
     *          "price" => "9.99",
     *          "description" => "'hammer'",
     */

    public static LinkedHashMap<String, String> objectToMapLessId(Object object)
    {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        List<?> list; // from your method
        for (Field field : fields) {
            String fieldName = field.getName();

            // ignore id field
            if(fieldName != "id"){
                try {
                    Method getter = clazz.getMethod(getterName(fieldName));
                    String fieldValue = getter.invoke(object)+"";
                    if(field.getType() == String.class){
                        fieldValue = "'" + fieldValue + "'";
                    }

                    map.put(fieldName, fieldValue);
                } catch (Exception e) {
                    System.out.println("exception occurred objectToArrayMapLessId");
                }
            }
        }

        return map;
    }

    public static String getterName(String field)
    {
        return "get" + firstLetterCapitalize(field);
    }

    public static String setterName(String field)
    {
        return "set" + firstLetterCapitalize(field);
    }

    public static String firstLetterCapitalize(String str)
    {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    /**
     * param String type
     * return String SQLDataType
     */
    public String dbDataType(Object javaType)
    {
        if(javaType.equals(Double.TYPE))
            return "float";

        if(javaType.equals(Float.TYPE))
            return "float";

        if(javaType.equals(Boolean.TYPE))
            return "tinyint";

        if(javaType.equals(Integer.TYPE))
            return "int";

        if(javaType.equals(String.class))
            return "text";

        return "text";

    }


    /**
     * given field name, return set method name
     *
     * e.g.
     * name -> getName
     * age -> getAge
     */
    public String setterMethodName(String fieldName)
    {
        return "set" + firstLetterCapitalize(fieldName);
    }


    /**
     * given LinkedhasMap of field names output comma separate list, with equals-colon syntax
     * e.g.
     * input:
     *          "title" => "String",
     *          "price" => "float",
     *          "category" => "String"
     *
     * output
     *       'title text, price float, category text'
     *
     * param array $fields
     * return string
     */
    public String dbPropertyTypeList(Map<String, String> sqlTypesMap)
    {
        List<String> strings = new LinkedList<>();

        for (Map.Entry<String, String> entry : sqlTypesMap.entrySet()) {
            String key = (String) entry.getKey();
            String type = (String) entry.getValue().toString();

            strings.add(key + " " + type);
        }

        String sql = String.join(", ", strings);
        return sql;
    }


    /**
     * given array of field names output comma separate list, with equals-colon syntax
     * e.g.
     * input:
     *      ['a', 'b']
     *
     * output
     *      'a = :a, b = :b'
     *
     * param array $fields
     * return string
     */
    public static String fieldListToUpdateString(String[] columns)
    {
        List<String> strings = new LinkedList<>();

        for(String column: columns){
            strings.add(column + " = :" + column);
        }

        String sql = String.join(", ", strings);
        return sql;
    }

    /**
     * given array of field names output parenthesied, comma separate list, with colon prefix
     * e.g.
     * input:
     *      ['one', 'two', 'three']
     *
     * output
     *      'values (:one, :two, :three)'
     *
     * param array $fields
     * return string
     */
    public static String fieldListToValuesString(String[] columns)
    {
        List<String> strings = new LinkedList<>();

        for(String column: columns){
            strings.add(":" + column);
        }

        String sql = String.join(", ", strings);
        return "values (" + sql + ")";
    }

    /**
     * given array of field names output parenthesied, comma separate list
     * e.g.
     * input:
     *      ['one', 'two', 'three']
     *
     * output
     *      '(one, two, three)'
     *
     * param array $fields
     * return string
     */
    public static String fieldListToInsertString(String[] columns)
    {
//        return ' (' . implode(', ', $fields) . ')';
//
//        List<String> strings = new LinkedList<>();
//
//        for(String column: columns){
//            strings.add(":" + column);
//        }

        String sql = String.join(", ", columns);
        return "(" + sql + ")";
    }

    public static String[] fieldNames(Field[] fields)
    {
        String[] fieldNames = new String[fields.length];
        for(int i = 0; i < fields.length; i++){
            fieldNames[i] = fields[i].getName();
        }

        return fieldNames;
    }

    public static String[] fieldNamesLessId(Field[] fields)
    {
        String[] fieldNames = new String[fields.length - 1];

        int i = 0;
        for(Field field: fields){
            String fieldName = field.getName();
            // don't add to array if current field is 'id'
            if(!fieldName.equals("id")){
                fieldNames[i] = fieldName;
                i++;
            }
        }

        return fieldNames;
    }

}
