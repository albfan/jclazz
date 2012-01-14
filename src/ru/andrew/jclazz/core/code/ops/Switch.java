package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

import java.util.*;

/**
 * Opcodes: 170, 171<BR>
 * Parameters: N<BR>
 * Operand stack: index => <BR>
 */
public class Switch extends Operation
{
    private List caseBlocks;

    private int operationLength;

    public Switch(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }

    protected void loadParams(Code code)
    {
        caseBlocks = new ArrayList();

        int zero_bytes = 3 - (int) (start_byte % 4);
        code.skipBytes(zero_bytes);
        operationLength = zero_bytes + 1;

        int def1 = code.getNextByte();
        int def2 = code.getNextByte();
        int def3 = code.getNextByte();
        int def4 = code.getNextByte();
        int defaultOffset = (def1 << 24) | (def2 << 16) | (def3 << 8) | def4;
        caseBlocks.add(new Case(0, start_byte + defaultOffset, true));
        operationLength += 4;

        if (opcode.getOpcode() == 170)  // tableswitch
        {
            int lowbyte1 = code.getNextByte();
            int lowbyte2 = code.getNextByte();
            int lowbyte3 = code.getNextByte();
            int lowbyte4 = code.getNextByte();
            int highbyte1 = code.getNextByte();
            int highbyte2 = code.getNextByte();
            int highbyte3 = code.getNextByte();
            int highbyte4 = code.getNextByte();
            int high = (highbyte1 << 24) | (highbyte2 << 16) | (highbyte3 << 8) | highbyte4;
            int low = (lowbyte1 << 24) | (lowbyte2 << 16) | (lowbyte3 << 8) | lowbyte4;

            operationLength += 8;

            for (int i = low; i <= high; i++)
            {
                int jumpbyte1 = code.getNextByte();
                int jumpbyte2 = code.getNextByte();
                int jumpbyte3 = code.getNextByte();
                int jumpbyte4 = code.getNextByte();
                int jumpOffset = (jumpbyte1 << 24) | (jumpbyte2 << 16) | (jumpbyte3 << 8) | jumpbyte4;
                caseBlocks.add(new Case(i, start_byte + jumpOffset, false));
            }
            operationLength += 4 * (high - low + 1);
        }
        else if (opcode.getOpcode() == 171) // lookupswitch
        {
            int npairs1 = code.getNextByte();
            int npairs2 = code.getNextByte();
            int npairs3 = code.getNextByte();
            int npairs4 = code.getNextByte();
            int total_pairs = (npairs1 << 24) | (npairs2 << 16) | (npairs3 << 8) | npairs4;
            operationLength += 4;

            for (int i = 0; i < total_pairs; i++)
            {
                int matchbyte1 = code.getNextByte();
                int matchbyte2 = code.getNextByte();
                int matchbyte3 = code.getNextByte();
                int matchbyte4 = code.getNextByte();
                int matchOffset = (matchbyte1 << 24) | (matchbyte2 << 16) | (matchbyte3 << 8) | matchbyte4;

                int jumpbyte1 = code.getNextByte();
                int jumpbyte2 = code.getNextByte();
                int jumpbyte3 = code.getNextByte();
                int jumpbyte4 = code.getNextByte();
                int jumpOffset = (jumpbyte1 << 24) | (jumpbyte2 << 16) | (jumpbyte3 << 8) | jumpbyte4;
                caseBlocks.add(new Case(matchOffset, start_byte + jumpOffset, false));
            }
            operationLength += total_pairs * 8;
        }
    }

    public int getLength()
    {
        return operationLength;
    }

    public List getCaseBlocks()
    {
        return caseBlocks;
    }

    public class Case
    {
        private int value;
        private long offset;
        private boolean isDeafult = false;

        public Case(int value, long offset, boolean deafult)
        {
            this.value = value;
            this.offset = offset;
            this.isDeafult = deafult;
        }

        public long getOffset()
        {
            return offset;
        }

        public int getValue()
        {
            return value;
        }

        public boolean isDeafult()
        {
            return isDeafult;
        }
    }

    public String asString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(start_byte).append(" ").append(opcode.getMnemonic()).append(" ");
        for (Iterator it = caseBlocks.iterator(); it.hasNext();)
        {
            Case cb = (Case) it.next();
            if (cb.isDeafult)
            {
                sb.append("default:");
            }
            else
            {
                sb.append(cb.value).append(":");
            }
            sb.append(cb.offset).append(" ");
        }
        return sb.toString();
    }
    
}
