package ru.andrew.jclazz.core.constants;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.core.FieldDescriptor;

public class CONSTANT_Fieldref extends CONSTANT_Ref {
    private FieldDescriptor fieldDescriptor;

    protected CONSTANT_Fieldref(int num, int tag, Clazz clazz) {
        super(num, tag, clazz);
    }

    public void update() throws ClazzException {
        super.update();

        fieldDescriptor = new FieldDescriptor(refNameAndType.getDescriptor());
    }

    public FieldDescriptor getFieldDescriptor() {
        return fieldDescriptor;
    }

    public String getValue() {
        return fieldDescriptor.getFQN();
    }
}
