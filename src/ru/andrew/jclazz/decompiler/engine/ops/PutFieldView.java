package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.decompiler.engine.CodeItem;

public class PutFieldView extends OperationView
{
    private String objectRef;
    private String value;

    private boolean isBoolean = false;

    private boolean isInInnerClass = false;
    private MethodSourceView msv;

    public PutFieldView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);

        this.msv = methodView;

        isInInnerClass = methodView.getClazzView().isInnerClass();

        isBoolean = "boolean".equalsIgnoreCase(((PutField) operation).getFieldDescriptor().getBaseType());
        if (getOpcode() == 179)   // putstatic
        {
            objectRef = ((PutField) operation).getClassForStaticField();
        }
    }

    public String source()
    {
        // Inner Class support
        /*
        FieldInfo fieldInfo = msv.getClazzView().getFieldByName(((PutField) operation).getFieldName());
        if (fieldInfo != null && fieldInfo.isSynthetic())
        {
            return null;
        }

        // final variables initialization in constructors should not be printed
        if ("this".equals(objectRef) && msv.getMethod().isInit())
        {
            FieldInfo field = msv.getClazzView().getFieldByName(((PutField) operation).getFieldName());
            if (field.isFinal() && field.getConstantValue() != null)
            {
                return null;
            }
        }

        StringBuffer sb = new StringBuffer();
        if (!("this".equals(objectRef) && ClazzSourceView.SUPPRESS_EXCESSED_THIS))
        {
            sb.append((getOpcode() == 179 ? alias(objectRef) : objectRef)).append(".");
        }
        if ("this".equals(objectRef) && ((PutField) operation).getFieldName().equals(value))
        {
            sb.append("this.");
        }
        sb.append(((PutField) operation).getFieldName()).append(" = ");
        if (isBoolean)
        {
            sb.append("0".equals(value) ? "false" : "true");
        }
        else
        {
            sb.append(value);
        }
        return sb.toString();
         */
         return null;
    }

    public String getPushType()
    {
        return null;
    }

    public String getFieldName()
    {
        return ((PutField) operation).getFieldName(); 
    }

    public void analyze(Block block)
    {
        /*
        OperationView pushOp = block.removePriorPushOperation();
        value = pushOp.source();

        if (getOpcode() == 181)  // putfield (non static)
        {
            OperationView refOp = block.removePriorPushOperation();
            if (refOp != null)
            {
                objectRef = refOp.source();
            }
            else
            {
                objectRef = "this";
            }
        }
         */
    }

    public void analyze2(Block block)
    {
        OperationView pushOp = context.pop();

        Object[] val = null;

        OperationView refOp = null;
        if (getOpcode() == 181)  // putfield (non static)
        {
            refOp = context.pop();
        }

        buildView(pushOp, refOp, val);
    }

    private void buildView(OperationView pushOp, OperationView refOp, Object[] val0)
    {
        // Inner Class support
        FieldInfo fieldInfo = msv.getClazzView().getFieldByName(((PutField) operation).getFieldName());
        if (fieldInfo != null && fieldInfo.isSynthetic())
        {
            return;
        }

        // final variables initialization in constructors should not be printed
        if (getOpcode() == 181 && msv.getMethod().isInit())
        {
            if (refOp == null || "this".equals(refOp.source3()))
            {
                FieldInfo field = msv.getClazzView().getFieldByName(((PutField) operation).getFieldName());
                if (field != null && field.isFinal() && field.getConstantValue() != null)
                {
                    return;
                }
            }
        }

        Object val = null;
        if (isBoolean)
        {
            val = "0".equals(pushOp.source3()) ? "false" : "true";
        }
        else
        {
            val = pushOp;
        }

        if (getOpcode() == 179)   // putstatic
        {
            String fieldName = ((PutField) operation).getFieldName();
            if (fieldName.startsWith("$SwitchMap$"))
            {
                fieldName = "switchMap";
                if (isInInnerClass)
                {
                    String clazzName = msv.getClazzView().getClazz().getThisClassInfo().getName();
                    clazzName = clazzName.substring(clazzName.lastIndexOf('$') + 1);
                }
            }
            if (val0 != null)
            {
                view = new Object[]{alias(objectRef), ".", fieldName + " = ", val0[0], " ? ", val0[1], " : ", val0[2]};
            }
            else
            {
                view = new Object[]{alias(objectRef), ".", fieldName + " = ", val};
            }
        }
        else
        {
            if ("this".equals(refOp.source3()) && ((val instanceof OperationView && !((OperationView) val).source3().equals(((PutField) operation).getFieldName())) || val instanceof String))
            {
                if (val0 != null)
                {
                    view = new Object[]{((PutField) operation).getFieldName() + " = ", val0[0], " ? ", val0[1], " : ", val0[2]};
                }
                else
                {
                    view = new Object[]{((PutField) operation).getFieldName() + " = ", val};
                }
            }
            else
            {
                if (val0 != null)
                {
                    view = new Object[]{refOp, ".", ((PutField) operation).getFieldName() + " = ", val0[0], " ? ", val0[1], " : ", val0[2]};
                }
                else
                {
                    view = new Object[]{refOp, ".", ((PutField) operation).getFieldName() + " = ", val};
                }
            }
        }
    }
}
