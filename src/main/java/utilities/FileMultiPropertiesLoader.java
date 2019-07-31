package utilities;



import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

public class FileMultiPropertiesLoader implements MultiPropertyLoader{


    private static final String DEFAULT_RELATIVE_DIR_PATH_DIRECTORY="confPath";
    private static final String DEFAULT_ABSOLUTE_DIRE_PATH_DIRECTORY="absoluteConfPath";
    private String propetiesDiretoryPath;
    private Function<File,String> bundleKeyExtractor;



    public FileMultiPropertiesLoader(Function<File,String> bundleKeyExtractor) {
        this.bundleKeyExtractor = bundleKeyExtractor;
        String absolutePath = System.getProperty("absoluteConfPath");
        if (StringUtils.isBlank(absolutePath)) {
            String relativePath = System.getProperty("confPath");
            if (StringUtils.isBlank(relativePath)){
                throw new IllegalArgumentException("Both relative and absolute System properties are not set. Please " +
                        "use -DabsoluteConfPath or -DconfPath");
            }
            this.propetiesDiretoryPath = System.getProperty("user.dir") + File.separator + relativePath;

        }else{
            this.propetiesDiretoryPath = absolutePath;
        }
    }

    @Override
    public Map<String, Properties> loadAndCheck(FilenameFilter var1, String... propertiesKeys) {

        Map<String,Properties> result = new HashMap<>();
        File propertiesDirectory = new File(this.propetiesDiretoryPath);

        File[] files = propertiesDirectory.listFiles(var1);

        if (ArrayUtils.isEmpty(files)){

            throw new IllegalArgumentException("no properties files found in the path " + this.propetiesDiretoryPath);
        }else{

            File[] var4 = files;

            for(int var3 =0 ; var3 < var4.length ; var3++){

                File file = var4[var3];
                String propertyBundelKeys = this.bundleKeyExtractor.apply(file);
                Properties properties = new Properties();
                try {
                    properties.load(new FileInputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.validateMandatory(file.getName(),properties,propertiesKeys);
                result.put(propertyBundelKeys,properties);
            }

        }

        return result;
    }


    public String getPath(){
        return this.propetiesDiretoryPath;
    }


    private void validateMandatory(String filename, Properties properties, String...propertykeys){
        String[] var1 = propertykeys;
        for (int var2 =0; var2 < var1.length; var2++){
            String propertKey = var1[var2];
            if (StringUtils.isBlank((String)properties.getProperty(propertKey))){
                throw new IllegalArgumentException(propertKey + "is undefined or empty in the file " + filename + " It's mandatory please set");
            }
        }

    }

}
