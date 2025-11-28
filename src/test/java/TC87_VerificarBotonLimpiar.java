import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.Assert;

/****************************************/
// TestCase 87: Verificar que cuando se presiona el botón de Limpiar en el Registro de Nueva Cuenta, 
//              todos los datos se borran y se puede volver a escribir
//
// Paso 1. Hacer click en el botón "Registrarse" dentro del Navbar
// Resultado Esperado: Se muestra el formulario para Crear Nueva Cuenta
//
// Paso 2. Llenar todos los campos del formulario
// Resultado Esperado: Los campos reflejan los datos ingresados, excepto por Contraseña y 
//                    Confirmar contraseña donde se muestra cifrado
//
// Paso 3. Presionar el botón "Limpiar"
// Resultado Esperado: Todos los datos se borran, el formulario queda limpio
/****************************************/

// Para ejecutar: mvn clean test -Dtest=TC87_VerificarBotonLimpiar

public class TC87_VerificarBotonLimpiar extends BaseTest {
    
    @Test
    public void testVerificarBotonLimpiar() {
        
        /************** 1. Preparacion de la prueba ***********/
        System.out.println("=== INICIANDO TEST: Verificacion Boton Limpiar en Registro ===");
        driver.get("http://localhost:5173/");
        System.out.println("Pagina cargada: " + driver.getTitle());
        
        /************** 2. Logica de la prueba ***************/
        
        // Paso 1. Hacer click en el botón "Registrarse" dentro del Navbar
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
        
        // Paso 2. Llenar todos los campos del formulario
        System.out.println("--- Paso 2: Llenar todos los campos del formulario ---");
        
        // Datos de prueba
        String nombre = "UsuarioPrueba";
        String apellido = "ApellidoPrueba";
        String email = "prueba@test.com";
        String telefono = "123456789";
        String username = "usuarioprueba";
        String password = "Password123!";
        
        // Llenar campos del formulario
        WebElement campoNombre = driver.findElement(By.id("nombre"));
        WebElement campoApellido = driver.findElement(By.id("apellido"));
        WebElement campoEmail = driver.findElement(By.id("email"));
        WebElement campoTelefono = driver.findElement(By.id("telefono"));
        WebElement campoUsername = driver.findElement(By.id("username"));
        WebElement campoPassword = driver.findElement(By.id("password"));
        WebElement campoConfirmPassword = driver.findElement(By.id("confirmPassword"));
        
        campoNombre.sendKeys(nombre);
        campoApellido.sendKeys(apellido);
        campoEmail.sendKeys(email);
        campoTelefono.sendKeys(telefono);
        campoUsername.sendKeys(username);
        campoPassword.sendKeys(password);
        campoConfirmPassword.sendKeys(password);
        
        System.out.println("Campos completados con datos de prueba:");
        System.out.println(" - Nombre: " + nombre);
        System.out.println(" - Apellido: " + apellido);
        System.out.println(" - Email: " + email);
        System.out.println(" - Teléfono: " + telefono);
        System.out.println(" - Username: " + username);
        System.out.println(" - Contraseña: Password123!");
        
        // Verificar que los campos tienen los datos ingresados
        Assert.assertEquals(campoNombre.getAttribute("value"), nombre, "El campo nombre deberia tener el valor ingresado");
        Assert.assertEquals(campoApellido.getAttribute("value"), apellido, "El campo apellido deberia tener el valor ingresado");
        Assert.assertEquals(campoEmail.getAttribute("value"), email, "El campo email deberia tener el valor ingresado");
        Assert.assertEquals(campoTelefono.getAttribute("value"), telefono, "El campo telefono deberia tener el valor ingresado");
        Assert.assertEquals(campoUsername.getAttribute("value"), username, "El campo username deberia tener el valor ingresado");
        
        // Verificar que los campos de contraseña están ocultos (type="password")
        String tipoPassword = campoPassword.getAttribute("type");
        String tipoConfirmPassword = campoConfirmPassword.getAttribute("type");
        Assert.assertEquals(tipoPassword, "password", "El campo contraseña deberia estar oculto");
        Assert.assertEquals(tipoConfirmPassword, "password", "El campo confirmar contraseña deberia estar oculto");
        
        System.out.println("Resultado Paso 2: Campos llenos y contraseñas ocultas - OK");
        
        // Pequeña pausa para ver los datos ingresados
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Paso 3. Presionar el botón "Limpiar"
        System.out.println("--- Paso 3: Presionar boton Limpiar ---");
        WebElement botonLimpiar = driver.findElement(By.xpath("//button[contains(text(), 'Limpiar')]"));
        botonLimpiar.click();
        System.out.println("Clic en boton Limpiar");
        
        // Esperar a que se limpien los campos
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        /************** 3. Verificacion de la situacion esperada ***************/
        
        System.out.println("--- Verificacion: Campos limpios despues de Limpiar ---");
        
        // Verificar que todos los campos están vacíos
        String valorNombreDespues = campoNombre.getAttribute("value");
        String valorApellidoDespues = campoApellido.getAttribute("value");
        String valorEmailDespues = campoEmail.getAttribute("value");
        String valorTelefonoDespues = campoTelefono.getAttribute("value");
        String valorUsernameDespues = campoUsername.getAttribute("value");
        String valorPasswordDespues = campoPassword.getAttribute("value");
        String valorConfirmPasswordDespues = campoConfirmPassword.getAttribute("value");
        
        System.out.println("Valores despues de limpiar:");
        System.out.println(" - Nombre: '" + valorNombreDespues + "'");
        System.out.println(" - Apellido: '" + valorApellidoDespues + "'");
        System.out.println(" - Email: '" + valorEmailDespues + "'");
        System.out.println(" - Teléfono: '" + valorTelefonoDespues + "'");
        System.out.println(" - Username: '" + valorUsernameDespues + "'");
        System.out.println(" - Contraseña: '" + valorPasswordDespues + "'");
        System.out.println(" - Confirmar Contraseña: '" + valorConfirmPasswordDespues + "'");
        
        // Verificación 1: Todos los campos de texto deben estar vacíos
        Assert.assertTrue(valorNombreDespues.isEmpty(), "El campo nombre deberia estar vacio");
        Assert.assertTrue(valorApellidoDespues.isEmpty(), "El campo apellido deberia estar vacio");
        Assert.assertTrue(valorEmailDespues.isEmpty(), "El campo email deberia estar vacio");
        Assert.assertTrue(valorTelefonoDespues.isEmpty(), "El campo telefono deberia estar vacio");
        Assert.assertTrue(valorUsernameDespues.isEmpty(), "El campo username deberia estar vacio");
        Assert.assertTrue(valorPasswordDespues.isEmpty(), "El campo contraseña deberia estar vacio");
        Assert.assertTrue(valorConfirmPasswordDespues.isEmpty(), "El campo confirmar contraseña deberia estar vacio");
        
        System.out.println("Resultado Paso 3: Todos los campos estan vacios - OK");
        
        // Verificación adicional: Probar que se puede volver a escribir
        System.out.println("--- Verificacion adicional: Probar que se puede volver a escribir ---");
        campoNombre.sendKeys("NuevoNombre");
        String nuevoValor = campoNombre.getAttribute("value");
        Assert.assertEquals(nuevoValor, "NuevoNombre", "Deberia poder escribirse nuevamente en el campo");
        System.out.println("Se puede volver a escribir en campos limpios - OK");
        
        /************** 4. Resultado final ***************/
        System.out.println("TEST EXITOSO: Boton Limpiar funciona correctamente");
        System.out.println("Todos los campos se borraron exitosamente despues de presionar Limpiar");
        System.out.println("El formulario queda listo para volver a escribir");
    }
}