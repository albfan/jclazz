package ru.andrew.jclazz.decompiler.engine.blockdetectors;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.engine.ops.*;

import java.util.*;

public class BackLoopDetector implements Detector
{
    public void analyze(Block block)
    {
        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem citem = block.next();
            if (!(citem instanceof IfView)) continue;

            IfView ifCond = (IfView) citem;
            if (!ifCond.isForwardBranch())
            {
                createBackLoop(block, ifCond);
            }
        }
    }

    private void createBackLoop(Block block, IfView ifCond)
    {
        CodeItem preLoop = block.getOperationPriorTo(ifCond.getTargetOperation());
        boolean printCondition = false;
        if (preLoop != null && preLoop instanceof GoToView &&
            ((GoToView) preLoop).isForwardBranch() &&
            ((GoToView) preLoop).getTargetOperation() < ifCond.getStartByte())
        {
            printCondition = true;
            block.removeOperation(preLoop.getStartByte());
            // TODO set label for conditions
        }

        Loop loop = new Loop(block, true);
        loop.setPrintPrecondition(printCondition);

        //try-catch inside loop with break in try block results in loop block inside catch
        boolean isLoopCreated = false;
        if (block instanceof Catch)
        {
            Try tryBlock = (Try) block.getParent().getOperationPriorTo(block.getStartByte());
            if (tryBlock.getStartByte() >= ifCond.getTargetOperation())
            {
                block.getParent().createSubBlock(ifCond.getTargetOperation(), ifCond.getStartByte(), loop);
                isLoopCreated = true;
            }
        }

        if (!isLoopCreated)
        {
            block.createSubBlock(ifCond.getTargetOperation(), ifCond.getStartByte(), loop);
        }

        List firstConditions = block.createSubBlock(ifCond.getStartByte(), ifCond.getStartByte() + 1, null);
        loop.addAndConditions(firstConditions); // TODO smth with multiple conditions
    }

    public Loop collapseBackLoops(Block block)
    {
        if (!(block instanceof Loop) || (block.size() > 2) || (block.size() == 0) || !(block.getFirstOperation() instanceof Loop)) return null;
        if ((block.size() == 2) && !(block.getLastOperation() instanceof NopView)) return null;

        Loop loop = ((Loop) block.getFirstOperation());
        if (!loop.isBackLoop()) return null;
        loop.addAndConditions2(((Loop) block).getConditions());
        loop.setParent(block.getParent());
        return loop;
    }
}
