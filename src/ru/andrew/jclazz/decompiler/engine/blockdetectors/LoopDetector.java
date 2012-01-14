package ru.andrew.jclazz.decompiler.engine.blockdetectors;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.engine.ops.*;

import java.util.*;

public class LoopDetector implements Detector
{
    public void analyze(Block block)
    {
        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem citem = block.next();
            if (!(citem instanceof IfView)) continue;
            IfView ifCond = (IfView) citem;

            CodeItem priorTarget = block.getOperationPriorTo(ifCond.getTargetOperation());

            if (priorTarget != null && priorTarget instanceof IfView)
            {
                priorTarget = block.getOperationPriorTo(((IfView) priorTarget).getTargetOperation());
            }

            // Inner loop
            if (priorTarget == null) priorTarget = block.getLastOperation();   // TODO better detection of target operation

            if ((priorTarget == null) || (!(priorTarget instanceof GoToView)))
            {
                continue;
            }

            GoToView priorTargetGoTo = (GoToView) priorTarget;

            if (!priorTargetGoTo.isForwardBranch() && (priorTargetGoTo.getTargetOperation() <= ifCond.getStartByte()) && (priorTargetGoTo.getLoop() != null || !isIfContinue(block, priorTargetGoTo)))
            {
                createForwardLoop(block, ifCond);
                continue;
            }
        }
    }

    private boolean isIfContinue(Block block, GoToView priorTarget)
    {
        // priorTarget - backward goto, but this is not loop
        // Case: if - continue for while (...) {...} block
        Block loop = block;
        while (loop != null)
        {
            if ((loop instanceof Loop) && (((Loop) loop).getBeginPc() == priorTarget.getTargetOperation()))
            {
                // This is "if - continue" case
                return true;
            }
            loop = loop.getParent();
        }

        return false;
    }

    private boolean isCompoundForwardLoop(Block block, IfView ifCond)
    {
        if (ifCond.getTargetOperation() <= block.getLastOperation().getStartByte())
        {
            return false;
        }
        if (!(block instanceof Loop))
        {
            return false;
        }

        CodeItem next = block.getParent().getOperationAfter(block.getStartByte());
        return next != null && next.getStartByte() == ifCond.getTargetOperation();
    }

    private void createForwardLoop(Block block, IfView firstIf)
    {
        CodeItem firstPriorOp = block.getOperationPriorTo(firstIf.getTargetOperation());
        IfView ifCond = firstPriorOp instanceof IfView ? (IfView) firstPriorOp : firstIf;

        if (isCompoundForwardLoop(block, ifCond))
        {
            List conditions = block.createSubBlock(0, ifCond.getStartByte() + 1, null);
            ((Loop) block).addAndConditions(conditions);
            return;
        }

        if (firstPriorOp instanceof GoToView && block.getOperationByStartByte(((GoToView) firstPriorOp).getTargetOperation()) == null) return;

        Loop loop = new Loop(block, false);
        block.createSubBlock(ifCond.getStartByte() + 1, ifCond.getTargetOperation(), loop);
        //List firstConditions = block.createSubBlock(firstIf.getStartByte(), ifCond.getStartByte() + 1, null);
        List firstConditions = block.createSubBlock(loop.getBeginPc(), ifCond.getStartByte() + 1, null);
        loop.addAndConditions(firstConditions);
    }
}
