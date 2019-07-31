package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WebDriverUtils {


    public static final String DRIVER_TARGET_URL="driver.target.url";
    public static final String DRIVER_TYPE="driver.type";
    public static final String SCREEN_SHOT_DIRECTORY="report.image.dir";
    public static final String DRIVER_LOCATION="driver.location";
    public static final String DRIVER_TYPE_CHROME="chrome";
    public static final String DRIVER_TYPE_FIREFOX="firefox";
    public static final String DRIVER_TYPE_ELECTRON="electrone";
    public static final String DRIVER_TYPE_IE_EXPLORER="iexplorer";
    public static final String DRIVER_TYPE_MOBILE="mobile";
    public static final String ELECTRON_APP_PATH="electron.app.path";
    private static final long defaultExplicitWaitTimeout= 10L;
    private static final long defaultImplicitWaitTimeout= 10L;
    private Map<String , Properties> propertiesBundles = null;
    public static String workingDirectory;
    private static WebDriverUtils instance = null ;
    protected WebDriver currentDriver = null;

       private WebDriverUtils()  {

        MultiPropertyLoader multiPropertyLoader = new FileMultiPropertiesLoader(file -> {
            return file.getName().split("-")[0];
        });

      this.propertiesBundles =  multiPropertyLoader.loadAndCheck((d,name) ->{
            return name.endsWith("-ui.properties");
        }, new String[]{DRIVER_TYPE});
    }

     public static synchronized WebDriverUtils ui(){
           if (instance == null){
               instance = new WebDriverUtils();
           }
           return instance;
     }

     public WebDriver driver(String configruationType){
            this.currentDriver = this.buildDriverWith(configruationType);
            return this.currentDriver;
     }

     private WebDriver buildDriverWith(String configurationType){
           Properties p = this.propertiesBundles.get(configurationType);
           CreateScreenshotDirectory(p);
           String driverType = p.getProperty("driver.type");
           WebDriver buildDriver = null;
           if (StringUtils.equalsIgnoreCase(driverType,"firefox")){
               buildDriver = this.buildFirefoxDriver(p);
           }else if (StringUtils.equalsIgnoreCase(driverType,"chrome")){
               buildDriver = this.buildChromeDriver(p);
           }else if(StringUtils.equalsIgnoreCase(driverType,"iexplorer")){
               buildDriver = this.buildIEDriver(p);
         }


           String targetUrl = p.getProperty(DRIVER_TARGET_URL);
           buildDriver.get(targetUrl);
           buildDriver.manage().window().maximize();
           buildDriver.manage().timeouts().implicitlyWait(defaultImplicitWaitTimeout, TimeUnit.SECONDS);

          return  buildDriver;
       }

       private WebDriver buildFirefoxDriver(Properties properties){
           WebDriverManager.firefoxdriver().setup();
           return new FirefoxDriver();
       }

    private WebDriver buildChromeDriver(Properties properties){
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver();
    }

    private WebDriver buildIEDriver(Properties properties){
        WebDriverManager.iedriver().setup();
        return new InternetExplorerDriver();
    }



     private void CreateScreenshotDirectory(Properties p){
        workingDirectory =    System.getProperty("user.dir");
       StringBuilder sb = new StringBuilder();
       String screenshotDir = p.getProperty(SCREEN_SHOT_DIRECTORY);
       sb.append(workingDirectory).append(File.separator).append("target").append(File.separator).append(screenshotDir);

         try {
             FileUtils.forceMkdir(new File(sb.toString()));
             System.setProperty(SCREEN_SHOT_DIRECTORY,sb.toString());
         } catch (IOException e) {
             e.printStackTrace();
         }

     }

}
