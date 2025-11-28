import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.Assert;

/****************************************/
// TestCase 2: Inicio de Sesión con Credenciales Inválidas
// 
// Prueba de Aceptacion: Verificar que no es posible iniciar sesión cuando se ingresan credenciales inválidas
//
// Paso 1. Hacer clic en "Iniciar Sesión"
// Paso 2. Completar el campo de usuario con: admin
// Paso 3. Completar el campo de contraseña con: passwordIncorrecta
// Paso 4. Hacer clic en el botón "Iniciar Sesión"
//
// Resultado Esperado: Debe mostrarse el mensaje "Error" con "Usuario o contraseña incorrectos" en un SweetAlert con icono de error
/****************************************/

// Para ejecutar: mvn clean test -Dtest=TC2_InicioSesionCredencialesInvalidas

public class TC2_InicioSesionCredencialesInvalidas extends BaseTest {
    
    @Test
    public void testInicioSesionCredencialesInvalidas() {
        
        /************** 1. Preparacion de la prueba ***********/
        System.out.println("=== INICIANDO TEST: Inicio de Sesion con Credenciales Invalidas ===");
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
        
        // Paso 2. Completar el campo de usuario con usuario válido
        WebElement campoUsuario = driver.findElement(By.id("username"));
        campoUsuario.sendKeys("admin");
        System.out.println("Usuario ingresado: admin");
        
        // Paso 3. Completar el campo de contraseña con contraseña incorrecta
        WebElement campoPassword = driver.findElement(By.id("password"));
        campoPassword.sendKeys("passwordIncorrecta");
        System.out.println("Contraseña ingresada: passwordIncorrecta");
        
        // Pequeña pausa para ver la interacción
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Paso 4. Hacer clic en el botón "Iniciar Sesión"
        WebElement botonLogin = driver.findElement(By.xpath("//button//span[contains(text(), 'Iniciar Sesión')]"));
        botonLogin.click();
        System.out.println("Clic en boton Iniciar Sesion con credenciales invalidas");
        
        // Esperar a que aparezca el SweetAlert de error
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        /************ 3. Verificacion de la situacion esperada ***************/
        
        // Verificar que se muestra el SweetAlert de error
        WebElement sweetAlert = driver.findElement(By.cssSelector(".swal2-popup.swal2-icon-error"));
        boolean alertVisible = sweetAlert.isDisplayed();
        
        // Verificar el título del alert - debe ser "Error"
        WebElement alertTitle = driver.findElement(By.cssSelector(".swal2-title"));
        String titulo = alertTitle.getText();
        
        // Verificar el contenido del alert - debe ser "Usuario o contraseña incorrectos"  
        WebElement alertContent = driver.findElement(By.cssSelector(".swal2-html-container"));
        String contenido = alertContent.getText();
        
        System.out.println("SweetAlert visible? " + alertVisible);
        System.out.println("Titulo del alert: " + titulo);
        System.out.println("Contenido del alert: " + contenido);
        
        // Verificación 1: El SweetAlert debe estar visible y tener la clase de error
        Assert.assertTrue(alertVisible, "El SweetAlert de error deberia mostrarse despues del intento fallido de login");
        
        // Verificación 2: El título debe ser exactamente "Error"
        Assert.assertEquals(titulo, "Error", "El titulo del alert debe ser 'Error'");
        
        // Verificación 3: El contenido debe ser exactamente "Usuario o contraseña incorrectos"
        Assert.assertEquals(contenido, "Usuario o contraseña incorrectos", 
                          "El contenido del alert debe ser 'Usuario o contraseña incorrectos'");
        
        // Verificación 4: Verificar que el SweetAlert tiene la clase de icono error
        boolean tieneClaseError = sweetAlert.getAttribute("class").contains("swal2-icon-error");
        Assert.assertTrue(tieneClaseError, "El SweetAlert debe tener la clase swal2-icon-error");
        
        // Verificación adicional: El usuario NO debería ser redirigido a la página principal
        String urlDespuesLogin = driver.getCurrentUrl();
        boolean sigueEnLogin = urlDespuesLogin.contains("/login") || !urlDespuesLogin.contains("/dashboard");
        Assert.assertTrue(sigueEnLogin, "El usuario deberia permanecer en la pagina de login tras credenciales invalidas");
        
        System.out.println("TEST EXITOSO: Verificacion de credenciales invalidas completada correctamente");
        System.out.println("SweetAlert de error mostrado correctamente con mensajes exactos");
        System.out.println("Usuario permanece en pagina de login: " + sigueEnLogin);
    }
}