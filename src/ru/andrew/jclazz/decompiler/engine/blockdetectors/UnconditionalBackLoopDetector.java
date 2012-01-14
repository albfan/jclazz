package ru.andrew.jclazz.decompiler.engine.blockdetectors;

import java.util.List;
import ru.andrew.jclazz.decompiler.engine.CodeItem;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.ops.GoToView;

public class UnconditionalBackLoopDetector implements Detector
{
    public void analyze(Block block)
    {
        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem citem = block.next();
            if (!(citem instanceof GoToView)) continue;

            GoToView gt = (GoToView) citem;
            if (gt.isBreak() || gt.isContinue() || gt.getLoop() != null) continue;
            if (!gt.isForwardBranch())
            {
                //if (isLastGoToWithSameTarget(block, gt))
                if (!isIfContinue(block, gt))
                {
                    createBackLoop(block, gt);
                }
                else
                {
                    gt.setContinue(true);
                }
            }
        }
    }

    private boolean isLastGoToWithSameTarget(Block block, GoToView gt)
    {
        List ops = block.getOperations();
        for (int i = 0; i < ops.size(); i++)
        {
            CodeItem citem = (CodeItem) ops.get(i);
            if (!(citem instanceof GoToView) || citem.getStartByte() <= gt.getStartByte()) continue;
            GoToView gt2 = (GoToView) citem;
            if (gt2.getTargetOperation() == gt.getTargetOperation())
            {
                return false;
            }
        }
        if (block.getParent() == null) return true;
        return isLastGoToWithSameTarget(block.getParent(), gt);
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

    private void createBackLoop(Block block, GoToView gotoView)
    {
        // Searching for outermost block for loop
        Block parent = block;
        Block foundBlock;
        CodeItem target;
        do
        {
            target = parent.getOperationByStartByte(gotoView.getTargetOperation());
            foundBlock = parent;
            parent = parent.getParent();
        }
        while (target == null && parent != null);

        if (parent == null && target == null)
        {
            if (block instanceof Catch)
            {
                foundBlock = block.getParent();
            }
            else
            {
                foundBlock = block;
            }
        }

        Loop loop = new Loop(foundBlock, false);
        foundBlock.createSubBlock(gotoView.getTargetOperation(), gotoView.getStartByte(), loop);
        loop.setAlwaysTrueCondition(true);
        block.removeOperation(gotoView.getStartByte());
    }
}
