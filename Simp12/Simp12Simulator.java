/**
 * Interface specifying required methods for Instruction level simulator for S12
 * ISA.
 * 
 * @author sfrost
 * @date Fall 2025
 */
public interface Simp12Simulator {
    /**
     * Reads in the plain text file and instantiates the memory array.
     * 
     * @param filename of memory to be read in
     * @return true if memory successfully parsed and instantiated
     */
    public boolean intializeMem(String filename);

    /**
     * Returns state of the registers in the machine. Specifying in what
     * order the register values are returned.
     * 
     * @return string array of register values
     */
    public String[] getProcessorState();

    /**
     * String form of the current state of the memory (e.g. each line is hex
     * address, space, binary word).
     * 
     * @return string representation of memory
     */
    public String getMemState();

    /**
     * Execute one cycle of the machine.
     * 
     * @return string representation of the instruction executed (binary)
     */
    public String update();

    /**
     * Write out the memFile associated with the current state of the simulation.
     * 
     * @param filename name of memFile to create
     * @return true if successful file creation, false otherwise
     */
    public boolean writeMem(String filename);

    /**
     * Write out the instructions executed up to this point.
     * 
     * @param filename name of trace file to create
     * @return true if successfully written, else false
     */
    public boolean writeTrace(String filename);
}
