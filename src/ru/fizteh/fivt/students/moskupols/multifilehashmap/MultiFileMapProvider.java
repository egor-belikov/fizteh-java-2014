package ru.fizteh.fivt.students.moskupols.multifilehashmap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by moskupols on 23.10.14.
 */
public class MultiFileMapProvider {
    private final Path rootPath;
    private Map<String, MultiFileMap> tablesIssued;
    private Map<String, Integer> issueCount;

    public MultiFileMapProvider(Path rootPath) throws IOException {
        this.rootPath = rootPath;
        tablesIssued = new HashMap<>();
        issueCount = new HashMap<>();

        if (!Files.exists(rootPath)) {
            throw new FileNotFoundException(String.format("DB directory %s does not exist", rootPath));
        }
        if (!Files.isDirectory(rootPath)) {
            throw new IOException(String.format("%s is not directory", rootPath));
        }
    }

    public MultiFileMap getTable(String name) throws IOException {
        MultiFileMap map = tablesIssued.get(name);
        final Integer oldCount = issueCount.getOrDefault(name, 0);
        if (map == null) {
            map = openTable(name);
            if (map == null) {
                return null;
            }
            tablesIssued.put(name, map);
        }
        issueCount.put(name, oldCount + 1);
        return map;
    }

    public MultiFileMap createTable(String name) throws IOException {
        final Path tablePath = rootPath.resolve(name);
        if (Files.exists(tablePath)) {
            return null;
        }
        try {
            Files.createDirectory(tablePath);
        } catch (IOException e) {
            throw new IOException(String.format("Couldn't create %s", tablePath), e);
        }
        final MultiFileMap newMap = new MultiFileMap(tablePath);
        tablesIssued.put(name, newMap);
        issueCount.put(name, 1);
        return newMap;
    }

    public boolean removeTable(String name) throws IOException {
        if (tablesIssued.containsKey(name)) {
            throw new IllegalStateException(String.format("%s is not fully released yet", name));
        }
        final MultiFileMap table = openTable(name);
        if (table != null) {
            table.clear();
            final Path tablePath = rootPath.resolve(name);
            try {
                Files.delete(tablePath);
            } catch (IOException e) {
                throw new IOException("Couldn't delete " + tablePath, e);
            }
            return true;
        }
        return false;
    }

    public List<String> listNames() {
        return Arrays.asList(rootPath.toFile().list());
    }

    private MultiFileMap openTable(String name) throws IOException {
        final Path tablePath = rootPath.resolve(name);
        if (!Files.exists(tablePath)) {
            return null;
        }
        return new MultiFileMap(tablePath);
    }

    public void releaseTable(MultiFileMap table) throws IOException {
        final String name = table.getName();
        final Integer count = issueCount.get(name);
        if (count == null) {
            throw new IllegalStateException(String.format("%s wasn't issued", name));
        }
        if (count == 1) {
            table.flush();
            tablesIssued.remove(name);
            issueCount.remove(name);
        }
        issueCount.put(name, count - 1);
    }
}
