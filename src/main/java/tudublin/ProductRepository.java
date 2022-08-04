package tudublin;
import java.lang.reflect.*;
import java.util.*;

public class ProductRepository
{
    public void fred()
    {

        try {
            Class c = Class.forName("com.mycompany.app.ProductRepository");
            System.out.println(c.getPackage());
            Method m[] = c.getDeclaredMethods();
            for (int i = 0; i < m.length; i++)
                System.out.println(m[i].toString());
        }
        catch (Throwable e) {
            System.err.println(e);
        }

        Product p = new Product();
        Field[] fields = p.getClass().getDeclaredFields();

        System.out.println("---- fields ------");
        String[] fieldNamesArray = getFieldNames(fields);
        for (int i = 0; i < fieldNamesArray.length; i++)
            System.out.println(fieldNamesArray[i]);

        System.out.println("------");

    }

    private static String[] getFieldNames(Field[] fields)
    {
        String[] names = new String[fields.length];
        List<String> fieldNames = new ArrayList<>();
        int i = 0;
        for (Field field : fields){
            names[i] = "name = " + field.getName()
                    + ", type = " + field.getType();
            i++;
        }
        return names;
    }

    public Product[] findAll()
    {
        Product[] products = {
                new Product()
        };
        return products;
    }
}
