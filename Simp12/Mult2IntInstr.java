package Simp12;

public class Mult2IntInstr {
    static Instr[] instrs = {
            // START:
            // LOAD FD // A ← M[FD] (multiplicand)
            new Instr(InstrKind.LOAD, 0xFD),
            // STORE F0 // F0 ← multiplicand
            new Instr(InstrKind.STORE, 0xF0),

            // LOAD FE // A ← M[FE] (multiplier)
            new Instr(InstrKind.LOAD, 0xFE),
            // STORE F3 // F3 ← multiplier (kept constant)
            new Instr(InstrKind.STORE, 0xF3),

            // LOADI 00 // A ← 0
            new Instr(InstrKind.LOADI, 0x00),
            // STORE FF // result ← 0
            new Instr(InstrKind.STORE, 0xFF),

            // LOADI 01 // A ← 1
            new Instr(InstrKind.LOADI, 0x01),
            // STORE F1 // mask ← 1 (tests bit0 first)
            new Instr(InstrKind.STORE, 0xF1),

            // LOADI 08 // A ← 8
            new Instr(InstrKind.LOADI, 0x08),
            // STORE F2 // counter ← 8 iterations (one per bit)
            new Instr(InstrKind.STORE, 0xF2),

            // LOADI 01 // A ← 1
            new Instr(InstrKind.LOADI, 0x01),
            // STORE F4 // F4 holds the constant 1 for SUB
            new Instr(InstrKind.STORE, 0xF4),

            // Main loop: 8 passes for 8 bits
            // 0x0C LOOP:
            // LOAD F3 // A ← multiplier
            new Instr(InstrKind.LOAD, 0xF3),
            // AND F1 // A ← multiplier & mask
            new Instr(InstrKind.AND, 0xF1),
            // JZ SKIP_ADD // if that bit is 0, skip accumulation
            new Instr(InstrKind.JZ, 0x1C),

            // LOAD FF // A ← result
            new Instr(InstrKind.LOAD, 0xFF),
            // ADD F0 // A ← result + multiplicand (current partial)
            new Instr(InstrKind.ADD, 0xF0),
            // STORE FF // result ← A
            new Instr(InstrKind.STORE, 0xFF),

            // 0x1C SKIP_ADD:
            // // Advance to next bit by doubling multiplicand and mask
            // LOAD F0 // A ← multiplicand
            new Instr(InstrKind.LOAD, 0xF0),
            // ADD F0 // A ← multiplicand * 2
            new Instr(InstrKind.ADD, 0xF0),
            // STORE F0 // multiplicand ← multiplicand << 1 (via add)
            new Instr(InstrKind.STORE, 0xF0),

            // LOAD F1 // A ← mask
            new Instr(InstrKind.LOAD, 0xF1),
            // ADD F1 // A ← mask * 2
            new Instr(InstrKind.ADD, 0xF1),
            // STORE F1 // mask ← mask << 1
            new Instr(InstrKind.STORE, 0xF1),

            // // Decrement counter and loop if not zero
            // LOAD F2 // A ← counter
            new Instr(InstrKind.LOAD, 0xF2),
            // SUB F4 // A ← counter - 1
            new Instr(InstrKind.SUB, 0xF4),
            // STORE F2 // counter ← A
            new Instr(InstrKind.STORE, 0xF2),
            // JZ DONE // if counter == 0, exit
            new Instr(InstrKind.JZ, 0x28),
            // JMP LOOP
            new Instr(InstrKind.JMP, 0x0C),

            // 0x28 DONE:
            // HALT // product low byte now in [FF]
            new Instr(InstrKind.HALT, 0x00)
    };

    /*
     * Generate a memFile for this instruction set.
     */
    public static void main(String[] args) {
        MemFileWriter.writeMemFile("bench/mult-2int-instr.memFile", instrs);
    }
}
