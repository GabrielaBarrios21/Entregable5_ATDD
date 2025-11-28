import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.Assert;

/****************************************/
// TestCase 1: Verificar que el inicio de sesión con credenciales válidas sea exitoso
//
// Paso 1. Hacer clic en "Iniciar Sesión"
// Paso 2. Completar el campo de usuario con: admin
// Paso 3. Completar el campo de contraseña con: 123456
// Paso 4. Hacer clic en el botón "Iniciar Sesión"
//
// Resultado Esperado: Debe mostrarse el mensaje "Sesión iniciada correctamente" en un SweetAlert
/****************************************/

// Para ejecutar: mvn clean test -Dtest=TC1_InicioSesionExitoso

public class TC1_InicioSesionExitoso extends BaseTest {
    
    @Test
    public void testInicioSesionExitoso() {
        
        /************** 1. Preparacion de la prueba ***********/
        System.out.println("=== INICIANDO TEST: Inicio de Sesion Exitoso ===");
        driver.get("http://localhost:5173/");
        System.out.println("Pagina cargada: " + driver.getTitle());
        
        /************** 2. Logica de la prueba ***************/
        
        // Paso 1. Hacer clic en el enlace "Iniciar Sesión"
        WebElement loginLink = driver.findElement(By.xpath("//a[contains(text(), 'Iniciar Sesión')]"));
        loginLink.click();
        System.out.println("Clic en Iniciar Sesion - Redirigiendo a pagina de login...");
        
        // Esperar a que cargue la página de login
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que estamos en la página de login
        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL actual: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("/login"), "Deberia estar en la pagina de login");
        
        // Paso 2. Completar el campo de usuario
        WebElement campoUsuario = driver.findElement(By.id("username"));
        campoUsuario.sendKeys("admin");
        System.out.println("Usuario ingresado: admin");
        
        // Paso 3. Completar el campo de contraseña  
        WebElement campoPassword = driver.findElement(By.id("password"));
        campoPassword.sendKeys("123456");
        System.out.println("Contraseña ingresada: 123456");
        
        // Pequeña pausa para ver la interacción
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Paso 4. Hacer clic en el botón "Iniciar Sesión"
        WebElement botonLogin = driver.findElement(By.xpath("//button//span[contains(text(), 'Iniciar Sesión')]"));
        botonLogin.click();
        System.out.println("Clic en boton Iniciar Sesion");
        
        // Esperar a que aparezca el SweetAlert
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        /************ 3. Verificacion de la situacion esperada ***************/
        
        // Verificar que se muestra el SweetAlert de éxito
        WebElement sweetAlert = driver.findElement(By.cssSelector(".swal2-popup"));
        boolean alertVisible = sweetAlert.isDisplayed();
        
        // Verificar el título del alert
        WebElement alertTitle = driver.findElement(By.cssSelector(".swal2-title"));
        String titulo = alertTitle.getText();
        
        // Verificar el contenido del alert  
        WebElement alertContent = driver.findElement(By.cssSelector(".swal2-html-container"));
        String contenido = alertContent.getText();
        
        System.out.println("SweetAlert visible? " + alertVisible);
        System.out.println("Titulo del alert: " + titulo);
        System.out.println("Contenido del alert: " + contenido);
        
        // Verificación 1: El SweetAlert debe estar visible
        Assert.assertTrue(alertVisible, "El SweetAlert deberia mostrarse despues del login");
        
        // Verificación 2: Debe contener el mensaje de éxito (CON CARACTERES CORRECTOS)
        boolean contieneMensajeExito = titulo.contains("Éxito") || contenido.contains("Sesión iniciada correctamente");
        Assert.assertTrue(contieneMensajeExito, 
                         "Debe contener el mensaje de exito. Titulo: '" + titulo + "', Contenido: '" + contenido + "'");
        
        System.out.println("TEST EXITOSO: Inicio de sesion completado correctamente");
        System.out.println("SweetAlert de exito mostrado correctamente");
    }
}