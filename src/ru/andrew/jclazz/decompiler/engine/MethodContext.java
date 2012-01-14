package ru.andrew.jclazz.decompiler.engine;

import java.util.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.decompiler.engine.ops.OperationView;

public class MethodContext
{
    private Stack stack = new Stack();;

    public MethodContext(MethodInfo methodInfo)
    {
        // Initialize local variables
        // TODO
    }

    //*** STACK METHODS ***//

    public void push(OperationView view)
    {
        stack.push(view);
    }

    public OperationView pop()
    {
        return (OperationView) stack.pop();
    }

    public OperationView peek()
    {
        return (OperationView) stack.peek();
    }

    public int stackSize()
    {
        return stack.size();
    }

    //*** METHODS ***//
}
