import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.Assert;

/****************************************/
// TestCase 86: Verificar que un usuario puede registrarse a si mismo como cliente
//
// Paso 1. En el navbar seleccionar "Registrarse"
// Resultado Esperado: Se muestra el formulario para crear nueva cuenta
//
// Paso 2. Completar los campos de Nombre, Apellido, Correo Electrónico, Teléfono, 
//         Nombre de usuario, Contraseña y Confirmar contraseña
// Resultado Esperado: Los campos reflejan los datos ingresados, excepto contraseña que estará oculta
//
// Paso 3. Hacer click en "Crear Cuenta"
// Resultado Esperado: Aparece el mensaje "¡Cuenta creada exitosamente! Ya puedes iniciar sesión."
/****************************************/

// Para ejecutar: mvn clean test -Dtest=TC86_RegistroUsuarioCliente

public class TC86_RegistroUsuarioCliente extends BaseTest {
    
    @Test
    public void testRegistroUsuarioCliente() {
        
        /************** 1. Preparacion de la prueba ***********/
        System.out.println("=== INICIANDO TEST: Registro de Usuario Cliente ===");
        driver.get("http://localhost:5173/");
        System.out.println("Pagina cargada: " + driver.getTitle());
        
        /************** 2. Logica de la prueba ***************/
        
        // Paso 1. En el navbar seleccionar "Registrarse"
        System.out.println("--- Paso 1: Seleccionar Registrarse en el navbar ---");
        WebElement registroLink = driver.findElement(By.xpath("//a[contains(text(), 'Registrarse')]"));
        registroLink.click();
        System.out.println("Clic en Registrarse");
        
        // Esperar a que cargue la página de registro
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que estamos en la página de registro
        String urlActual = driver.getCurrentUrl();
        Assert.assertTrue(urlActual.contains("/register"), "Deberia estar en la pagina de registro");
        System.out.println("Resultado Paso 1: Se muestra formulario para crear nueva cuenta - OK");
        System.out.println("URL actual: " + urlActual);
        
        // Paso 2. Completar los campos del formulario
        System.out.println("--- Paso 2: Completar campos del formulario ---");
        
        // Generar datos únicos para evitar conflictos
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nombre = "Cliente" + timestamp;
        String apellido = "AutoRegistrado" + timestamp;
        String email = "cliente" + timestamp + "@gmail.com";
        String telefono = "777888999";
        String username = "cliente" + timestamp;
        String password = "Password123!";
        
        // Llenar campos del formulario
        driver.findElement(By.id("nombre")).sendKeys(nombre);
        driver.findElement(By.id("apellido")).sendKeys(apellido);
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("telefono")).sendKeys(telefono);
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("confirmPassword")).sendKeys(password);
        
        System.out.println("Campos completados:");
        System.out.println(" - Nombre: " + nombre);
        System.out.println(" - Apellido: " + apellido);
        System.out.println(" - Email: " + email);
        System.out.println(" - Teléfono: " + telefono);
        System.out.println(" - Username: " + username);
        System.out.println(" - Contraseña: Password123!");
        
        // Verificar que los campos de contraseña están ocultos (type="password")
        String tipoPassword = driver.findElement(By.id("password")).getAttribute("type");
        String tipoConfirmPassword = driver.findElement(By.id("confirmPassword")).getAttribute("type");
        Assert.assertEquals(tipoPassword, "password", "El campo contraseña deberia estar oculto");
        Assert.assertEquals(tipoConfirmPassword, "password", "El campo confirmar contraseña deberia estar oculto");
        System.out.println("Resultado Paso 2: Campos de contraseña ocultos - OK");
        
        // Pequeña pausa para ver los datos ingresados
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Paso 3. Hacer click en "Crear Cuenta"
        System.out.println("--- Paso 3: Hacer click en Crear Cuenta ---");
        WebElement botonCrearCuenta = driver.findElement(By.xpath("//button[.//span[contains(text(), 'Crear Cuenta')]]"));
        botonCrearCuenta.click();
        System.out.println("Clic en boton Crear Cuenta");
        
        // Esperar a que se procese el registro
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        /************** 3. Verificacion de la situacion esperada ***************/
        
        // Verificación: SweetAlert de éxito
        System.out.println("--- Verificacion: SweetAlert de exito ---");
        WebElement sweetAlert = driver.findElement(By.cssSelector(".swal2-popup.swal2-icon-success"));
        boolean alertVisible = sweetAlert.isDisplayed();
        
        WebElement alertTitle = driver.findElement(By.cssSelector(".swal2-title"));
        String titulo = alertTitle.getText();
        
        WebElement alertContent = driver.findElement(By.cssSelector(".swal2-html-container"));
        String contenido = alertContent.getText();
        
        System.out.println("SweetAlert visible? " + alertVisible);
        System.out.println("Titulo: " + titulo);
        System.out.println("Contenido: " + contenido);
        
        // Verificación 1: El SweetAlert debe estar visible y ser de éxito
        Assert.assertTrue(alertVisible, "Deberia mostrarse SweetAlert de exito");
        Assert.assertEquals(titulo, "Éxito", "El titulo deberia ser 'Éxito'");
        
        // Verificación 2: El mensaje debe ser exactamente "¡Cuenta creada exitosamente! Ya puedes iniciar sesión."
        Assert.assertEquals(contenido, "¡Cuenta creada exitosamente! Ya puedes iniciar sesión.", 
                          "El mensaje deberia ser '¡Cuenta creada exitosamente! Ya puedes iniciar sesión.'");
        
        System.out.println("Resultado Paso 3: Cuenta creada exitosamente - OK");
        
        /************** 4. Resultado final ***************/
        System.out.println("TEST EXITOSO: Registro de usuario cliente completado correctamente");
        System.out.println("Usuario cliente creado: " + nombre + " " + apellido + " (" + email + ")");
        System.out.println("Mensaje de exito mostrado correctamente: '¡Cuenta creada exitosamente! Ya puedes iniciar sesión.'");
    }
}