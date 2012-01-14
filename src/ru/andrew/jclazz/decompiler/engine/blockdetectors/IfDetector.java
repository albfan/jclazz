package ru.andrew.jclazz.decompiler.engine.blockdetectors;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.engine.ops.*;

import java.util.*;

public class IfDetector implements Detector
{
    public void analyze(Block block)
    {
        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem citem = block.next();
            if (!(citem instanceof IfView)) continue;
            IfView ifCond = (IfView) citem;

            if (!ifCond.isForwardBranch()) continue;

            CodeItem priorTarget = block.getOperationPriorTo(ifCond.getTargetOperation());
            CodeItem firstPriorTarget = priorTarget;

            if (priorTarget != null && priorTarget instanceof IfView)
            {
                priorTarget = block.getOperationPriorTo(((IfView) priorTarget).getTargetOperation());
            }
            if ((priorTarget == null) || (!(priorTarget instanceof GoToView)))
            {
                createIf(block, ifCond, firstPriorTarget);
                continue;
            }

            GoToView priorTargetGoTo = (GoToView) priorTarget;

            if (priorTargetGoTo.isForwardBranch())
            {
                createIf(block, ifCond, firstPriorTarget);
                continue;
            }
            else if (isIfContinue(block, priorTargetGoTo))
            {
                createIfContinue(block, ifCond, firstPriorTarget);
                continue;
            }
        }
    }

    private void createIf(Block block, IfView firstIf, CodeItem firstPriorTarget)
    {
        IfView ifCond = firstPriorTarget instanceof IfView ? (IfView) firstPriorTarget : firstIf;
        CodeItem priorOp = firstPriorTarget instanceof IfView ? block.getOperationPriorTo(((IfView) firstPriorTarget).getTargetOperation()) : firstPriorTarget;

        if (isCompoundIf(block, ifCond))
        {
            List compoundConditions = block.createSubBlock(0, ifCond.getStartByte() + 1, null);
            ((IfBlock) block).addAndConditions(compoundConditions);
            return;
        }

        IfBlock ifBlock = new IfBlock(block);
        block.createSubBlock(ifCond.getStartByte() + 1, ifCond.getTargetOperation(), ifBlock);
        List firstConditions = block.createSubBlock(firstIf.getStartByte(), ifCond.getStartByte() + 1, null);
        ifBlock.addAndConditions(firstConditions);
        
        if (priorOp != null && priorOp instanceof GoToView && ((GoToView) priorOp).isForwardBranch())
        {
            detectElse(block, ifBlock, ifCond, (GoToView) priorOp);
        }
    }

    private boolean isCompoundIf(Block block, IfView ifCond)
    {
        if (!(block instanceof IfBlock))
        {
            return false;
        }
        if (ifCond.getTargetOperation() <= block.getLastOperation().getStartByte())
        {
            return false;
        }

        // If-continue case
        if (block.getLastOperation() instanceof GoToView && ((GoToView) block.getLastOperation()).isContinue())
        {
            return true;
        }

        CodeItem next = block.getParent().getOperationAfter(block.getStartByte());
        return (next instanceof Else) && (next.getStartByte() == ifCond.getTargetOperation());
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

    private void createIfContinue(Block block, IfView firstIf, CodeItem firstPriorTarget)
    {
        IfView ifCond = firstPriorTarget instanceof IfView ? (IfView) firstPriorTarget : firstIf;
        GoToView priorTarget = (GoToView) (firstPriorTarget instanceof IfView ? block.getOperationPriorTo(((IfView) firstPriorTarget).getTargetOperation()) : firstPriorTarget);

        if (isCompoundIf(block, ifCond))
        {
            List compoundConditions = block.createSubBlock(0, ifCond.getStartByte() + 1, null);
            ((IfBlock) block).addAndConditions(compoundConditions);
            return;
        }

        priorTarget.setContinue(true);

        IfBlock ifBlock = new IfBlock(block);
        block.createSubBlock(ifCond.getStartByte() + 1, ifCond.getTargetOperation(), ifBlock);
                                                                                                      
        List firstConditions = block.createSubBlock(firstIf.getStartByte(), ifCond.getStartByte() + 1, null);
        ifBlock.addAndConditions(firstConditions);
    }

    private void detectElse(Block block, IfBlock ifBlock, IfView ifCond, GoToView ifPriorTarget)
    {
        // Generic Else block
        if (block.getLastOperation().getStartByte() >= ifPriorTarget.getTargetOperation())
        {
            Else elseBlock = new Else(block);
            block.createSubBlock(ifCond.getTargetOperation(), ifPriorTarget.getTargetOperation(), elseBlock);
            ifBlock.setElseBlock(elseBlock);
            elseBlock.postCreate();
            return;
        }

        long ifPriorTarget_pc = ifPriorTarget.getTargetOperation();

        // Trying to find target operation
        Block ff_block = block;
        CodeItem ff_oper;
        do
        {
            ff_oper = ff_block.getOperationByStartByte(ifPriorTarget_pc);
            if (ff_oper != null) break;
            ff_block = ff_block.getParent();
        }
        while (ff_block != null);
        if (ff_oper == null) return;

        CodeItem prevFFOper = ff_block.getOperationPriorTo(ifPriorTarget_pc);
        if (prevFFOper instanceof Loop)
        {
            ifPriorTarget.setBreak(true); // Set goto to break
            return;
        }
        else if (prevFFOper instanceof Else || prevFFOper instanceof IfBlock)   // Else block
        {
            Else elseBlock = new Else(block);
            block.createSubBlock(ifCond.getTargetOperation(), ifPriorTarget_pc, elseBlock);
            ifBlock.setElseBlock(elseBlock);
            elseBlock.postCreate();
            return;
        }
        else if (prevFFOper instanceof SwitchBlock)
        {
            Else elseBlock = new Else(block);
            block.createSubBlock(ifCond.getTargetOperation(), ifPriorTarget_pc, elseBlock);
            ifBlock.setElseBlock(elseBlock);
            elseBlock.postCreate();
            return;
        }

        if (ff_block instanceof Loop)    // this is continue in for loop
        {
            if (!((Loop) ff_block).isBackLoop())
            {
                ((Loop) ff_block).setIncrementalPartStartOperation(ff_oper.getStartByte());
            }
            ifPriorTarget.setContinue(true);
            return;
        }

        // Add return in previous "if" if adding new "if" 
    }
}
