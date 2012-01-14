package ru.andrew.jclazz.decompiler.engine.blocks;

import java.util.*;
import java.util.ArrayList;
import ru.andrew.jclazz.decompiler.engine.ops.*;
import ru.andrew.jclazz.decompiler.engine.*;

public class Else extends Block
{
    public Else(Block parent)
    {
        super(parent);
    }

    public String getSource()
    {
        StringBuffer sb = new StringBuffer();
//        if ( ((size() == 1) && (getFirstOperation() instanceof IfBlock)) ||
//             ((size() == 2) && (getFirstOperation() instanceof IfBlock) && (getLastOperation() instanceof Else)) )
//        {
//            IfBlock ifb = (IfBlock) getFirstOperation();
//            ifb.setElseIf(true);
//            ifb.setIndent(getIndent());
//            sb.append(ifb.getSource());
//            if (size() == 2)
//            {
//                Else elb = (Else) getLastOperation();
//                elb.setIndent(getIndent());
//                sb.append(elb.getSource());
//            }
//        }
        if ( ((printedSize() == 1) && (getFirstPrintedOperation() instanceof IfBlock)) ||
             ((printedSize() == 2) && (getFirstPrintedOperation() instanceof IfBlock) && (getLastPrintedOperation() instanceof Else)) )
        {
            IfBlock ifb = (IfBlock) getFirstPrintedOperation();
            ifb.setElseIf(true);
            ifb.setIndent(getIndent());
            sb.append(ifb.getSource());
            if (printedSize() == 2)
            {
                Else elb = (Else) getLastPrintedOperation();
                elb.setIndent(getIndent());
                sb.append(elb.getSource());
            }
        }

        else
        {
            sb.append(indent).append("else").append(NL).append(super.getSource());
        }
        return sb.toString();
    }

    public void postCreate()
    {
        // Last "goto" in else block can be "break" or "continue" if enclosed in loop
        if (!(getLastOperation() instanceof GoToView)) return;

        long target_byte = ((GoToView) getLastOperation()).getTargetOperation();
        // Checking if last goto forwards to the begining of loop block, thus it is "continue"
        if (!((GoToView) getLastOperation()).isForwardBranch())
        {
            Block prevBlock = getParent();
            while (prevBlock != null && !(prevBlock instanceof Loop))
            {
                prevBlock = prevBlock.getParent();
            }
            if (prevBlock != null)
            {
                if (((Loop) prevBlock).getBeginPc() == target_byte)
                {
                    ((GoToView) getLastOperation()).setContinue(true);
                    return;
                }
            }
        }

        // Trying to find target operation for "break"
        Block prevBlock = getParent();
        while (prevBlock != null)
        {
            CodeItem ff_oper = prevBlock.getOperationByStartByte(target_byte);
            if (ff_oper != null)
            {
                CodeItem prev_ff = prevBlock.getOperationPriorTo(target_byte);
                if (prev_ff instanceof Loop) // Found target operation after loop
                {
                    ((GoToView) getLastOperation()).setBreak(true); // Set goto to break
                    return;
                }
            }
            prevBlock = prevBlock.getParent();
        }
    }

    private int stackSizeChange;

    public void preanalyze(Block block)
    {
        stackSizeChange = getMethodView().getMethodContext().stackSize();
    }

    public void postanalyze(Block block)
    {
        stackSizeChange = getMethodView().getMethodContext().stackSize() - stackSizeChange;

        if (stackSizeChange == 1)
        {
            // Convert to "condition ? op1 : op2" form
            CodeItem item = block.getOperationPriorTo(getStartByte());
            if (item instanceof IfBlock && ((IfBlock) item).isIsPushShortForm())
            {
                OperationView elsePush = context.pop();
                OperationView ifPush = context.pop();
                FakeConditionalPushView fakePush = new FakeConditionalPushView(getMethodView(), (IfBlock) item, ifPush, elsePush);

                // Placing Fake Push View instead of If and Else Blocks
                context.push(fakePush);
                block.removeOperation(item.getStartByte());
                block.removeOperation(this.getStartByte());
            }
        }
    }
}
