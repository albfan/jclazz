package ru.andrew.jclazz.decompiler.engine;

import ru.andrew.jclazz.core.code.ops.Operation;
import ru.andrew.jclazz.decompiler.FileInputStreamBuilder;
import ru.andrew.jclazz.decompiler.InputStreamBuilder;
import ru.andrew.jclazz.decompiler.MethodSourceView;
import ru.andrew.jclazz.decompiler.engine.blockdetectors.*;
import ru.andrew.jclazz.decompiler.engine.blocks.Block;
import ru.andrew.jclazz.decompiler.engine.blocks.Loop;
import ru.andrew.jclazz.decompiler.engine.ops.InvokeView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ByteCodeConverter
{
    private InputStreamBuilder builder;

    public ByteCodeConverter() {
        this(new FileInputStreamBuilder());
    }

    public ByteCodeConverter(InputStreamBuilder builder)
    {
        this.builder = builder;
    }

    public static Block detectBlocks(Block topBlock, MethodSourceView msv)
    {
        detectBlocks(topBlock, new TryDetector(msv.getMethod().getCodeBlock().getExceptionTable()));

        detectBlocks(topBlock, new SwitchDetector());

        detectBlocks(topBlock, new BackLoopDetector());
        detectBlocks(topBlock, new LoopDetector());

        
        

        detectBlocks(topBlock, new IfDetector());

        detectBlocks(topBlock, new UnconditionalBackLoopDetector());

        // Pass 2
        detectBlocks(topBlock, new IfDetector());
        return topBlock;
    }

    private HashMap op2constructor = new HashMap();
    public Block convertToViewOperations(List operations, MethodSourceView msv)
    {
        ArrayList list = new ArrayList(operations.size());
        for (Iterator i = operations.iterator(); i.hasNext();)
        {
            Operation op = (Operation) i.next();
            String className = op.getClass().getName();
            Constructor constructor = (Constructor) op2constructor.get(className);
            CodeItem codeItem;
            if (constructor == null)
            {
                String shortClassName = className.substring(className.lastIndexOf('.') + 1);
                if (shortClassName.equals("Invoke")) {
                    codeItem = new InvokeView(op, msv, builder);
                    list.add(codeItem);
                    continue;
                } else {
                    String newName = "ru.andrew.jclazz.decompiler.engine.ops." + shortClassName + "View";
                    try
                    {
                        constructor = Class.forName(newName).getConstructor(new Class[]{Operation.class, MethodSourceView.class});
                        op2constructor.put(className, constructor);
                    }
                    catch (ClassNotFoundException e)
                    {
                        throw new RuntimeException(e);
                    }
                    catch (NoSuchMethodException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }

            try
            {
                codeItem = (CodeItem) constructor.newInstance(new Object[]{op, msv});
            }
            catch (Exception ex)
            {
                throw new RuntimeException(ex);
            }
            list.add(codeItem);
        }
        return new Block(list, msv);
    }

    private static void detectBlocks(Block block, Detector detector)
    {
        if (block == null) return;
        detector.analyze(block);

        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem ci = block.next();
            if (ci instanceof Block)
            {
                detectBlocks((Block) ci, detector);
            }
        }
    }

    public static Block convert(Block topBlock, MethodSourceView msv)
    {
        detectBlocks(topBlock, msv);
        postProcess(topBlock);
        //analyze(topBlock);
        analyze2(topBlock);
        detectCompoundBackLoops(topBlock, new BackLoopDetector());
        return topBlock;
    }

    private static void postProcess(Block block)
    {
        if (block == null) return;
        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem citem = block.next();
            if (citem instanceof Block)
            {
                ((Block) citem).postProcess();
                postProcess((Block) citem);
            }
        }
    }

    private static void analyze(Block block)
    {
        if (block == null) return;
        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem citem = block.next();
            citem.analyze(block);

            if (citem instanceof Block)
            {
                if (citem instanceof Loop)
                {
                    ((Loop) citem).preanalyze(block);
                }
                analyze((Block) citem);
                if (citem instanceof Loop)
                {
                    ((Loop) citem).postanalyze(block);
                }
            }
        }
    }

    private static void analyze2(Block block)
    {
        if (block == null) return;
        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem citem = block.next();
            citem.analyze2(block);

            if (citem instanceof Block)
            {
                ((Block) citem).preanalyze(block);
                analyze2((Block) citem);
                ((Block) citem).postanalyze(block);
            }
        }
    }

    private static Loop detectCompoundBackLoops(Block block, BackLoopDetector detector)
    {
        if (block == null) return null;
        Loop innerLoop = detector.collapseBackLoops(block);
        if (innerLoop != null)
        {
            return innerLoop;
        }

        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem ci = block.next();
            if (ci instanceof Block)
            {
                Block loop = (Block) ci;
                do
                {
                    loop = detectCompoundBackLoops(loop, detector);
                    if (loop != null) block.replaceCurrentOperation(loop);
                }
                while (loop != null);
            }
        }
        return null;
    }
}
