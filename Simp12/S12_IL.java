package Simp12;
/**
 * Instruction level simulator for S12
 * ISA
 *
 * @author ekondratyuk
 * @date Fall 2025
 */


import java.io.*;

public class S12_IL {
    byte PC;
    short ACC;
    String memOutFilename;
    String traceFilename;
    short[] mem;
    int memSize = 256;

    String instrTrace;
    boolean isHalted;


    public S12_IL(String baseName, short accumulator, byte programCounter) {
        ACC = accumulator;
        PC = programCounter;
        mem = new short[memSize];
        instrTrace = "";
        isHalted = false;
        for (int i = 0; i < memSize; i++) {
            mem[i] = 0;
        }
        memOutFilename = baseName + "_memOut";
        traceFilename = baseName + "_trace";
    }

    public S12_IL() {
        new S12_IL("basefile", (short) 0, (byte) 0);
    }

    public S12_IL(short accumulator, byte programCounter) {
        new S12_IL("basefile", accumulator, programCounter);
    }

    public boolean intializeMem(String filename) {
        // Assuming file already read and in format
        // First line is PC, ACC
        // Keep reading lines for all mem spaces.
        String[] memArrStr = filename.split("\n");
        if (memArrStr.length != memSize) {
            System.out.println("NOTE: Memory Array is not " + memSize + " lines! Last mem address: " + memArrStr.length +
                    ". All other memory addresses will be set to zero!");
        }
        String[] memStr;
        byte memAddr;
        short memVal;

        for (int i = 0; i < memArrStr.length; i++) {
            memStr = memArrStr[i].split(" ");
            if (memStr.length != 2) { //Each line should only contain address and value
                System.err.println("initializeMem ERR! line" + i + "contains more than 2 numbers");
                return false;
            }
            if (memStr[0].length() != 2) { //00 to FF, so string should not be more than 2 characters!
                System.err.println("initializeMem ERR! line" + i + "memory address contains invalid address: " + memStr[0] +
                        ". Please change to valid value (00..FF)");
                return false;
            }
            if (memStr[1].length() != 12) { //12-bit binary value.
                System.err.println("initializeMem ERR! line" + i + "memory value is not 12-bits!: " + memStr[1] +
                        ". Please change to valid 12-bit binary value");
                return false;
            }
            try {
                memAddr = Byte.parseByte(memStr[0], 16); //First number is hex string
                memVal = Short.parseShort(memStr[1], 2); //Second number is binary
            } catch (NumberFormatException e) {
                System.err.println("initializeMem ERR! String is not in proper format. Memory address should be 2 hex values." +
                        " Memory value should be 12-bit binary value: " + e.getMessage());
                return false;
            }

            if (memAddr != i) { //Make sure that the memory address is the expected address
                System.err.println("initializeMem ERR! memory address is not in order. Expecting" + i + ". Got " +
                        memAddr + ".");
                return false;
            }

            // If all passes, then memory value will be stored at given address.
            mem[i] = memVal;
        }
        // If not all memory addresses listed, then set rest to zero.
        for (int i = memArrStr.length; i < memSize; i++) {
            mem[i] = 0;
        }
        return true;
    }

    /**
     * Returns state of the registers in the machine
     * This is one place to add a javadoc in your implementation.
     * Specifying in what order the register values are returned
     *
     * @return String array of register values
     */
    public String[] getProcessorState() {
        return new String[]{""};
    }

    public String getMemState() {
        String memArr = "";
        for (int i = 0; i < memSize; i++) {
            String memAdr = Integer.toHexString(i & 0xFF);
            if (memAdr.length() == 1) {
                memAdr = "0" + memAdr;
            }
            String memVal = Integer.toBinaryString(mem[i] & 0xFFF);
            memArr += memAdr + " " + memVal + "\n";
        }
        return memArr;
    }

    public String update() {
        // Fetch Instruction
        short instr = mem[PC];
        // Decode & Execute Instruction. Get which instruction was executed in assembly
        String instrAssembly = s12_lut(instr);
        // Save Trace and return instruction executed
        instrTrace += instrAssembly + "\n";
        return Integer.toBinaryString(instr & 0xFFF);
    }

