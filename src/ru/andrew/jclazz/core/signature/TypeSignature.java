package ru.andrew.jclazz.core.signature;

/*
TypeSignature:
   FieldTypeSignature
   BaseType
 */
public class TypeSignature
{
    private FieldTypeSignature fieldType;
    private String baseType;

    private TypeSignature(FieldTypeSignature fieldType)
    {
        this.fieldType = fieldType;
    }

    private TypeSignature(String baseType)
    {
        this.baseType = baseType;
    }

    public static TypeSignature parse(StringBuffer sign)
    {
        // Loading with FieldTypeSignature
        if ((sign.charAt(0) == 'L') || (sign.charAt(0) == '[') || (sign.charAt(0) == 'T'))
        {
            return new TypeSignature(FieldTypeSignature.parse(sign));
        }

        // Loading with base type
        char bt = sign.charAt(0);
        sign.deleteCharAt(0);
        String base;
        switch (bt)
        {
            case 'B':
                base = "byte";
                break;
            case 'C':
                base = "char";
                break;
            case 'D':
                base = "double";
                break;
            case 'F':
                base = "float";
                break;
            case 'I':
                base = "int";
                break;
            case 'J':
                base = "long";
                break;
            case 'S':
                base = "short";
                break;
            case 'Z':
                base = "boolean";
                break;
            default:
                throw new RuntimeException("TypeSignature: unknown base type");
        }
        return new TypeSignature(base);
    }

    public FieldTypeSignature getFieldType()
    {
        return fieldType;
    }

    public String getBaseType()
    {
        return baseType;
    }

    
}
