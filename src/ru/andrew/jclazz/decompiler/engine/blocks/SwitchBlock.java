package ru.andrew.jclazz.decompiler.engine.blocks;

import ru.andrew.jclazz.decompiler.engine.ops.*;
import ru.andrew.jclazz.core.code.ops.*;

import java.util.*;

public class SwitchBlock extends Block
{
    private OperationView switchVar;
    private ArrayList caseBlocks = new ArrayList();

    public SwitchBlock(Block parent)
    {
        super(parent, new ArrayList());
    }

    public void addCaseBlock(Switch.Case _case, Block block)
    {
        CaseBlock caseBlock = new CaseBlock(_case, this, block);
        caseBlocks.add(caseBlock);
        ops.add(caseBlock);
    }

    public String getSource()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(indent).append("switch (").append(switchVar.source2()).append(")").append(NL);
        sb.append(indent).append("{").append(NL);
        for (Iterator it = caseBlocks.iterator(); it.hasNext();)
        {
            CaseBlock cb = (CaseBlock) it.next();
            cb.setIndent(indent + "    ");
            sb.append(cb.getSource());
        }
        sb.append(indent).append("}").append(NL);
        return sb.toString();
    }

    public List getCaseBlocks()
    {
        return caseBlocks;
    }

    public void analyze(Block block)
    {
        //OperationView intVal = block.removePriorPushOperation();
        OperationView intVal = context.pop();
        switchVar = intVal;
    }
}
