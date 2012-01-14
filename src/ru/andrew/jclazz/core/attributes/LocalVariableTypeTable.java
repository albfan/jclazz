package ru.andrew.jclazz.core.attributes;

import ru.andrew.jclazz.core.io.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.constants.*;

import java.io.*;

public class LocalVariableTypeTable extends AttributeInfo
{
    class LocalSignedVariable
    {
        int start_pc;
        int length;
        CONSTANT_Utf8 name;
        CONSTANT_Utf8 signature;
        int index;
    }

    private LocalSignedVariable[] local_variable_type_table;

    public LocalVariableTypeTable(CONSTANT_Utf8 attributeName, Clazz clazz)
    {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException
    {
        attributeLength = (int) cis.readU4();

        int local_variable_table_type_length = cis.readU2();
        local_variable_type_table = new LocalSignedVariable[local_variable_table_type_length];
        for (int i = 0; i < local_variable_table_type_length; i++)
        {
            local_variable_type_table[i] = new LocalSignedVariable();
            local_variable_type_table[i].start_pc = cis.readU2();
            local_variable_type_table[i].length = cis.readU2();
            int name_index = cis.readU2();
            local_variable_type_table[i].name = (CONSTANT_Utf8) clazz.getConstant_pool()[name_index];
            int signature_index = cis.readU2();
            local_variable_type_table[i].signature = (CONSTANT_Utf8) clazz.getConstant_pool()[signature_index];
            local_variable_type_table[i].index = cis.readU2();
        }
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU4(attributeLength);
        cos.writeU2(local_variable_type_table.length);
        for (int i = 0; i < local_variable_type_table.length; i++)
        {
            cos.writeU2(local_variable_type_table[i].start_pc);
            cos.writeU2(local_variable_type_table[i].length);
            cos.writeU2(local_variable_type_table[i].name.getIndex());
            cos.writeU2(local_variable_type_table[i].signature.getIndex());
            cos.writeU2(local_variable_type_table[i].index);
        }
    }

    public LocalSignedVariable[] getLocalSignedVariablesTable()
    {
        return local_variable_type_table;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append("LocalVariableTypeTable: \n");
        for (int i = 0; i < local_variable_type_table.length; i++)
        {
            LocalSignedVariable lsv = local_variable_type_table[i];
            sb.append(INDENT).append(lsv.start_pc).append("+").append(lsv.length).append(": ");
            sb.append(lsv.name.getString()).append(" (sign=").append(lsv.signature.getString()).append("), ").append(lsv.index);
            if (i < local_variable_type_table.length - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
