import java.io.InputStream;
import java.util.Properties;

public class Configuracion {
    private static Properties propiedades = new Properties();
    
    static {
        try {
            InputStream entrada = Configuracion.class.getClassLoader().getResourceAsStream("config.properties");
            if (entrada != null) {
                propiedades.load(entrada);
            } else {
                // Valores por defecto si no encuentra el archivo
                propiedades.setProperty("chrome.driver.path", "C\\:\\\\Users\\\\USUARIO\\\\Downloads\\\\chromedriver-win64 (1)\\\\chromedriver-win64\\\\chromedriver.exe");
                propiedades.setProperty("base.url", "https://www.google.com");
                propiedades.setProperty("timeout.seconds", "10");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String getChromeDriverPath() {
        return propiedades.getProperty("chrome.driver.path");
    }
    
    public static String getBaseUrl() {
        return propiedades.getProperty("base.url");
    }
    
    public static int getTimeout() {
        return Integer.parseInt(propiedades.getProperty("timeout.seconds"));
    }
}