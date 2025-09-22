package Simp12;

import java.io.FileWriter;
import java.io.IOException;

public class MemFileWriter {
    /*
     * Write a memFile for `instrs` to `path`. Initializes the program counter
     * and accumulator to 0.
     */
    public static void writeMemFile(String path, Instr[] instrs) {
        MemFileWriter.writeMemFile(path, instrs, 0, 0);
    }

    /*
     * Write a memFile for `instrs` to `path`. Initializes the program counter
     * to `pc` and the accumulator with `accum`.
     */
    public static void writeMemFile(String path, Instr[] instrs, int pc, int accum) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(nLeastSignificantBits(pc, 8)
                    + " "
                    + nLeastSignificantBits(accum, 12)
                    + "\n");
            int i = 0;
            for (Instr instr : instrs) {
                String addr = Integer.toHexString(i);
                if (i < 16) {
                    addr = String.format("0%s", addr);
                }
                writer.write(addr
                        + " "
                        + nLeastSignificantBits((int) instr.encode(), 12)
                        + "\n");
                i += 1;
            }
        } catch (IOException e) {
            System.err.println("error writing to file: " + e.getMessage());
        }
    }

    private static String nLeastSignificantBits(int number, int n) {
        StringBuilder result = new StringBuilder();
        for (int i = n - 1; i >= 0; i--) {
            int bit = (number >> i) & 1;
            result.append(bit);
        }
        return result.toString();
    }
}
