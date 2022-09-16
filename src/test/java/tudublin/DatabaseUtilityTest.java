package tudublin;

import mattsmithdev.pdocrudrepo.DatabaseUtility;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseUtilityTest
{
    @Test
    public void javaIntBecomesMysqlInt()
    {
        // Arrange
        DatabaseUtility databaseUtility = new DatabaseUtility();
        Object type1 = Integer.TYPE;
        String expectedResult = "int";
        // Act
        String dataType1 = databaseUtility.dbDataType(type1);

        // Assert
        assertEquals(expectedResult, dataType1);
    }

    @Test
    public void columnTypeCommaSeparatedString()
    {
        // Arrange
        DatabaseUtility databaseUtility = new DatabaseUtility();
        Map<String, String> sqlTypesMap = new LinkedHashMap<String, String>();
        sqlTypesMap.put("title", "text");
        sqlTypesMap.put("price", "float");
        sqlTypesMap.put("category", "text");
        String expectedResult = "title text, price float, category text";

        // Act
        String columnsTypesString = databaseUtility.dbPropertyTypeList(sqlTypesMap);

        // Assert
        assertEquals(expectedResult, columnsTypesString);
    }

    @Test
    public void columnNamesEqualsColonSyntax()
    {
        // Arrange
        String[] columns = {"title", "price", "category"};
        String expectedResult = "title = :title, price = :price, category = :category";

        // Act
        String columnsSqlString = DatabaseUtility.fieldListToUpdateString(columns);

        // Assert
        assertEquals(expectedResult, columnsSqlString);
    }

    @Test
    public void columnNamesValueColonSyntax()
    {
        // Arrange
        String[] columns = {"title", "price", "category"};
        String expectedResult = "values (:title, :price, :category)";

        // Act
        String columnsSqlString = DatabaseUtility.fieldListToValuesString(columns);

        // Assert
        assertEquals(expectedResult, columnsSqlString);
    }

    @Test
    public void columnNamesParethesisSyntax()
    {
        // Arrange
        String[] columns = {"title", "price", "category"};
        String expectedResult = "(title, price, category)";

        // Act
        String columnsSqlString = DatabaseUtility.fieldListToInsertString(columns);

        // Assert
        assertEquals(expectedResult, columnsSqlString);
    }





    @Test
    public void objectToPropertyString()
    {
        // Arrange
        Product product = new Product();
        product.setId(1);
        product.setDescription("fred");
        product.setPrice(9.99);
        String expectedResult = "description = fred, price = 9.99";

        // Act
        String productAsString = DatabaseUtility.objectToArrayMapLessId(product);

        // Assert
        assertEquals(expectedResult, productAsString);
    }


    @Test
    public void objectToPropertyMap()
    {
        // Arrange
        Product product = new Product();
        product.setId(1);
        product.setDescription("fred");
        product.setPrice(9.99);

        LinkedHashMap<String, String> mapExpected = new LinkedHashMap<>();
        mapExpected.put("id", "1");
        mapExpected.put("description", "'fred'");
        mapExpected.put("price", "9.99");

        // Act
        LinkedHashMap<String, String> productAsMapLessId = DatabaseUtility.objectToMapLess(product);

        // Assert
        assertEquals(mapExpected, productAsMapLessId);
    }

    @Test
    public void objectToPropertyMapLessId()
    {
        // Arrange
        Product product = new Product();
        product.setId(1);
        product.setDescription("fred");
        product.setPrice(9.99);

        LinkedHashMap<String, String> mapExpected = new LinkedHashMap<>();
        mapExpected.put("description", "'fred'");
        mapExpected.put("price", "9.99");

        // Act
        LinkedHashMap<String, String> productAsMapLessId = DatabaseUtility.objectToMapLessId(product);

        // Assert
        assertEquals(mapExpected, productAsMapLessId);
    }

}
