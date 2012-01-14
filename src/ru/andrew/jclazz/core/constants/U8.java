package ru.andrew.jclazz.core.constants;

import java.math.*;

public final class U8
{
    private static final BigInteger LOW_MASK = new BigInteger("FFFFFFFF", 16);

    private long high;
    private long low;

    public U8(long high, long low)
    {
        this.high = high;
        this.low = low;
    }

    public U8(String hex)
    {
        if (!hex.startsWith("0x"))
        {
            throw new RuntimeException("Invalid U8 hex format");
        }
        BigInteger h = new BigInteger(hex.substring(2), 16);
        this.high = h.shiftRight(32).longValue();
        this.low = h.and(LOW_MASK).longValue();
    }

    public long getHigh()
    {
        return high;
    }

    public long getLow()
    {
        return low;
    }

    public int compareTo(U8 u8)
    {
        if (high > u8.getHigh())
        {
            return 1;
        }
        else if (high < u8.getHigh())
        {
            return -1;
        }
        else
        {
            if (low > u8.getLow())
            {
                return 1;
            }
            else if (low < u8.getLow())
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }
    }

    public long lowest52bit()
    {
        return ((high & 0xFFFFFL) << 32) + low;
    }
}
