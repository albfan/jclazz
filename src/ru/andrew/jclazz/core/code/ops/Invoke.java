package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.constants.*;

/**
 * Opcodes: 182 - 186<BR>
 * Parameters: ref_to_method(2), for opcode 185 additional parameters: count(1), 0<BR>
 * Operand stack: <BR>
 * invokestatic(184): [arg1, arg2, ...] => <BR> 
 * other: objectref, [arg1, arg2, ...] => <BR>
 */
public class Invoke extends PushOperation
{
    private String staticref;
    private String methodName;
    private MethodDescriptor md;

    private boolean isSuperMethodInvoke = false;

    public Invoke(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        if (opcode == 185)  // invokeinterface
        {
            CONSTANT_InterfaceMethodref mi_info = (CONSTANT_InterfaceMethodref) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
            md = mi_info.getMethodDescriptor();
            methodName = mi_info.getName();
        }
        else if (opcode == 186) // invokedynamic
        {
            CONSTANT_NameAndType nat_info = (CONSTANT_NameAndType) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
            methodName = nat_info.getName();
            try
            {
                md = new MethodDescriptor(nat_info.getDescriptor());
            }
            catch (ClazzException e)
            {
                throw new RuntimeException("Invoke: exception during initialization", e);
            }
        }
        else
        {
            CONSTANT_Methodref m_info = (CONSTANT_Methodref) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
            md = m_info.getMethodDescriptor();
            methodName = m_info.getName();

            if (opcode == 183)
            {
                String refClass = m_info.getRefClazz().getFullyQualifiedName();
                String thisClass = code.getClazz().getThisClassInfo().getFullyQualifiedName();
                if (!thisClass.equals(refClass))
                {
                    isSuperMethodInvoke = true;
                }
            }

            if (opcode == 184)  // invokestatic
            {
                staticref = m_info.getRefClazz().getFullyQualifiedName();
            }
        }
    }

    public MethodDescriptor getMethodDescriptor()
    {
        return md;
    }

    public String getReturnType()
    {
        return md.getReturnType().getFQN();
    }

    public String getMethodName()
    {
        return methodName;
    }

    public boolean isSuperMethodInvoke()
    {
        return isSuperMethodInvoke;
    }

    public String getClassForStaticInvoke()
    {
        return staticref;
    }

    public boolean isVoidReturned()
    {
        return "void".equals(getReturnType());
    }

    public String asString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(start_byte).append(" ").append(opcode.getMnemonic()).append(" ");
        if (opcode.getOpcode() == 184)  // invokestatic
        {
            sb.append(staticref).append(".");
        }
        sb.append(methodName);
        return sb.toString();
    }
}
