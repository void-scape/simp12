package Simp12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HexFormat;
import java.util.Optional;

public class S12_Sim {
    static class Config {
        String outputFileBaseName = "S12Sim";
        boolean limitCycles = false;
        int cyclesToExecute = 0;
    }

    static String cmdArgs = "<memFile> <optional: -o outputFileBaseName> <optional: -c cyclesToExecute>";

    public static void main(String[] args) {
        if (args.length == 0 || args.length > 5) {
            System.out.println("invalid arguments, expected " + cmdArgs);
            return;
        }

        String memFile = args[0];
        Config config = new Config();

        if (args.length > 1) {
            if (args.length != 3 && args.length != 5) {
                System.out.println("invalid arguments, expected " + cmdArgs);
                return;
            }

            if (args.length >= 3) {
                parseArg(1, args, config);
            }

            if (args.length >= 5) {
                parseArg(3, args, config);
            }
        }

        try {
            String content = Files.readString(Paths.get(memFile));
            Optional<String> firstLine = content.lines().findFirst();
            if (firstLine.isPresent()) {
                String[] accum_pc = firstLine.get().split(" ");
                if (accum_pc.length == 2) {
                    S12_IL il = new S12_IL(config.outputFileBaseName, Short.parseShort(accum_pc[0]),
                            Byte.parseByte(accum_pc[1]));
                    int splitAt = content.indexOf('\n');
                    il.intializeMem(content.substring(splitAt + 1));

                    int cycleCount = 0;
                    while (!il.isHalted || (config.limitCycles && config.cyclesToExecute < cycleCount)) {
                        il.update();
                        cycleCount += 1;
                    }

                    il.writeMem(config.outputFileBaseName + "_memOut");
                    il.writeTrace(config.outputFileBaseName + "_trace");

                    System.out.println("Cycles Executed: " + cycleCount);
                    HexFormat fmt = HexFormat.of();
                    System.out.println("PC: " + fmt.toHexDigits(il.PC));
                    System.out.println("ACC: " + fmt.toHexDigits(il.ACC));
                } else {
                    System.out.println("bad memFile");
                }
            } else {
                System.out.println("bad memFile");
            }

        } catch (IOException e) {
            System.out.println("error reading memFile: " + e.getMessage());
        }
    }

    static void parseArg(int index, String[] args, Config config) {
        switch (args[index]) {
            case "-o": {
                config.outputFileBaseName = args[index + 1];
                break;
            }
            case "-c": {
                config.cyclesToExecute = Integer.parseInt(args[index + 1]);
                config.limitCycles = true;
                break;
            }
            default: {
                System.out.println("invalid arguments, expected " + cmdArgs);
                return;
            }
        }
    }
}
