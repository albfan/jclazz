package ru.andrew.jclazz.core.code;

public final class Opcode
{
    private int opcode;
    private String mnemonic;
    private String description;
    private int params_count;
    private boolean isVariableLength;

    private Opcode(int opcode, String mnemonic, String description, int params_count)
    {
        this.opcode = opcode;
        this.mnemonic = mnemonic;
        this.description = description;
        this.params_count = params_count;
        this.isVariableLength = params_count < 0;
    }

    public int getOpcode()
    {
        return opcode;
    }

    public String getMnemonic()
    {
        return mnemonic;
    }

    public String getDescription()
    {
        return description;
    }

    public boolean isVariableLength()
    {
        return isVariableLength;
    }

    public int getParamsCount()
    {
        return params_count;
    }

    // Standard opcodes

    private static Opcode[] opcodes = new Opcode[256];

    static
    {
        opcodes[0] = new Opcode(0, "nop", "Do nothing", 0);

        // Load Constant Instructions (1 - 20)
        opcodes[1] = new Opcode(1, "aconst_null", "Push null", 0);
        opcodes[2] = new Opcode(2, "iconst_m1", "Push int constant", 0);
        opcodes[3] = new Opcode(3, "iconst_0", "Push int constant", 0);
        opcodes[4] = new Opcode(4, "iconst_1", "Push int constant", 0);
        opcodes[5] = new Opcode(5, "iconst_2", "Push int constant", 0);
        opcodes[6] = new Opcode(6, "iconst_3", "Push int constant", 0);
        opcodes[7] = new Opcode(7, "iconst_4", "Push int constant", 0);
        opcodes[8] = new Opcode(8, "iconst_5", "Push int constant", 0);
        opcodes[9] = new Opcode(9, "lconst_0", "Push long constant", 0);
        opcodes[10] = new Opcode(10, "lconst_1", "Push long constant", 0);
        opcodes[11] = new Opcode(11, "fconst_0", "Push float", 0);
        opcodes[12] = new Opcode(12, "fconst_1", "Push float", 0);
        opcodes[13] = new Opcode(13, "fconst_2", "Push float", 0);
        opcodes[14] = new Opcode(14, "dconst_0", "Push double", 0);
        opcodes[15] = new Opcode(15, "dconst_1", "Push double", 0);
        opcodes[16] = new Opcode(16, "bipush", "Push byte", 1);
        opcodes[17] = new Opcode(17, "sipush", "Push short", 2);
        opcodes[18] = new Opcode(18, "ldc", "Push item from runtime constant pool", 1);
        opcodes[19] = new Opcode(19, "ldc_w", "Push item from runtime constant pool (wide index)", 2);
        opcodes[20] = new Opcode(20, "ldc2_w", "Push long or double from runtime constant pool (wide index)", 2);

        // Load Instructions (21 - 53)
        opcodes[21] = new Opcode(21, "iload", "Load int from local variable", 1);
        opcodes[22] = new Opcode(22, "lload", "Load long from local variable", 1);
        opcodes[23] = new Opcode(23, "fload", "Load float from local variable", 1);
        opcodes[24] = new Opcode(24, "dload", "Load double from local variable", 1);
        opcodes[25] = new Opcode(25, "aload", "Load reference from local variable", 1);
        opcodes[26] = new Opcode(26, "iload_0", "Load int from local variable", 0);
        opcodes[27] = new Opcode(27, "iload_1", "Load int from local variable", 0);
        opcodes[28] = new Opcode(28, "iload_2", "Load int from local variable", 0);
        opcodes[29] = new Opcode(29, "iload_3", "Load int from local variable", 0);
        opcodes[30] = new Opcode(30, "lload_0", "Load long from local variable", 0);
        opcodes[31] = new Opcode(31, "lload_1", "Load long from local variable", 0);
        opcodes[32] = new Opcode(32, "lload_2", "Load long from local variable", 0);
        opcodes[33] = new Opcode(33, "lload_3", "Load long from local variable", 0);
        opcodes[34] = new Opcode(34, "fload_0", "Load float from local variable", 0);
        opcodes[35] = new Opcode(35, "fload_1", "Load float from local variable", 0);
        opcodes[36] = new Opcode(36, "fload_2", "Load float from local variable", 0);
        opcodes[37] = new Opcode(37, "fload_3", "Load float from local variable", 0);
        opcodes[38] = new Opcode(38, "dload_0", "Load double from local variable", 0);
        opcodes[39] = new Opcode(39, "dload_1", "Load double from local variable", 0);
        opcodes[40] = new Opcode(40, "dload_2", "Load double from local variable", 0);
        opcodes[41] = new Opcode(41, "dload_3", "Load double from local variable", 0);
        opcodes[42] = new Opcode(42, "aload_0", "Load reference from local variable", 0);
        opcodes[43] = new Opcode(43, "aload_1", "Load reference from local variable", 0);
        opcodes[44] = new Opcode(44, "aload_2", "Load reference from local variable", 0);
        opcodes[45] = new Opcode(45, "aload_3", "Load reference from local variable", 0);
        opcodes[46] = new Opcode(46, "iaload", "Load int from array", 0);
        opcodes[47] = new Opcode(47, "laload", "Load long from array", 0);
        opcodes[48] = new Opcode(48, "faload", "Load float from array", 0);
        opcodes[49] = new Opcode(49, "daload", "Load double from array", 0);
        opcodes[50] = new Opcode(50, "aaload", "Load reference from array", 0);
        opcodes[51] = new Opcode(51, "baload", "Load byte or boolean from array", 0);
        opcodes[52] = new Opcode(52, "caload", "Load char from array", 0);
        opcodes[53] = new Opcode(53, "saload", "Load short from array", 0);

        // Store Instructions (54 - 86)
        opcodes[54] = new Opcode(54, "istore", "Store int into local variable", 1);
        opcodes[55] = new Opcode(55, "lstore", "Store long into local variable", 1);
        opcodes[56] = new Opcode(56, "fstore", "Store float into local variable", 1);
        opcodes[57] = new Opcode(57, "dstore", "Store double into local variable", 1);
        opcodes[58] = new Opcode(58, "astore", "Store reference into local variable", 1);
        opcodes[59] = new Opcode(59, "istore_0", "Store int into local variable", 0);
        opcodes[60] = new Opcode(60, "istore_1", "Store int into local variable", 0);
        opcodes[61] = new Opcode(61, "istore_2", "Store int into local variable", 0);
        opcodes[62] = new Opcode(62, "istore_3", "Store int into local variable", 0);
        opcodes[63] = new Opcode(63, "lstore_0", "Store long into local variable", 0);
        opcodes[64] = new Opcode(64, "lstore_1", "Store long into local variable", 0);
        opcodes[65] = new Opcode(65, "lstore_2", "Store long into local variable", 0);
        opcodes[66] = new Opcode(66, "lstore_3", "Store long into local variable", 0);
        opcodes[67] = new Opcode(67, "fstore_0", "Store float into local variable", 0);
        opcodes[68] = new Opcode(68, "fstore_1", "Store float into local variable", 0);
        opcodes[69] = new Opcode(69, "fstore_2", "Store float into local variable", 0);
        opcodes[70] = new Opcode(70, "fstore_3", "Store float into local variable", 0);
        opcodes[71] = new Opcode(71, "dstore_0", "Store double into local variable", 0);
        opcodes[72] = new Opcode(72, "dstore_1", "Store double into local variable", 0);
        opcodes[73] = new Opcode(73, "dstore_2", "Store double into local variable", 0);
        opcodes[74] = new Opcode(74, "dstore_3", "Store double into local variable", 0);
        opcodes[75] = new Opcode(75, "astore_0", "Store reference into local variable", 0);
        opcodes[76] = new Opcode(76, "astore_1", "Store reference into local variable", 0);
        opcodes[77] = new Opcode(77, "astore_2", "Store reference into local variable", 0);
        opcodes[78] = new Opcode(78, "astore_3", "Store reference into local variable", 0);
        opcodes[79] = new Opcode(79, "iastore", "Store into int array", 0);
        opcodes[80] = new Opcode(80, "lastore", "Store into long array", 0);
        opcodes[81] = new Opcode(81, "fastore", "Store into float array", 0);
        opcodes[82] = new Opcode(82, "dastore", "Store into double array", 0);
        opcodes[83] = new Opcode(83, "aastore", "Store into reference array", 0);
        opcodes[84] = new Opcode(84, "bastore", "Store into byte or boolean array", 0);
        opcodes[85] = new Opcode(85, "castore", "Store into char array", 0);
        opcodes[86] = new Opcode(86, "sastore", "Store into short array", 0);

        // Operand Stack Management Instructions (87 - 95)
        opcodes[87] = new Opcode(87, "pop", "Pop the top operand stack value", 0);
        opcodes[88] = new Opcode(88, "pop2", "Pop the top one or two operand stack values", 0);
        opcodes[89] = new Opcode(89, "dup", "Duplicate the top operand stack value", 0);
        opcodes[90] = new Opcode(90, "dup_x1", "Duplicate the top operand stack value and insert two values down", 0);
        opcodes[91] = new Opcode(91, "dup_x2", "Duplicate the top operand stack value and insert two or three values down", 0);
        opcodes[92] = new Opcode(92, "dup2", "Duplicate the top one or two operand stack values", 0);
        opcodes[93] = new Opcode(93, "dup2_x1", "Duplicate the top one or two operand stack values and insert two or three values down", 0);
        opcodes[94] = new Opcode(94, "dup2_x2", "Duplicate the top one or two operand stack values and insert two, three, or four values down", 0);
        opcodes[95] = new Opcode(95, "swap", "Swap the top two operand stack values", 0);

        // Arithmetic Instructions (96 - 119)
        opcodes[96] = new Opcode(96, "iadd", "Add int", 0);
        opcodes[97] = new Opcode(97, "ladd", "Add long", 0);
        opcodes[98] = new Opcode(98, "fadd", "Add float", 0);
        opcodes[99] = new Opcode(99, "dadd", "Add double", 0);
        opcodes[100] = new Opcode(100, "isub", "Subtract int", 0);
        opcodes[101] = new Opcode(101, "lsub", "Subtract long", 0);
        opcodes[102] = new Opcode(102, "fsub", "Subtract float", 0);
        opcodes[103] = new Opcode(103, "dsub", "Subtract double", 0);
        opcodes[104] = new Opcode(104, "imul", "Multiply int", 0);
        opcodes[105] = new Opcode(105, "lmul", "Multiply long", 0);
        opcodes[106] = new Opcode(106, "fmul", "Multiply float", 0);
        opcodes[107] = new Opcode(107, "dmul", "Multiply double", 0);
        opcodes[108] = new Opcode(108, "idiv", "Divide int", 0);
        opcodes[109] = new Opcode(109, "ldiv", "Divide long", 0);
        opcodes[110] = new Opcode(110, "fdiv", "Divide float", 0);
        opcodes[111] = new Opcode(111, "ddiv", "Divide double", 0);
        opcodes[112] = new Opcode(112, "irem", "Remainder int", 0);
        opcodes[113] = new Opcode(113, "lrem", "Remainder long", 0);
        opcodes[114] = new Opcode(114, "frem", "Remainder float", 0);
        opcodes[115] = new Opcode(115, "drem", "Remainder double", 0);
        opcodes[116] = new Opcode(116, "ineg", "Negate int", 0);
        opcodes[117] = new Opcode(117, "lneg", "Negate long", 0);
        opcodes[118] = new Opcode(118, "fneg", "Negate float", 0);
        opcodes[119] = new Opcode(119, "dneg", "Negate double", 0);

        // Bit Arithmetic Instrutions (120 - 131)
        opcodes[120] = new Opcode(120, "ishl", "Shift left int", 0);
        opcodes[121] = new Opcode(121, "lshl", "Shift left", 0);
        opcodes[122] = new Opcode(122, "ishr", "Arithmetic shift right int", 0);
        opcodes[123] = new Opcode(123, "lshr", "Arithmetic shift right long", 0);
        opcodes[124] = new Opcode(124, "iushr", "Logical shift right int", 0);
        opcodes[125] = new Opcode(125, "lushr", "Logical shift right long", 0);
        opcodes[126] = new Opcode(126, "iand", "Boolean AND int", 0);
        opcodes[127] = new Opcode(127, "land", "Boolean AND long", 0);
        opcodes[128] = new Opcode(128, "ior", "Boolean OR int", 0);
        opcodes[129] = new Opcode(129, "lor", "Boolean OR long", 0);
        opcodes[130] = new Opcode(130, "ixor", "Boolean XOR int", 0);
        opcodes[131] = new Opcode(131, "lxor", "Boolean XOR long", 0);

        opcodes[132] = new Opcode(132, "iinc", "Increment local variable by constant", 2);

        // Type Conversion Instructions (133 - 147)
        opcodes[133] = new Opcode(133, "i2l", "Convert int to long", 0);
        opcodes[134] = new Opcode(134, "i2f", "Convert int to float", 0);
        opcodes[135] = new Opcode(135, "i2d", "Convert int to double", 0);
        opcodes[136] = new Opcode(136, "l2i", "Convert long to int", 0);
        opcodes[137] = new Opcode(137, "l2f", "Convert long to float", 0);
        opcodes[138] = new Opcode(138, "l2d", "Convert long to double", 0);
        opcodes[139] = new Opcode(139, "f2i", "Convert float to int", 0);
        opcodes[140] = new Opcode(140, "f2l", "Convert float to long", 0);
        opcodes[141] = new Opcode(141, "f2d", "Convert float to double", 0);
        opcodes[142] = new Opcode(142, "d2i", "Convert double to int", 0);
        opcodes[143] = new Opcode(143, "d2l", "Convert double to long", 0);
        opcodes[144] = new Opcode(144, "d2f", "Convert double to float", 0);
        opcodes[145] = new Opcode(145, "i2b", "Convert int to byte", 0);
        opcodes[146] = new Opcode(146, "i2c", "Convert int to char", 0);
        opcodes[147] = new Opcode(147, "i2s", "Convert int to short", 0);

        // Comparison Instructions (148 - 152)
        opcodes[148] = new Opcode(148, "lcmp", "Compare long", 0);
        opcodes[149] = new Opcode(149, "fcmpl", "Compare float", 0);
        opcodes[150] = new Opcode(150, "fcmpg", "Compare float", 0);
        opcodes[151] = new Opcode(151, "dcmpl", "Compare double", 0);
        opcodes[152] = new Opcode(152, "dcmpg", "Compare double", 0);

        // Control Transfer Instructions (153 - 171, 198 - 201)
        opcodes[153] = new Opcode(153, "ifeq", "Branch if int comparison with zero succeeds", 2);
        opcodes[154] = new Opcode(154, "ifne", "Branch if int comparison with zero succeeds", 2);
        opcodes[155] = new Opcode(155, "iflt", "Branch if int comparison with zero succeeds", 2);
        opcodes[156] = new Opcode(156, "ifge", "Branch if int comparison with zero succeeds", 2);
        opcodes[157] = new Opcode(157, "ifgt", "Branch if int comparison with zero succeeds", 2);
        opcodes[158] = new Opcode(158, "ifle", "Branch if int comparison with zero succeeds", 2);
        opcodes[159] = new Opcode(159, "if_icmpeq", "Branch if int comparison succeeds", 2);
        opcodes[160] = new Opcode(160, "if_icmpne", "Branch if int comparison succeeds", 2);
        opcodes[161] = new Opcode(161, "if_icmplt", "Branch if int comparison succeeds", 2);
        opcodes[162] = new Opcode(162, "if_icmpge", "Branch if int comparison succeeds", 2);
        opcodes[163] = new Opcode(163, "if_icmpgt", "Branch if int comparison succeeds", 2);
        opcodes[164] = new Opcode(164, "if_icmple", "Branch if int comparison succeeds", 2);
        opcodes[165] = new Opcode(165, "if_acmpeq", "Branch if reference comparison succeeds", 2);
        opcodes[166] = new Opcode(166, "if_acmpne", "Branch if reference comparison succeeds", 2);
        opcodes[167] = new Opcode(167, "goto", "Branch always", 2);
        opcodes[168] = new Opcode(168, "jsr", "Jump subroutine", 2);
        opcodes[169] = new Opcode(169, "ret", "Return from subroutine", 1);
        opcodes[170] = new Opcode(170, "tableswitch", "Access jump table by index and jump", -1);
        opcodes[171] = new Opcode(171, "lookupswitch", "Access jump table by key match and jump", -1);
        opcodes[198] = new Opcode(198, "ifnull", "Branch if reference is 0", 2);
        opcodes[199] = new Opcode(199, "ifnonnull", "Branch if reference not 0", 2);
        opcodes[200] = new Opcode(200, "goto_w", "Branch always (wide index)", 4);
        opcodes[201] = new Opcode(201, "jsr_w", "Jump subroutine (wide index)", 4);

        // Return Instructions (172 - 177)
        opcodes[172] = new Opcode(172, "ireturn", "Return int from method", 0);
        opcodes[173] = new Opcode(173, "lreturn", "Return long from method", 0);
        opcodes[174] = new Opcode(174, "freturn", "Return float from method", 0);
        opcodes[175] = new Opcode(175, "dreturn", "Return double from method", 0);
        opcodes[176] = new Opcode(176, "areturn", "Return reference from method", 0);
        opcodes[177] = new Opcode(177, "return", "Return void from method", 0);

        // Object Creation and Manipulation Instructions (178 - 193, 197)
        opcodes[178] = new Opcode(178, "getstatic", "Get static field from class", 2);
        opcodes[179] = new Opcode(179, "putstatic", "Set static field in class", 2);
        opcodes[180] = new Opcode(180, "getfield", "Fetch field from object", 2);
        opcodes[181] = new Opcode(181, "putfield", "Set field in object", 2);
        opcodes[182] = new Opcode(182, "invokevirtual", "Invoke instance method; dispatch based on class", 2);
        opcodes[183] = new Opcode(183, "invokespecial", "Invoke instance method; special handling for superclass, private, and instance initialization method invocations", 2);
        opcodes[184] = new Opcode(184, "invokestatic", "Invoke a class (static) method", 2);
        opcodes[185] = new Opcode(185, "invokeinterface", "Invoke interface method", 4);
        opcodes[186] = new Opcode(186, "invokedynamic", "Invoke instance method; resolve and dispatch based on class", 2);  // Since Java 1.5
        opcodes[187] = new Opcode(187, "new", "Create new object", 2);
        opcodes[188] = new Opcode(188, "newarray", "Create new array", 1);
        opcodes[189] = new Opcode(189, "anewarray", "Create new array of reference", 2);
        opcodes[190] = new Opcode(190, "arraylength", "Get length of array", 0);
        opcodes[191] = new Opcode(191, "athrow", "Throw exception or error", 0);
        opcodes[192] = new Opcode(192, "checkcast", "Check whether object is of given type", 2);
        opcodes[193] = new Opcode(193, "instanceof", "Determine if object is of given type", 2);
        opcodes[197] = new Opcode(197, "multianewarray", "Create new multidimensional array", 3);

        // Monitor Instructions (194 - 195)
        opcodes[194] = new Opcode(194, "monitorenter", "Enter monitor for object", 0);
        opcodes[195] = new Opcode(195, "monitorexit", "Exit monitor for object", 0);

        opcodes[196] = new Opcode(196, "wide", "Extend local variable index by additional bytes", -1);

        // Reserved Opcodes
        opcodes[202] = new Opcode(202, "breakpoint", "RESERVED", 0);
        opcodes[254] = new Opcode(254, "impdep1", "RESERVED", 0);
        opcodes[255] = new Opcode(255, "impdep2", "RESERVED", 0);
    }

    public static Opcode getOpcode(int opcode)
    {
        return opcodes[opcode];
    }
}