    public boolean writeMem(String filename) {
        try {
            File file = new File(filename);
            file.createNewFile();

        } catch (IOException e) {
            System.err.println("writeMem ERR! Could not create file! " + e.getMessage());
            return false;
        }
        try (FileWriter writer = new FileWriter(filename)) {
            // First, write the PC and ACC
            String PCStr = Integer.toBinaryString(PC & 0xFF);
            String ACCStr = Integer.toBinaryString(ACC & 0xFFF);
            writer.write(PCStr + " " + ACCStr + "\n");
            // Next, write memory values
            writer.write(getMemState());
        } catch (IOException e) {
            System.err.println("writeMem ERR! Could not write to file! " + e.getMessage());
            return false;
        }
        // File written
        return true;
    }

    public boolean writeTrace(String filename) {
        try {
            File file = new File(filename);
            file.createNewFile();

        } catch (IOException e) {
            System.err.println("writeTrace ERR! Could not create file! " + e.getMessage());
            return false;
        }
        try (FileWriter writer = new FileWriter(filename)) {
            // Write the traced values
            writer.write(instrTrace);
        } catch (IOException e) {
            System.err.println("writeTrace ERR! Could not write to file! " + e.getMessage());
            return false;
        }
        // File written
        return true;
    }

    /**
     * Lookup table to figure out instruction (decode) and then execute instruction.
     *
     * @return String representation of the instruction executed (Assembly)
     */
    private String s12_lut(short instr) {
        String opcodeStr;
        byte opcode = (byte) (instr >> 8);
        byte x = (byte) (instr & 0xFF);
        switch (opcode) {
            // Jumps
            case 0b0000: {
                opcodeStr = "JMP";
                PC = x;
            }
            case 0b0001: {
                opcodeStr = "JN";
                String ACCStr = Integer.toBinaryString(ACC);
                if (ACCStr.charAt(ACCStr.length() - 12) == '1') {//Bit 12 is negative
                    PC = x;
                } else PC +=1;
            }
            case 0b0010: {
                opcodeStr = "JZ";
                if(ACC == 0) {
                    PC = x;
                } else PC +=1;
            }
            // Memory Access
            case 0b0100: {
                opcodeStr = "LOAD";
                ACC = mem[x];
                PC+=1;
            }
            case 0b0101: {
                opcodeStr = "STORE";
                mem[x] = ACC;
                PC+=1;
            }
            case 0b0110: {
                opcodeStr = "LOADI";
                byte val = (byte) (ACC & 0xFF); //Get 8 bits from ACC
                ACC = mem[val];
                PC +=1;
            }
            case 0b0111: {
                opcodeStr = "STOREI";
                byte val = (byte) (ACC & 0xFF); //Get 8 bits from ACC
                mem[val] = ACC;
                PC +=1;
            }
            // ALU
            case 0b1000: {
                opcodeStr = "AND";
                ACC = (short) (ACC & mem[x]);
                PC+=1;
            }
            case 0b1001: {
                opcodeStr = "OR";
                ACC = (short) (ACC | mem[x]);
                PC+=1;
            }
            case 0b1010: {
                opcodeStr = "ADD";
                ACC = (short) ((ACC + mem[x]) & 0xFFF);
                PC+=1;
            }
            case 0b1011: {
                opcodeStr = "SUB";
                String valStr = Integer.toBinaryString(mem[x]);
                String valTwosCompStr = "";
                for (int i = 0; i < 12; i++) {
                    if (valStr.charAt(valStr.length()-12+i) == 0) {
                        valTwosCompStr+="1";
                    } else valTwosCompStr+="0";
                }
                short valTwosComp = (short) Integer.parseInt(valTwosCompStr, 2);
                ACC = (short) ((ACC + valTwosComp) & 0xFFF);
                PC+=1;
            }
            // HALT
            case 0b1111: {
                opcodeStr = "HALT";
                isHalted = true;
            }
            default: {//Should never access! Will default with halt.
                opcodeStr = "HALT";
                System.err.println("ERROR! Invalid instruction! Program will immediately halt.");
                isHalted = true;
            }
        }

        // Return the trace
        if(isHalted) return opcodeStr;
        return opcodeStr + " " + x;
    }
}