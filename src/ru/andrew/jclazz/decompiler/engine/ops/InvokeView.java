package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.FieldDescriptor;
import ru.andrew.jclazz.core.attributes.InnerClass;
import ru.andrew.jclazz.core.code.ops.Invoke;
import ru.andrew.jclazz.core.code.ops.Operation;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.decompiler.engine.CodeItem;
import ru.andrew.jclazz.decompiler.engine.LocalVariable;
import ru.andrew.jclazz.decompiler.engine.blocks.Block;
import ru.andrew.jclazz.decompiler.engine.blocks.Condition;

import java.util.ArrayList;
import java.util.List;

public class InvokeView extends OperationView {
    private String objectref;
    private List pushedParams;

    private boolean isConstructor = false;
    private boolean isAnonymousConstructor = false;
    private InnerClass anonymousClass;
    private String anonymousIndent;

    // Inner Class support
    private boolean isICField = false;
    private String icFieldName;
    private int paramsAddition = 0;
    private boolean isICMethod = false;
    private String icMethodName;
    private InputStreamBuilder builder;

    public InvokeView(Operation operation, MethodSourceView methodView, InputStreamBuilder builder) {
        super(operation, methodView);
        this.builder = builder;
        pushedParams = new ArrayList();

        if (getOpcode() == 184)  // invokestatic
        {
            objectref = ((Invoke) operation).getClassForStaticInvoke();

            // Inner Class support
            if (methodView.getClazzView().isInnerClass() && ((Invoke) operation).getMethodName().startsWith("access$")) {
                MethodSourceView m_ic = methodView.getClazzView().getOuterClazz().getSyntheticMethodForIC(((Invoke) operation).getMethodName());
                if (m_ic != null && m_ic.getMethodNameForIC() != null) {
                    icMethodName = m_ic.getMethodNameForIC();
                    isICMethod = true;
                } else if (objectref.equals(methodView.getClazzView().getOuterClazz().getClazz().getThisClassInfo().getFullyQualifiedName())) {
                    if (m_ic != null && m_ic.isForIC()) {
                        icFieldName = m_ic.getFieldNameForIC();
                        isICField = true;
                    }
                }
            }
        }
    }

