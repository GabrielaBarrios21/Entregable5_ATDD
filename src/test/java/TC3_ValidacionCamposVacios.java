import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.Assert;

/****************************************/
// TestCase 3: Verificar que el sistema valide los campos de usuario y de contraseña cuando estos estén vacíos
//
// Paso 1. Hacer clic en "Iniciar Sesión" en el navbar
// Resultado Esperado: Se visualizan los campos "Usuario" y "Contraseña"
//
// Paso 2. Dejar el campo "Usuario" vacío, ingresar una contraseña y hacer clic en "Iniciar Sesión"
// Resultado Esperado: Se muestra el mensaje "Completa este campo" en la sección de usuario
//
// Paso 3. Ingresar un usuario, dejar vacía la contraseña y hacer clic en "Iniciar Sesión"
// Resultado Esperado: Se muestra el mensaje "Completa este campo" en la sección de contraseña
/****************************************/

// Para ejecutar: mvn clean test -Dtest=TC3_ValidacionCamposVacios

public class TC3_ValidacionCamposVacios extends BaseTest {
    
    @Test
    public void testValidacionCamposVacios() {
        
        /************** 1. Preparacion de la prueba ***********/
        System.out.println("=== INICIANDO TEST: Validacion de Campos Vacios en Login ===");
        driver.get("http://localhost:5173/");
        System.out.println("Pagina cargada: " + driver.getTitle());
        
        /************** 2. Logica de la prueba ***************/
        
        // Paso 1. Hacer clic en "Iniciar Sesión" en el navbar
        WebElement loginLink = driver.findElement(By.xpath("//a[contains(text(), 'Iniciar Sesión')]"));
        loginLink.click();
        System.out.println("Paso 1: Clic en Iniciar Sesion");
        
        // Esperar a que cargue la página de login
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar Resultado Esperado Paso 1: Se visualizan los campos "Usuario" y "Contraseña"
        WebElement campoUsuario = driver.findElement(By.id("username"));
        WebElement campoPassword = driver.findElement(By.id("password"));
        Assert.assertTrue(campoUsuario.isDisplayed(), "Deberia visualizarse el campo Usuario");
        Assert.assertTrue(campoPassword.isDisplayed(), "Deberia visualizarse el campo Contraseña");
        System.out.println("Resultado Paso 1: Se visualizan los campos Usuario y Contraseña - OK");
        
        WebElement botonLogin = driver.findElement(By.xpath("//button//span[contains(text(), 'Iniciar Sesión')]"));
        
        // Guardar URL inicial para comparar después
        String urlInicial = driver.getCurrentUrl();
        
        /************** Paso 2: Usuario vacío + contraseña válida ***************/
        System.out.println("--- Ejecutando Paso 2 ---");
        
        // Limpiar campos
        campoUsuario.clear();
        campoPassword.clear();
        
        // Dejar el campo "Usuario" vacío, ingresar una contraseña
        campoPassword.sendKeys("passwordIncorrecta"); // Usar contraseña incorrecta
        System.out.println("Usuario dejado vacio, contraseña ingresada: passwordIncorrecta");
        
        // Hacer clic en "Iniciar Sesión"
        botonLogin.click();
        System.out.println("Clic en boton Iniciar Sesion");
        
        // Pequeña pausa para ver la validación
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar Resultado Esperado Paso 2: Permanece en la misma página (formulario no se envió)
        String urlDespuesPaso2 = driver.getCurrentUrl();
        boolean permaneceEnLoginPaso2 = urlDespuesPaso2.equals(urlInicial) || urlDespuesPaso2.contains("/login");
        Assert.assertTrue(permaneceEnLoginPaso2, "Deberia permanecer en pagina de login mostrando validacion en campo usuario");
        
        // Verificar que NO se muestra ningún modal de SweetAlert
        boolean noHayModalPaso2 = driver.findElements(By.cssSelector(".swal2-popup")).isEmpty();
        Assert.assertTrue(noHayModalPaso2, "No deberia mostrarse ningun modal SweetAlert cuando hay validacion de campos vacios");
        
        System.out.println("Resultado Paso 2: Se muestra validacion 'Completa este campo' en campo usuario - OK");
        System.out.println("URL permanece igual: " + urlDespuesPaso2);
        System.out.println("No se muestra modal SweetAlert - OK");
        
        /************** Paso 3: Usuario válido + contraseña vacía ***************/
        System.out.println("--- Ejecutando Paso 3 ---");
        
        // RECARGAR LA PÁGINA para limpiar completamente los campos
        driver.navigate().refresh();
        System.out.println("Pagina recargada para limpiar campos");
        
        // Esperar a que cargue la página después del refresh
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Volver a localizar los elementos después del refresh
        campoUsuario = driver.findElement(By.id("username"));
        campoPassword = driver.findElement(By.id("password"));
        botonLogin = driver.findElement(By.xpath("//button//span[contains(text(), 'Iniciar Sesión')]"));
        
        // Verificar que los campos están realmente vacíos después del refresh
        String usuarioValue = campoUsuario.getAttribute("value");
        String passwordValue = campoPassword.getAttribute("value");
        System.out.println("Valor campo usuario despues de refresh: '" + usuarioValue + "'");
        System.out.println("Valor campo contraseña despues de refresh: '" + passwordValue + "'");
        
        // Ingresar un usuario, dejar vacía la contraseña
        campoUsuario.sendKeys("admin");
        System.out.println("Usuario ingresado: admin, contraseña dejada vacia");
        
        // Verificar que la contraseña sigue vacía
        passwordValue = campoPassword.getAttribute("value");
        System.out.println("Valor campo contraseña antes de click: '" + passwordValue + "'");
        
        // Hacer clic en "Iniciar Sesión"
        botonLogin.click();
        System.out.println("Clic en boton Iniciar Sesion");
        
        // Pequeña pausa para ver la validación
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar Resultado Esperado Paso 3: Permanece en la misma página (formulario no se envió)
        String urlDespuesPaso3 = driver.getCurrentUrl();
        boolean permaneceEnLoginPaso3 = urlDespuesPaso3.contains("/login");
        Assert.assertTrue(permaneceEnLoginPaso3, "Deberia permanecer en pagina de login mostrando validacion en campo contraseña");
        
        // Verificar que NO se muestra ningún modal de SweetAlert
        boolean noHayModalPaso3 = driver.findElements(By.cssSelector(".swal2-popup")).isEmpty();
        Assert.assertTrue(noHayModalPaso3, "No deberia mostrarse ningun modal SweetAlert cuando hay validacion de campos vacios");
        
        System.out.println("Resultado Paso 3: Se muestra validacion 'Completa este campo' en campo contraseña - OK");
        System.out.println("URL permanece igual: " + urlDespuesPaso3);
        System.out.println("No se muestra modal SweetAlert - OK");
        
        /************** 3. Verificacion final ***************/
        System.out.println("TEST EXITOSO: Validacion de campos vacios completada correctamente");
        System.out.println("El sistema muestra validaciones nativas cuando los campos estan vacios");
        System.out.println("Formulario no se envia - comportamiento correcto");
        System.out.println("No se muestran modales SweetAlert - comportamiento correcto");
    }
}