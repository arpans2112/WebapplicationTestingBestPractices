package utilities;

import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public interface MultiPropertyLoader {

    Map<String, Properties> loadAndCheck(FilenameFilter var1, String...var2);
}
