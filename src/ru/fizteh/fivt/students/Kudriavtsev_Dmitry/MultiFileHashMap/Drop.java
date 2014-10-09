package ru.fizteh.fivt.students.Kudriavtsev_Dmitry.MultiFileHashMap;

import java.nio.file.Files;

/**
 * Created by Дмитрий on 07.10.14.
 */
public class Drop extends Command {

    public Drop() {
        super("drop", 1);
    }

    @Override
    public boolean exec(Connector dbConnector, String[] args) {
        if (args.length != argLen) {
            System.err.println("Incorrect number of arguments in " + name);
            return false;
        }

        MFHMap map = dbConnector.tables.get(args[0]);
        if (map == null) {
            System.out.println(args[0] + " not exists");
            return false;
        }
        if (dbConnector.activeTable == map) {
            dbConnector = null;
        }
        try {
            assert dbConnector != null;
            dbConnector.tables.remove(args[0]);
            map.clear();
            Files.delete(map.dbPath);
        } catch (Exception e) {
            System.err.println("Exception in drop: " + e.getMessage());
            System.exit(-1);
        }
        System.out.println("dropped");
        return true;
    }
}
