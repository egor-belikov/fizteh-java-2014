package ru.fizteh.fivt.students.valentine_lebedeva.multifilehashmap.filemap.Cmd;

import ru.fizteh.fivt.students.valentine_lebedeva.multifilehashmap.MultiFileHashMapManager;

public class PutCommand extends FileMapCommand {
    public final void execute(final String[] args,
            final MultiFileHashMapManager parser) {
        if (checkWorkTable(parser.getWorkTable())) {
            checkArgs(3, args);
            if (parser.getWorkTable().getBase().get(args[1]) != null) {
                System.out.println("overwrite");
                System.out.println(args[2]);
            } else {
                System.out.println("new");
            }
            parser.getWorkTable().putBase(args[1], args[2]);
        }
    }
}
