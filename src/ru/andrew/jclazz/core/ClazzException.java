package ru.andrew.jclazz.core;

public class ClazzException extends Exception
{
    public ClazzException(String message)
    {
        super(message);
    }

    public ClazzException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
