package ru.andrew.jclazz.decompiler.engine.blocks;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.engine.ops.*;

import java.util.*;

public class Try extends Block
{
    public static boolean DEBUG = false;

    private List catchBlocks = new ArrayList();
    private List finallyExceptions = new ArrayList();

    private List suspiciousFinallies = null;

    private int goto_cmd = -1;

    private long start_pc;
    private long end_pc;

    public Try(long start_pc, long end_pc, Block parent)
    {
        super(parent);
        this.start_pc = start_pc;
        this.end_pc = end_pc;
    }

    public void setSuspiciousFinallies(List suspiciousFinallies)
    {
        this.suspiciousFinallies = suspiciousFinallies;
    }

    public void addCatchBlock(Catch _catch)
    {
        catchBlocks.add(_catch);
    }

    public List getCatchBlocks()
    {
        return catchBlocks;
    }

    public void setFinallyException(List excTable)
    {
        finallyExceptions = excTable;
    }

    public long getStartPC()
    {
        return start_pc;
    }

    public long getEndPC()
    {
        return end_pc;
    }

    public int getRetrieveOperation()
    {
        return goto_cmd;
    }

    public String getSource()
    {
        return indent + "try" + NL + super.getSource();
    }

    public void postCreate()
    {
        CodeItem gop = getLastOperation();
        if (gop instanceof GoToView && ((GoToView) gop).isForwardBranch())
        {
            goto_cmd = (int) ((GoToView) gop).getTargetOperation();
        }
        else
        {
            goto_cmd = Integer.MAX_VALUE;
        }
    }

    public void removeSuspiciousInlinedFinallyBlocks()
    {
        // Checking inlined finally block in try block
        if (suspiciousFinallies != null && !suspiciousFinallies.isEmpty())
        {
            for (Iterator sif = suspiciousFinallies.iterator(); sif.hasNext();)
            {
                int[] gap = (int[]) sif.next();
                if (gap[0] > start_pc && gap[1] < end_pc)
                {
                    CodeItem retItem = getOperationPriorTo(gap[1]);
                    if (retItem instanceof ReturnView)
                    {
                        boolean isFinallyExists = false;
                        for (int k = 0; k < catchBlocks.size(); k++)
                        {
                            if (((Catch) catchBlocks.get(k)).isFinally())
                            {
                                isFinallyExists = true;
                                break;
                            }
                        }
                        if (isFinallyExists)
                        {
                            for (long f_byte = gap[0]; f_byte < retItem.getStartByte(); f_byte++)
                            {
                                removeOperation(f_byte);
                            }
                        }
                    }
                }
            }
        }
    }

    public void postProcess()
    {
        // Checking inlined finally block in try block
        // For jsr-ret constructions it also truncates jsr + goto operations
        if (end_pc < getLastOperation().getStartByte())
        {
            truncate(end_pc);
        }

        // Checking inlined finally block in catch blocks
        Iterator i = finallyExceptions.iterator();
        while (i.hasNext())
        {
            Code.ExceptionTable et = (Code.ExceptionTable) i.next();

            for (Iterator cit = catchBlocks.iterator(); cit.hasNext();)
            {
                Catch _catch = (Catch) cit.next();

                if (et.end_pc > _catch.getFirstOperation().getStartByte() &&
                        et.end_pc < _catch.getLastOperation().getStartByte() &&
                        et.handler_pc > _catch.getLastOperation().getStartByte())
                {
                    _catch.truncate(et.end_pc);
                }
            }
        }

        // Checking last goto: break or continue
        if (getLastOperation() instanceof GoToView)
        {
            if (!checkLastGoTo((GoToView) getLastOperation()))
            {
                removeLastOperation();
            }
        }
    }

    private boolean checkLastGoTo(GoToView oper)
    {
        Block ff_block = this;
        CodeItem ff_oper;
        do
        {
            ff_oper = ff_block.getOperationByStartByte(oper.getTargetOperation());
            if (ff_oper != null) break;
            ff_block = ff_block.getParent();
        }
        while (ff_block != null);
        if (ff_oper == null) return false;

        CodeItem prevFF = ff_block.getOperationPriorTo(ff_oper.getStartByte());
        if (prevFF instanceof Loop)
        {
            oper.setBreak(true);
            return true;
        }
        if (ff_block instanceof Loop)
        {
            Loop ff_loop = (Loop) ff_block;
            if (!ff_loop.isBackLoop() && (ff_loop.getBeginPc() == oper.getTargetOperation()))
            {
                oper.setContinue(true);
                return true;
            }
            else if (ff_loop.isBackLoop())
            {
                if (!(prevFF instanceof Catch))
                {
                    oper.setContinue(true);
                    return true;
                }
            }
        }

        return false;
    }
}
