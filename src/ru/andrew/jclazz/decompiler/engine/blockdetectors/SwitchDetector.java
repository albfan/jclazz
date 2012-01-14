package ru.andrew.jclazz.decompiler.engine.blockdetectors;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.engine.ops.*;
import ru.andrew.jclazz.core.code.ops.*;

import java.util.*;

public class SwitchDetector implements Detector
{
    public void analyze(Block block)
    {
        if (block instanceof SwitchBlock) return;
        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem ci = block.next();
            if (ci instanceof SwitchView)
            {
                SwitchBlock switchBlock = new SwitchBlock(block);
                detectCaseBlocks((SwitchView) ci, switchBlock, block);
                block.replaceCurrentOperation(switchBlock);
            }
        }
    }

    private void detectCaseBlocks(SwitchView switchView, SwitchBlock switchBlock, Block block)
    {
        List cases = switchView.getCases();
        Collections.sort(cases, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                return (int) (((Switch.Case) o1).getOffset() - ((Switch.Case) o2).getOffset());
            }
        });

        long prev_offset = ((Switch.Case) cases.get(0)).getOffset();
        for (int i = 1; i < cases.size(); i++)
        {
            Switch.Case nextCase = (Switch.Case) cases.get(i);
            Block caseBlock = new Block(block, block.createSubBlock(prev_offset, nextCase.getOffset(), null));
            switchBlock.addCaseBlock((Switch.Case) cases.get(i - 1), caseBlock);
            prev_offset = nextCase.getOffset();
        }
        // Trying to find at least one case block with last GoTo operation
        long exitGoto = 0;
        Iterator it = switchBlock.getCaseBlocks().iterator();
        while (it.hasNext())
        {
            exitGoto = ((CaseBlock) it.next()).getLastGotoOffset();
            if (exitGoto != 0) break;
        }
        // If found creating block for last case block
        if (exitGoto != 0)
        {
            Block caseBlock = new Block(block, block.createSubBlock(prev_offset, exitGoto, null));
            switchBlock.addCaseBlock((Switch.Case) cases.get(cases.size() - 1), caseBlock);
        }
        else    // Last (usually default) block is print outside switch, so no need to create subblock
        {
            // do nothing
        }
    }
}
