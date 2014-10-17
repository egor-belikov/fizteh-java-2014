package ru.fizteh.fivt.students.ZatsepinMikhail.MultiFileHashMap;

import ru.fizteh.fivt.students.ZatsepinMikhail.FileMap.Command;
import ru.fizteh.fivt.students.ZatsepinMikhail.shell.CommandRm;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by mikhail on 17.10.14.
 */
public class CommandDrop extends CommandMultiFileHashMap {
    public CommandDrop() {
        name = "drop";
        numberOfArguments = 2;
    }

    @Override
    public boolean run(MFileHashMap myDataBase, String[] args) {
        if (numberOfArguments != args.length) {
            System.out.println(name + ": wrong number of arguments");
            return false;
        }
        CommandRm myRemover = new CommandRm();
        Path PathForRemoveTable = Paths.get(myDataBase.getDataBaseDirectory(), args[1]);
        if (!Files.exists(PathForRemoveTable)) {
            System.out.println(args[1] + " not exists");
            return true;
        }
        String[] argsArray = {
                "rm",
                "-r",
                PathForRemoveTable.toString()
        };
        if (myRemover.run(argsArray)) {
            System.out.println("dropped");
            return true;
        } else {
            System.err.println(name + " : error while removing table's directory");
            return false;
        }
    }
}
