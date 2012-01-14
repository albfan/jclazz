package ru.andrew.jclazz.decompiler.engine.blockdetectors;

import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.attributes.Code.ExceptionTable;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.*;

import java.util.*;
import ru.andrew.jclazz.decompiler.engine.ops.GoToView;

public class TryDetector implements Detector
{
    private List trys = new ArrayList();
    private List fakeFinallies = new ArrayList();

    private List suspiciousFinallies = new ArrayList();

    public TryDetector(Code.ExceptionTable[] exc_table)
    {
        if (exc_table != null && exc_table.length > 0)
        {
            trys.addAll(Arrays.asList(exc_table));

            // Removing fake finally blocks for each catch block
            for (Iterator fit = trys.iterator(); fit.hasNext();)
            {
                Code.ExceptionTable cet = (Code.ExceptionTable) fit.next();
                if (cet.catch_type == null && !isReallyFinally(exc_table, cet))
                {
                    fakeFinallies.add(cet);
                    fit.remove();
                }
            }

            // Join splitted try-catch blocks
            for (Iterator fit = trys.iterator(); fit.hasNext();)
            {
                Code.ExceptionTable cet = (Code.ExceptionTable) fit.next();
                for (int k = 0; k < trys.size(); k++)
                {
                    Code.ExceptionTable etn = (ExceptionTable) trys.get(k);
                    if (cet.start_pc == etn.start_pc && cet.end_pc == etn.end_pc && cet.handler_pc == etn.handler_pc) continue;
                    if (etn.handler_pc == cet.handler_pc && cet.start_pc > etn.end_pc)
                    {
                        // Removing etn as it is simply continue of cet
                        suspiciousFinallies.add(new int[]{etn.end_pc, cet.start_pc});
                        fit.remove();
                        etn.end_pc = cet.end_pc;
                        break;
                    }
                }
            }

            // sorting try-catches
            Collections.sort(trys, new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    Code.ExceptionTable cet1 = (Code.ExceptionTable) o1;
                    Code.ExceptionTable cet2 = (Code.ExceptionTable) o2;

                    if (cet1.end_pc < cet2.start_pc)
                    {
                        int comparison = cet1.end_pc - cet2.end_pc;
                        if (comparison == 0) comparison = cet1.handler_pc - cet2.handler_pc;
                        return comparison;
                    }
                    else
                    {
                        return cet1.start_pc - cet2.start_pc;
                    }
                }
            });
        }
    }

    private boolean isReallyFinally(Code.ExceptionTable[] exc_table, Code.ExceptionTable fin)
    {
        if (fin.start_pc == fin.handler_pc) return false;

        for (int i = 0; i < exc_table.length; i++)
        {
            if ((exc_table[i].catch_type != null) && (exc_table[i].handler_pc == fin.start_pc))
            {
                return false;
            }
        }
        return true;
    }

    public void analyze(Block block)
    {
        if (trys.isEmpty()) return;

        // Creating blocks
        Iterator it = trys.iterator();
        boolean hasMoreTries = true;
        Code.ExceptionTable etn = null;
        do
        {
            if (!it.hasNext() && etn == null) break;
            Code.ExceptionTable et = etn == null ? (Code.ExceptionTable) it.next() : etn;

            if ((et.start_pc < block.getStartByte()) || (et.end_pc > block.getLastOperation().getStartByte()))
            {
                etn = null;
                continue;
            }
            if (findSubBlock(block, et.start_pc, et.end_pc))
            {
                etn = null;
                continue;
            }

            // If trys are sorted correctly then next iterator value will be first catch for this try block
            // We assume that its start byte will be end byte for try
            CodeItem gop = block.getOperationPriorTo(et.handler_pc);

            Try _try = new Try(et.start_pc, et.end_pc, block);
            _try.setFinallyException(fakeFinallies);
            _try.setSuspiciousFinallies(suspiciousFinallies);
            block.createSubBlock(et.start_pc, gop.getStartByte() + 1, _try);
            it.remove();

            // Processing several catch blocks (except last)
            Catch _catch = new Catch(et.handler_pc, et.catch_type, block);
            hasMoreTries = false;
            while (it.hasNext())
            {
                etn = (Code.ExceptionTable) it.next();
                if (((etn.start_pc != et.start_pc) || (etn.end_pc != et.end_pc && etn.end_pc > gop.getStartByte())))
                {
                    hasMoreTries = true;
                    break;
                }

                block.createSubBlock(et.handler_pc, etn.handler_pc, _catch);
                _try.addCatchBlock(_catch);
                et = etn;
                _catch = new Catch(et.handler_pc, et.catch_type, block);
                it.remove();
            }
            // Processing last catch block
            long retr_pc = _try.getRetrieveOperation();
            for (Iterator cit = _try.getCatchBlocks().iterator(); cit.hasNext();)
            {
                Catch __catch = (Catch) cit.next();
                if (__catch.getRetrievePc() < retr_pc)
                {
                    retr_pc = __catch.getRetrievePc();
                }
            }
            if (retr_pc == Integer.MAX_VALUE && block.getLastOperation() instanceof GoToView)
            {
                retr_pc = block.getLastOperation().getStartByte();
            }
            block.createSubBlock(et.handler_pc, retr_pc, _catch);
            _try.addCatchBlock(_catch);
            _try.removeSuspiciousInlinedFinallyBlocks();
        }
        while (hasMoreTries);
    }

    private boolean findSubBlock(Block block, long start_pc, long end_pc)
    {
        CodeItem ci = block.getOperationByStartByte(start_pc);
        if (ci instanceof Block)
        {
            return ((Block) ci).getLastOperation().getStartByte() > end_pc;
        }          
        return ci == null;
    }
}
