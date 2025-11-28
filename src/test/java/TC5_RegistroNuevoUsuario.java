import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;
import org.testng.Assert;

/****************************************/
// TestCase 5: Verificar que es posible registrar un nuevo usuario del sistema desde el panel de administración
//
// antes: Iniciar sesión como Administrador
// Paso 2. En el navbar, seleccionar "Usuarios"
// Paso 3. Hacer clic en el botón "Nuevo Usuario"
// Paso 4. Completar los campos requeridos del formulario
// Paso 5. Hacer clic en el botón "Crear Usuario"
// Paso 6. Verificar que el usuario aparece en la tabla de usuarios
//
// Resultado Esperado: 
// - Debe mostrarse mensaje "Usuario creado correctamente" en SweetAlert
// - El nuevo usuario debe aparecer en la lista de usuarios
/****************************************/

// Para ejecutar: mvn clean test -Dtest=TC5_RegistroNuevoUsuario

public class TC5_RegistroNuevoUsuario extends BaseTest {
    
    @Test
    public void testRegistroNuevoUsuario() {
        
        /************** 1. Preparacion de la prueba - Login como Admin ***********/
        System.out.println("=== INICIANDO TEST: Registro de Nuevo Usuario ===");
        
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
        campoPassword.sendKeys("Password123!");
        
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
        
        // Paso 1. En el navbar, seleccionar "Usuarios"
        System.out.println("--- Paso 1: Seleccionar Usuarios en el navbar ---");
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
        
        // Paso 3. Completar los campos requeridos del formulario
        System.out.println("--- Paso 3: Completar campos del formulario ---");
        
        // Generar datos únicos para evitar conflictos
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nombre = "Nuevo" + timestamp;
        String apellido = "Usuario" + timestamp;
        String email = "nuevo" + timestamp + "@gmail.com";
        String username = "nuevo" + timestamp;
        String telefono = "123456789";
        String password = "Password123!";
        
        // Llenar campos del formulario
        driver.findElement(By.id("nombre")).sendKeys(nombre);
        driver.findElement(By.id("apellido")).sendKeys(apellido);
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("telefono")).sendKeys(telefono);
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("confirmPassword")).sendKeys(password);
        
        // Seleccionar un rol (MESERO)
        Select selectRol = new Select(driver.findElement(By.id("rolId")));
        selectRol.selectByVisibleText("MESERO");
        
        System.out.println("Campos completados:");
        System.out.println(" - Nombre: " + nombre);
        System.out.println(" - Apellido: " + apellido);
        System.out.println(" - Email: " + email);
        System.out.println(" - Username: " + username);
        System.out.println(" - Rol: MESERO");
        
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
        
        // Esperar a que se procese el registro
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        /************** 3. Verificacion de la situacion esperada ***************/
        
        // Verificación 1: SweetAlert de éxito
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
        
        Assert.assertTrue(alertVisible, "Deberia mostrarse SweetAlert de exito");
        Assert.assertEquals(titulo, "Éxito", "El titulo deberia ser 'Éxito'");
        Assert.assertEquals(contenido, "Usuario creado correctamente", "El mensaje deberia ser 'Usuario creado correctamente'");
        
        System.out.println("Resultado Paso 4: Usuario creado correctamente - OK");
        
        // Clic en Aceptar del SweetAlert
        WebElement botonAceptarSweetAlert = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
        botonAceptarSweetAlert.click();
        
        // Esperar a que se cierre el alert y cargue la página
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Paso 5. Verificar que el usuario aparece en la tabla de usuarios
        System.out.println("--- Paso 5: Verificar usuario en lista ---");
        
        // Verificar que estamos en la página de gestión de usuarios
        String urlFinal = driver.getCurrentUrl();
        Assert.assertTrue(urlFinal.contains("/gestion-usuarios"), "Deberia estar en la pagina de gestion de usuarios");
        
        // Buscar el usuario en la tabla por su email único
        boolean usuarioEncontrado = false;
        try {
            WebElement usuarioEnTabla = driver.findElement(By.xpath("//td[contains(., '" + email + "')]"));
            usuarioEncontrado = usuarioEnTabla.isDisplayed();
            System.out.println("Usuario encontrado en tabla con email: " + email);
        } catch (Exception e) {
            System.out.println("Usuario no encontrado en tabla con email: " + email);
        }
        
        // También buscar por username
        boolean usernameEncontrado = false;
        try {
            WebElement usernameEnTabla = driver.findElement(By.xpath("//td[contains(., '@" + username + "')]"));
            usernameEncontrado = usernameEnTabla.isDisplayed();
            System.out.println("Username encontrado en tabla: @" + username);
        } catch (Exception e) {
            System.out.println("Username no encontrado en tabla: @" + username);
        }
        
        // Verificar que al menos uno de los dos métodos encontró el usuario
        Assert.assertTrue(usuarioEncontrado || usernameEncontrado, 
                         "El nuevo usuario deberia aparecer en la tabla de usuarios. Email: " + email + ", Username: " + username);
        
        System.out.println("Resultado Paso 5: Usuario aparece en lista - OK");
        
        /************** 4. Resultado final ***************/
        System.out.println("TEST EXITOSO: Registro de nuevo usuario completado correctamente");
        System.out.println("Usuario creado: " + nombre + " " + apellido + " (" + email + ")");
        System.out.println("Todos los pasos se ejecutaron y verificaron exitosamente");
    }
}