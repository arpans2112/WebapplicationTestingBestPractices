package Tests.individual.feature;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utilities.WebDriverUtils;

import java.io.File;
import java.util.Properties;

public class CallingBrowser {

    public static WebDriver driver ;

    @BeforeTest
    public static void init(){

        driver = WebDriverUtils.ui().driver("chrome");

    }

    @Test
    public static void driverInitialization(){



    }

}