    public String source() {
        /*
        if (isICField) return icFieldName;

//        if (ref != null)
//        {
//            return ref.getOperation();
//        }
//
        StringBuffer sb = new StringBuffer();
        boolean isInit = false;
        if ("<init>".equals(((Invoke) operation).getMethodName()))
        {
            isInit = true;
        }
        String anonymousClassAsString = "";
        if (isConstructor)
        {
            // Anonymous Class support ^
            if (isAnonymousConstructor)
            {
                String inname = anonymousClass.getInnerClass().getName();
                String enclosingClassFileName = methodView.getClazz().getFileName();
                String path = enclosingClassFileName.substring(0, enclosingClassFileName.lastIndexOf(System.getProperty("file.separator")) + 1);
                Clazz innerClazz;
                try
                {
                    innerClazz = new Clazz(path + inname + ".class");
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
                AnonymousClazzSourceView csv = new AnonymousClazzSourceView(innerClazz, methodView.getClazzView());
                csv.setPrintAsAnonymous(true);
                //csv.setIndent(anonymousIndent);
                csv.setIndent("    ");
                InnerClassView icv = methodView.getClazzView().getInnerClassView(innerClazz.getThisClassInfo().getFullyQualifiedName());
                icv.setClazzView(csv);
                anonymousClassAsString = csv.getSource();

                objectref = alias(csv.getAnonymousSuperClassFQN());
            }
            // Anonymous Class support v

            sb.append("new ").append(objectref);
        }
        else
        {
            if (((Invoke) operation).isSuperMethodInvoke())
            {
                if (isInit)
                {
                    // Do not print invokation of default constructor
                    if (((Invoke) operation).getMethodDescriptor().getParams().size() > 0)
                    {
                        sb.append("super");
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {
                    sb.append("super.").append(((Invoke) operation).getMethodName());
                }
            }
            else
            {
                if (isInit)
                {
                    sb.append("this");
                }
                else
                {
                    if (!("this".equals(objectref) && ClazzSourceView.SUPPRESS_EXCESSED_THIS))
                    {
                        if (getOpcode() == 184)
                        {
                            if (!isICMethod)
                            {
                                sb.append(alias(objectref)).append(".");
                            }
                        }
                        else
                        {
                            sb.append(objectref).append(".");
                        }
                    }
                    // Inner class support method invokation
                    if (isICMethod)
                    {
                        sb.append(icMethodName);
                    }
                    else
                    {
                        sb.append(((Invoke) operation).getMethodName());
                    }
                }
            }
        }
        sb.append("(");

        if (((Invoke) operation).getMethodDescriptor().getParams().size() > 0)
        {
            if ((pushedParams == null) || (pushedParams.size() + paramsAddition != ((Invoke) operation).getMethodDescriptor().getParams().size()))
            {
                throw new OperationException("Invoke: invalid parameters");
            }
            boolean firstParam = true;
            for (int i = pushedParams.size() - 1; i >= 0; i--)
            {
                // Inner class method invokation support
                if (i == pushedParams.size() - 1 && isICMethod) continue;

                if (!firstParam) 
                {
                    sb.append(", ");
                }
                OperationView pushOp = (OperationView) pushedParams.get(i);

                FieldDescriptor argDescriptor = (FieldDescriptor) ((Invoke) operation).getMethodDescriptor().getParams().get(pushedParams.size() + paramsAddition - i - 1);
                if ("boolean".equals(argDescriptor.getBaseType()))
                {
                    if ("1".equals(pushOp.source()))
                    {
                        sb.append("true");
                    }
                    else
                    {
                        sb.append("false");
                    }
                }
                else
                {
                    // Narrowing Primitive Conversions
                    if (argDescriptor.isBaseType())
                    {
                        if (Block.widePrimitiveConversion(argDescriptor.getBaseType(), pushOp.getPushType()))
                        {
                            sb.append("(").append(argDescriptor.getBaseType()).append(") ");
                        }
                    }
                    sb.append(pushOp.source());
                }
                firstParam = false;
            }
        }

        sb.append(")");

        if (isAnonymousConstructor)
        {
            sb.append(anonymousClassAsString);
        }

        return sb.toString();
         */
        return null;
    }

    public boolean isPush() {
        return !"void".equals(((Invoke) operation).getMethodDescriptor().getReturnType().getBaseType()) || isConstructor;
    }

    public boolean isPrintable() {
        return !isPush();
    }

    public String getPushType() {
        if ("<init>".equals(((Invoke) operation).getMethodName())) {
            return objectref;
        }
        return ((Invoke) operation).getReturnType();
    }

    public void analyze(Block block) {
        /*
        List params = ((Invoke) operation).getMethodDescriptor().getParams();
        if (params != null && params.size() > 0)
        {
            for (int ip = 0; ip < params.size(); ip++)
            {
                OperationView pushOp = block.removePriorPushOperation();
                if (pushOp == null)
                {
                    throw new RuntimeException("Not enough pushs for invoke operation");
                }
                pushedParams.add(pushOp);
            }
        }

        if (getOpcode() != 184)  // NOT invokestatic
        {                                                                       
            OperationView refOp = block.removePriorPushOperation();
            if ((refOp instanceof NewView) && ("<init>".equals(((Invoke) operation).getMethodName())))    // new + init
            {
                isConstructor = true;

                // Inner Class support
                if (((NewView) refOp).isICConstructor() && pushedParams.size() > 0)
                {
                    paramsAddition = 1;
                    pushedParams.remove(pushedParams.size() - 1);
                }
                if (((NewView) refOp).isACConstructor())
                {
                    isAnonymousConstructor = true;
                    anonymousClass = ((NewView) refOp).getAnonymousClass();
                    anonymousIndent = block.getIndent();
                }
            }
            int refOpOpcode = refOp.getOpcode();
            if (refOp instanceof PushVariableView)
            {
                // TODO
                objectref = ((PushVariableView) refOp).source();
                //objectref = ((PushVariableView) refOp).getLocalVariable().getName((int) getStartByte());
            }
            else
            {
                objectref = refOp.source();
                // Case If with assignment
                if (block instanceof Condition &&
                        block.getOperationAfter(refOp.getStartByte()) instanceof PopView &&
                        block.getLastOperation() instanceof IfView)
                {
                    PopView popView = (PopView) block.getOperationAfter(refOp.getStartByte());
                    LocalVariable popedLV = block.getLocalVariable(popView.getLocalVariableNumber(), null);
                    //popedLV.setPrinted(true);
                    objectref = "(" + popView.source() + ")";
                    //popedLV.setPrinted(false);
                    FakePopView fakePop = new FakePopView(methodView, popedLV, "null");
                    // insert fake pop before loop
                    block.getParent().getParent().addOperation(fakePop, block.getParent().getStartByte());
                }

                if (refOp instanceof CheckCastView)
                {
                    objectref = "(" + objectref + ")";
                }
            }
            if ((((Invoke) operation).isSuperMethodInvoke()) && "this".equals(objectref))
            {
                objectref = "super";
            }
        }
         * */
    }

