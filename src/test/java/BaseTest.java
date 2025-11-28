import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
    protected WebDriver driver;
    
    @BeforeTest
    public void setDriver() throws Exception {
        // WebDriverManager automático - sin rutas manuales
        WebDriverManager.chromedriver().setup();
        
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
    
    @AfterTest
    public void closeDriver() throws Exception {
        if (driver != null) {
            driver.quit();
        }
    }
}