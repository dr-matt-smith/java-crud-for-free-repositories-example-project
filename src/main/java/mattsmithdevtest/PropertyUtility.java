package mattsmithdevtest;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class PropertyUtility
{
    public String visibility;
    public String name;
    public String type;

    public PropertyUtility(Field f) {
        this.visibility = this.fieldVisibilityAsString(f);
        this.name = f.getName();
        this.type = this.fieldTypeAsString(f);
    }

    public PropertyUtility(String visibility, String type, String name) {
        this.visibility = visibility;
        this.name = name;
        this.type = type;
    }
    @Override
    public boolean equals(Object object)
    {
        PropertyUtility f2 = (PropertyUtility)object;
        String name2 = f2.name;

        String type2 = f2.type;

        String visibility2 = f2.visibility;

        return (this.name.equals(name2) && this.type.equals(type2) && this.visibility.equals(visibility2));
    }

    private String fieldTypeAsString(Field f)
    {
        Object javaType = f.getType();
        if(javaType.equals(Double.TYPE))
            return "double";

        if(javaType.equals(Float.TYPE))
            return "float";

        if(javaType.equals(Boolean.TYPE))
            return "boolean";

        if(javaType.equals(Integer.TYPE))
            return "int";

        if(javaType.equals(String.class))
            return "String";

        return "unknown-type";
    }

    private String fieldVisibilityAsString(Field f)
    {
        if(Modifier.isPublic(f.getModifiers())){
            return "public";
        }

        if(Modifier.isPrivate(f.getModifiers())){
            return "private";
        }

        if(Modifier.isProtected(f.getModifiers())){
            return "protected";
        }

        return "not-public-private-protected";
    }

    /**
     * current FieldUtility is present within provided Field[] array
     */
    public boolean inArray(Field[] fields)
    {
        for (Field field : fields) {
            PropertyUtility currentField = new PropertyUtility(field);

            if(currentField.equals(this)){
                return true;
            }
        }

        return false;
    }
}