    public void analyze2(Block block) {
        List params = ((Invoke) operation).getMethodDescriptor().getParams();
        if (params != null && params.size() > 0) {
            for (int ip = 0; ip < params.size(); ip++) {
                OperationView pushOp = context.pop();
                if (pushOp == null) {
                    throw new RuntimeException("Not enough pushs for invoke operation");
                }
                pushedParams.add(pushOp);
            }
        }

        OperationView refOp = null;
        if (getOpcode() != 184)  // NOT invokestatic
        {
            refOp = context.pop();
            if ((refOp instanceof NewView) && ("<init>".equals(((Invoke) operation).getMethodName())))    // new + init
            {
                context.pop(); // remove 'new' operation from stack
                isConstructor = true;

                // Inner Class support
                if (((NewView) refOp).isICConstructor() && pushedParams.size() > 0) {
                    paramsAddition = 1;
                    pushedParams.remove(pushedParams.size() - 1);
                }
                if (((NewView) refOp).isACConstructor()) {
                    isAnonymousConstructor = true;
                    anonymousClass = ((NewView) refOp).getAnonymousClass();
                    anonymousIndent = block.getIndent();
                }
            }
            if (refOp instanceof PushVariableView) {
                // TODO
                //objectref = ((PushVariableView) refOp).source();
                //objectref = ((PushVariableView) refOp).getLocalVariable().getName((int) getStartByte());
            } else {
                objectref = refOp.source3();
                // Case If with assignment
                CodeItem nextDup = block.getOperationAfter(refOp.getStartByte());
                if (block instanceof Condition &&
                        nextDup instanceof DupView &&
                        block.getLastOperation() instanceof IfView) {
                    CodeItem nextPop = block.getOperationAfter(nextDup.getStartByte());
                    if (nextPop instanceof PopView) {
                        PopView popView = (PopView) nextPop;
                        LocalVariable popedLV = block.getLocalVariable(popView.getLocalVariableNumber(), null, (int) getStartByte());
                        //popedLV.setPrinted(true);
                        refOp = popView;
                        objectref = "(" + popView.source3() + ")";
                        //popedLV.setPrinted(false);
                        FakePopView fakePop = new FakePopView(methodView, popedLV, "null");
                        // insert fake pop before loop
                        block.getParent().getParent().addOperation(fakePop, block.getParent().getStartByte());
                    }
                }

                if (refOp instanceof CheckCastView) {
                    objectref = "(" + objectref + ")";
                }
            }
            if ((((Invoke) operation).isSuperMethodInvoke()) && "this".equals(objectref)) {
                objectref = "super";
            }
        }
        buildView(refOp, pushedParams);
    }

