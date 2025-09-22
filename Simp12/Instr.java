package Simp12;

public class Instr {
    private InstrKind kind;
    private int addr;

    /*
     * Construct a new `Instr` from `kind` and `addr`.
     */
    public Instr(InstrKind kind, int addr) {
        this.kind = kind;
        this.addr = addr;
    }

    /*
     * Construct a new `Instr` from `bits`.
     *
     * The 12-bit word should be in the least significant bits of `bits`.
     *
     * E.g.
     *
     * JN 0b01001101
     * 0000 [0001] [01001101]
     * |pad||opco| | -addr- |
     */
    public Instr(short bits) {
        // first 8 bits
        addr = bits & 0xFF;

        // last 4 bits
        short op_bits = (short) (bits >> 8);
        int opcode = op_bits & 0b1111;

        for (InstrKind instrKind : InstrKind.values()) {
            if (instrKindBits(instrKind) == opcode) {
                kind = instrKind;
                break;
            }
        }
    }

    /*
     * Encode the instruction as a short.
     *
     * The 12-bit word is in the least significant bits of the returned short.
     *
     * E.g.
     *
     * JN 0b01001101
     * 0000 [0001] [01001101]
     * |pad||opco| | -addr- |
     */
    public short encode() {
        return (short) ((instrKindBits(kind) << 8) | addr);
    }

    private static int instrKindBits(InstrKind kind) {
        switch (kind) {
            case JMP: {
                return 0b0000;
            }
            case JN: {
                return 0b0001;

            }
            case JZ: {
                return 0b0010;

            }
            case LOAD: {
                return 0b0100;

            }
            case STORE: {
                return 0b0101;

            }
            case LOADI: {
                return 0b0110;

            }
            case STOREI: {
                return 0b0111;

            }
            case AND: {
                return 0b1000;

            }
            case OR: {
                return 0b1001;

            }
            case ADD: {
                return 0b1010;

            }
            case SUB: {
                return 0b1011;

            }
            case HALT: {
                return 0b1111;
            }
        }
        return 0xDEADBEEF;
    }
}
