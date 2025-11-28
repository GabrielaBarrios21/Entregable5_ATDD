import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;
import org.testng.Assert;

/****************************************/
// TestCase 85: Verificar que el sistema no permite crear usuarios con usernames ya registrados
//
// Paso 1. Acceder al módulo de Gestión de Usuarios, "Usuarios"
// Resultado Esperado: Se muestra la lista de usuarios registrados
//
// Paso 2. Hacer clic en el botón "Nuevo Usuario"
// Resultado Esperado: Se despliega un formulario de registro en una nueva página
//
// Paso 3. Completar los campos con datos nuevos excepto por el username, donde se usará un username ya registrado
// Paso 4. Hacer clic en "Crear Usuario"
// Resultado Esperado: El sistema muestra un mensaje de error "El username ya está en uso"
/****************************************/

// Para ejecutar: mvn clean test -Dtest=TC85_VerificarUsernameDuplicado

public class TC85_VerificarUsernameDuplicado extends BaseTest {
    
    @Test
    public void testVerificarUsernameDuplicado() {
        
        /************** 1. Preparacion de la prueba - Login como Admin ***********/
        System.out.println("=== INICIANDO TEST: Verificacion de Username Duplicado ===");
        
        // Primero hacer login como administrador
        System.out.println("--- Preparacion: Login como Administrador ---");
        driver.get("http://localhost:5173/");
        
        // Ir a la página de login
        WebElement loginLink = driver.findElement(By.xpath("//a[contains(text(), 'Iniciar Sesión')]"));
        loginLink.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Completar credenciales de admin
        WebElement campoUsuario = driver.findElement(By.id("username"));
        WebElement campoPassword = driver.findElement(By.id("password"));
        campoUsuario.sendKeys("admin");
        campoPassword.sendKeys("123456");
        
        WebElement botonLogin = driver.findElement(By.xpath("//button//span[contains(text(), 'Iniciar Sesión')]"));
        botonLogin.click();
        
        // Esperar login exitoso
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Cerrar el SweetAlert de login exitoso
        WebElement botonAceptar = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
        botonAceptar.click();
        
        System.out.println("Login como administrador exitoso");
        
        /************** 2. Logica de la prueba ***************/
        
        // Paso 1. Acceder al módulo de Gestión de Usuarios, "Usuarios"
        System.out.println("--- Paso 1: Acceder a Gestión de Usuarios ---");
        WebElement menuUsuarios = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Usuarios')]]"));
        menuUsuarios.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que se muestra la lista de usuarios
        boolean listaUsuariosVisible = driver.findElements(By.xpath("//table")).size() > 0;
        Assert.assertTrue(listaUsuariosVisible, "Deberia mostrarse la lista de usuarios registrados");
        System.out.println("Resultado Paso 1: Se muestra lista de usuarios - OK");
        
        // Paso 2. Hacer clic en el botón "Nuevo Usuario"
        System.out.println("--- Paso 2: Hacer clic en Nuevo Usuario ---");
        WebElement botonNuevoUsuario = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Nuevo Usuario')]]"));
        botonNuevoUsuario.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que estamos en la página de crear usuario
        String urlActual = driver.getCurrentUrl();
        Assert.assertTrue(urlActual.contains("/gestion-usuarios/crear"), "Deberia estar en la pagina de crear usuario");
        System.out.println("Resultado Paso 2: Se despliega formulario de registro - OK");
        System.out.println("URL actual: " + urlActual);
        
        // Paso 3. Completar los campos con datos nuevos excepto por el username
        System.out.println("--- Paso 3: Completar formulario con username duplicado ---");
        
        // Generar datos únicos para evitar conflictos, excepto username
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nombre = "UsuarioDuplicado" + timestamp;
        String apellido = "Apellido" + timestamp;
        String email = "nuevo" + timestamp + "@gmail.com"; // Email nuevo
        String username = "nuevo"; // Username ya registrado
        String telefono = "555123456";
        String password = "123456";
        
        // Llenar campos del formulario
        driver.findElement(By.id("nombre")).sendKeys(nombre);
        driver.findElement(By.id("apellido")).sendKeys(apellido);
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("telefono")).sendKeys(telefono);
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("confirmPassword")).sendKeys(password);
        
        // Seleccionar un rol (CLIENTE)
        Select selectRol = new Select(driver.findElement(By.id("rolId")));
        selectRol.selectByVisibleText("CLIENTE");
        
        System.out.println("Campos completados:");
        System.out.println(" - Nombre: " + nombre);
        System.out.println(" - Apellido: " + apellido);
        System.out.println(" - Email: " + email);
        System.out.println(" - Username (DUPLICADO): " + username);
        System.out.println(" - Rol: CLIENTE");
        
        // Pequeña pausa para ver los datos ingresados
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Paso 4. Hacer clic en el botón "Crear Usuario"
        System.out.println("--- Paso 4: Hacer clic en Crear Usuario ---");
        WebElement botonCrearUsuario = driver.findElement(By.xpath("//button[.//span[contains(text(), 'Crear Usuario')]]"));
        botonCrearUsuario.click();
        
        // Esperar a que se procese el registro y muestre el error
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        /************** 3. Verificacion de la situacion esperada ***************/
        
        // Verificación: SweetAlert de error por username duplicado
        System.out.println("--- Verificacion: SweetAlert de error por username duplicado ---");
        WebElement sweetAlert = driver.findElement(By.cssSelector(".swal2-popup.swal2-icon-error"));
        boolean alertVisible = sweetAlert.isDisplayed();
        
        WebElement alertTitle = driver.findElement(By.cssSelector(".swal2-title"));
        String titulo = alertTitle.getText();
        
        WebElement alertContent = driver.findElement(By.cssSelector(".swal2-html-container"));
        String contenido = alertContent.getText();
        
        System.out.println("SweetAlert visible? " + alertVisible);
        System.out.println("Titulo: " + titulo);
        System.out.println("Contenido: " + contenido);
        
        // Verificación 1: El SweetAlert debe estar visible y ser de error
        Assert.assertTrue(alertVisible, "Deberia mostrarse SweetAlert de error");
        Assert.assertEquals(titulo, "Error", "El titulo deberia ser 'Error'");
        
        // Verificación 2: El mensaje debe ser exactamente "El username ya está en uso"
        Assert.assertEquals(contenido, "El username ya está en uso", 
                          "El mensaje de error deberia ser 'El username ya está en uso'");
        
        System.out.println("Resultado Paso 4: Sistema muestra error por username duplicado - OK");
        
        // Verificación adicional: Deberíamos permanecer en la página de creación (no redirigir)
        String urlDespuesError = driver.getCurrentUrl();
        boolean permaneceEnCreacion = urlDespuesError.contains("/gestion-usuarios/crear");
        Assert.assertTrue(permaneceEnCreacion, "Deberia permanecer en pagina de creacion tras el error");
        System.out.println("Permanece en pagina de creacion: " + permaneceEnCreacion);
        
        // Clic en Entendido del SweetAlert
        WebElement botonEntendido = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
        botonEntendido.click();
        
        // Esperar a que se cierre el alert
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        /************** 4. Resultado final ***************/
        System.out.println("TEST EXITOSO: Verificacion de username duplicado completada correctamente");
        System.out.println("El sistema correctamente impidio crear usuario con username duplicado: " + username);
        System.out.println("Mensaje de error mostrado correctamente: 'El username ya está en uso'");
    }
}