package Simp12;

public class Mult2IntMem {
    static Instr[] instrs = {
            // START:
            // LOAD E2
            new Instr(InstrKind.LOAD, 0xE2),
            // SUB E2 // Create 0
            new Instr(InstrKind.SUB, 0xE2),
            // STORE FF // result <- 0
            new Instr(InstrKind.STORE, 0xFF),

            // 0x03 LOOP:
            // LOAD FE // A = multiplier
            new Instr(InstrKind.LOAD, 0xFE),
            // JZ DONE // if multiplier==0, its over
            new Instr(InstrKind.JZ, 0x0C),
            //
            // LOAD FF // A = result
            new Instr(InstrKind.LOAD, 0xFF),
            // ADD FD // A += multiplicand
            new Instr(InstrKind.ADD, 0xFD),
            // STORE FF // result <- A
            new Instr(InstrKind.STORE, 0xFF),

            // LOAD FE // A = multiplier
            new Instr(InstrKind.LOAD, 0xFE),
            // SUB E2 // A = multiplier - 1
            new Instr(InstrKind.SUB, 0xE2),
            // STORE FE // multiplier <- multiplier - 1
            new Instr(InstrKind.STORE, 0xFE),
            // JMP LOOP
            new Instr(InstrKind.JMP, 0x03),

            // 0x0C DONE:
            // HALT
            new Instr(InstrKind.LOAD, 0xE2),
    };

    /*
     * Generate a memFile for this instruction set.
     */
    public static void main(String[] args) {
        MemFileWriter.writeMemFile("bench/mult-2int-mem.memFile", instrs);
    }
}
