package ru.andrew.jclazz.decompiler.engine.blocks;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.ops.*;

public class CaseBlock extends Block
{
    private Switch.Case _case;
    private long exitGoto = 0;

    public CaseBlock(Switch.Case _case, SwitchBlock switchBlock, Block block)
    {
        super(switchBlock, block.getOperations());
        this._case = _case;

        // Try to find last goto, which means the end of switch block
        if (getLastOperation() instanceof GoToView)
        {
            exitGoto = ((GoToView) removeLastOperation()).getTargetOperation();
        }
    }

    public long getStartByte()
    {
        return _case.getOffset();
    }

    public long getLastGotoOffset()
    {
        return exitGoto;
    }

    public String getSource()
    {
        StringBuffer sb = new StringBuffer();
        if (_case.isDeafult() && isEmpty()) return ""; // absence of default block
        if (_case.isDeafult())
        {
            sb.append(indent).append("default");
        }
        else
        {
            sb.append(indent).append("case ").append(_case.getValue());
        }
        sb.append(":").append(NL);
        if (!isEmpty())
        {
            sb.append(super.getOperationsSource());
        }
        else
        {
            // Empty case block
        }
        if (exitGoto != 0)
        {
            sb.append(indent).append("    break;").append(NL);
        }
        return sb.toString();
    }
}