    private void buildView(OperationView opRef, List params) {
        List items = new ArrayList();

        if (opRef instanceof PopView) {
            items.add("(");
        }

        if (isICField) {
            view = new Object[]{icFieldName};
            context.push(this);
            return;
        }

//        if (ref != null)
//        {
//            return ref.getOperation();
//        }
//
        boolean isInit = false;
        if ("<init>".equals(((Invoke) operation).getMethodName())) {
            isInit = true;
        }
        String anonymousClassAsString = "";
        AnonymousClazzSourceView csv = null;
        if (isConstructor) {
            items.add("new ");
            // Anonymous Class support ^
            if (isAnonymousConstructor) {
                String inname = anonymousClass.getInnerClass().getName();
                String enclosingClassFileName = methodView.getClazz().getFileName();
                String path = enclosingClassFileName.substring(0, enclosingClassFileName.lastIndexOf(System.getProperty("file.separator")) + 1);
                Clazz innerClazz;
                try {
                    String innerClassName = path + inname + ".class";
                    innerClazz = new Clazz(innerClassName);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                csv = new AnonymousClazzSourceView(innerClazz, methodView.getClazzView());
                csv.setPrintAsAnonymous(true);
                //csv.setIndent(anonymousIndent);
                csv.setIndent("    ");
                InnerClassView icv = methodView.getClazzView().getInnerClassView(innerClazz.getThisClassInfo().getFullyQualifiedName());
                icv.setClazzView(csv);

                objectref = alias(csv.getAnonymousSuperClassFQN());

                items.add(objectref);
            }
            // Anonymous Class support v
            else {
                items.add(opRef);
            }
        } else {
            if (((Invoke) operation).isSuperMethodInvoke()) {
                if (isInit) {
                    // Do not print invokation of default constructor
                    if (((Invoke) operation).getMethodDescriptor().getParams().size() > 0) {
                        items.add("super");
                    } else {
                        view = null;
                        return;
                    }
                } else {
                    items.add("super." + ((Invoke) operation).getMethodName());
                }
            } else {
                if (isInit) {
                    items.add("this");
                } else {
                    if (!("this".equals(objectref) && ClazzSourceView.SUPPRESS_EXCESSED_THIS)) {
                        if (getOpcode() == 184) // invokestatic
                        {
                            if (!isICMethod) {
                                items.add(alias(objectref));
                                items.add(".");
                                //sb.append(alias(objectref)).append(".");
                            }
                        } else {
                            if (!"this".equals(opRef.source3())) {
                                items.add(opRef);
                                if (opRef instanceof PopView) {
                                    items.add(")");
                                }
                                items.add(".");
                            }
                        }
                    }
                    // Inner class support method invokation
                    if (isICMethod) {
                        items.add(icMethodName);
                    } else {
                        items.add(((Invoke) operation).getMethodName());
                    }
                }
            }
        }
        items.add("(");

        if (params.size() > 0) {
            if ((params == null) || (params.size() + paramsAddition != ((Invoke) operation).getMethodDescriptor().getParams().size())) {
                throw new OperationException("Invoke: invalid parameters");
            }
            boolean firstParam = true;
            for (int i = params.size() - 1; i >= 0; i--) {
                // Inner class method invokation support
                if (i == params.size() - 1 && isICMethod) continue;

                OperationView pushOp = (OperationView) params.get(i);

                if (isAnonymousConstructor && csv != null) {
                    int icParamCnt = csv.getInParamsCount();
                    if (pushOp instanceof PushVariableView) {
                        LocalVariable lv = ((PushVariableView) pushOp).getLocalVariable();
                        lv.forceFinal();
                        ((AnonymousClazzSourceView) csv).putOuterMapping(params.size() - 1 - i, lv.getName());
                    }
                    if (i >= icParamCnt) continue;
                }

                if (!firstParam) {
                    items.add(", ");
                }

                FieldDescriptor argDescriptor = (FieldDescriptor) ((Invoke) operation).getMethodDescriptor().getParams().get(params.size() + paramsAddition - i - 1);
                if ("boolean".equals(argDescriptor.getBaseType())) {
                    if ("1".equals(pushOp.source3())) {
                        items.add("true");
                    } else {
                        items.add("false");
                    }
                } else {
                    // Narrowing Primitive Conversions
                    if (argDescriptor.isBaseType()) {
                        if (Block.widePrimitiveConversion(argDescriptor.getBaseType(), pushOp.getPushType())) {
                            items.add("(" + argDescriptor.getBaseType() + ") ");
                        }
                    }
                    items.add(pushOp);
                }
                firstParam = false;
            }
        }

        items.add(")");

        if (isAnonymousConstructor) {
            anonymousClassAsString = csv.getSource();
            items.add(anonymousClassAsString);
        }

        view = new Object[items.size()];
        view = items.toArray(view);

        if (!"void".equals(((Invoke) operation).getMethodDescriptor().getReturnType().getBaseType()) || isConstructor) {
            context.push(this);
        }
    }

}
