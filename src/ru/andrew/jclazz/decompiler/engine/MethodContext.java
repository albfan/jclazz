package ru.andrew.jclazz.decompiler.engine;

import ru.andrew.jclazz.core.MethodInfo;
import ru.andrew.jclazz.decompiler.engine.ops.OperationView;

import java.util.Stack;

public class MethodContext {
    private Stack stack = new Stack();
    ;

    public MethodContext(MethodInfo methodInfo) {
        // Initialize local variables
        // TODO
    }

    //*** STACK METHODS ***//

    public void push(OperationView view) {
        stack.push(view);
    }

    public OperationView pop() {
        return stack.isEmpty() ? null : (OperationView) stack.pop();
    }

    public OperationView peek() {
        return (OperationView) stack.peek();
    }

    public int stackSize() {
        return stack.size();
    }

    //*** METHODS ***//
}
