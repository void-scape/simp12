package Simp12;

public class Instr {
    public InstrKind kind;
    public short arg;

    /*
     * Construct a new `Instr` from `bits`.
     *
     * The 12-bit word should be in the least significant bits of `bits`.
     *
     * E.g.
     *
     * JN 0b01001101
     * 0000 [0001] [01001101]
     * |pad||opco| | -argu- |
     */
    public Instr(short bits) {
        // first 8 bits
        arg = (short) (bits & 0xFF);

        // last 4 bits
        short op_bits = (short) (bits >> 8);
        int opcode = op_bits & 0b1111;

        switch (opcode) {
            case 0b0000: {
                kind = InstrKind.JMP;
                break;
            }
            case 0b0001: {
                kind = InstrKind.JN;
                break;
            }
            case 0b0010: {
                kind = InstrKind.JZ;
                break;
            }
            case 0b0100: {
                kind = InstrKind.LOAD;
                break;
            }
            case 0b0101: {
                kind = InstrKind.STORE;
                break;
            }
            case 0b1000: {
                kind = InstrKind.AND;
                break;
            }
            case 0b1001: {
                kind = InstrKind.OR;
                break;
            }
            case 0b1010: {
                kind = InstrKind.ADD;
                break;
            }
            case 0b1011: {
                kind = InstrKind.SUB;
                break;
            }
            case 0b1111: {
                kind = InstrKind.HALT;
                break;
            }
            default:
                // TOOD: something
        }
    }
}
