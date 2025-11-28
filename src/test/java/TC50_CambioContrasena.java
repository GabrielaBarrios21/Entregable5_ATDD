import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.Assert;

/****************************************/
// TestCase 50: Verificar que es posible actualizar la contraseña del usuario y la nueva contraseña funciona
//
// Paso 1. Iniciar sesión con usuario y contraseña actual (Password123!)
// Paso 2. En el navbar, hacer clic en la opción 'Mi Perfil'
// Paso 3. En la página 'Mi Perfil', acceder a la opción 'Cambiar Contraseña'
// Paso 4. Ingresar la contraseña actual en el campo 'Contraseña Actual'
// Paso 5. Ingresar nueva contraseña válida en 'Nueva Contraseña' y 'Confirmar Nueva Contraseña'
// Paso 6. Guardar los cambios haciendo clic en 'Cambiar Contraseña'
// Paso 7. Cerrar sesión e iniciar sesión con la nueva contraseña
//
// Resultado Esperado: 
// - Debe mostrarse mensaje "Contraseña actualizada correctamente"
// - Debe poder iniciar sesión con la nueva contraseña
/****************************************/

// Para ejecutar: mvn clean test -Dtest=TC50_CambioContrasena

public class TC50_CambioContrasena extends BaseTest {
    
    @Test
    public void testCambioContrasena() {
        
        /************** 1. Preparacion de la prueba - Login inicial ***********/
        System.out.println("=== INICIANDO TEST: Cambio de Contraseña de Usuario ===");
        
        // Paso 1. Iniciar sesión con usuario y contraseña actual
        System.out.println("--- Paso 1: Iniciar sesión con contraseña actual ---");
        driver.get("http://localhost:5173/");
        
        // Ir a la página de login
        WebElement loginLink = driver.findElement(By.xpath("//a[contains(text(), 'Iniciar Sesión')]"));
        loginLink.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Completar credenciales con contraseña actual
        WebElement campoUsuario = driver.findElement(By.id("username"));
        WebElement campoPassword = driver.findElement(By.id("password"));
        campoUsuario.sendKeys("usuario");
        campoPassword.sendKeys("Password123!");
        
        WebElement botonLogin = driver.findElement(By.xpath("//button//span[contains(text(), 'Iniciar Sesión')]"));
        botonLogin.click();
        
        // Esperar login exitoso
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Cerrar el SweetAlert de login exitoso si existe
        try {
            WebElement botonAceptar = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
            botonAceptar.click();
            System.out.println("SweetAlert de login cerrado");
        } catch (Exception e) {
            System.out.println("No había SweetAlert de login o ya estaba cerrado");
        }
        
        System.out.println("Login exitoso con contraseña actual (Password123!)");
        
        /************** 2. Logica de cambio de contraseña ***************/
        
        // Paso 2. En el navbar, hacer clic en la opción 'Mi Perfil'
        System.out.println("--- Paso 2: Acceder a Mi Perfil ---");
        
        WebElement menuMiPerfil = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Mi Perfil')]]"));
        menuMiPerfil.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que estamos en la página de Mi Perfil
        String urlMiPerfil = driver.getCurrentUrl();
        Assert.assertTrue(urlMiPerfil.contains("/mi-perfil"), "Deberia estar en la pagina Mi Perfil");
        System.out.println("Resultado Paso 2: Se accede a página Mi Perfil - OK");
        System.out.println("URL actual: " + urlMiPerfil);
        
        // Paso 3. En la página 'Mi Perfil', acceder a la opción 'Cambiar Contraseña'
        System.out.println("--- Paso 3: Acceder a Cambiar Contraseña ---");
        
        // Buscar el botón/tab de Cambiar Contraseña
        WebElement tabCambiarPassword = driver.findElement(By.xpath("//button[contains(text(), 'Cambiar Contraseña')]"));
        tabCambiarPassword.click();
        
        // Esperar a que cargue la sección de cambio de contraseña
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que se muestran los campos de cambio de contraseña
        WebElement campoCurrentPassword = driver.findElement(By.id("currentPassword"));
        WebElement campoNewPassword = driver.findElement(By.id("newPassword"));
        WebElement campoConfirmPassword = driver.findElement(By.id("confirmPassword"));
        
        Assert.assertTrue(campoCurrentPassword.isDisplayed(), "Deberia mostrarse campo Contraseña Actual");
        Assert.assertTrue(campoNewPassword.isDisplayed(), "Deberia mostrarse campo Nueva Contraseña");
        Assert.assertTrue(campoConfirmPassword.isDisplayed(), "Deberia mostrarse campo Confirmar Contraseña");
        System.out.println("Resultado Paso 3: Se muestran campos de cambio de contraseña - OK");
        
        // Paso 4. Ingresar la contraseña actual en el campo 'Contraseña Actual'
        System.out.println("--- Paso 4: Ingresar contraseña actual ---");
        campoCurrentPassword.sendKeys("Password123!");
        
        // Verificar que el campo está cifrado
        String tipoCurrentPassword = campoCurrentPassword.getAttribute("type");
        Assert.assertEquals(tipoCurrentPassword, "password", "El campo contraseña actual deberia estar cifrado");
        System.out.println("Contraseña actual ingresada y cifrada - OK");
        
        // Paso 5. Ingresar nueva contraseña válida en 'Nueva Contraseña' y 'Confirmar Nueva Contraseña'
        System.out.println("--- Paso 5: Ingresar nueva contraseña ---");
        
        String nuevaContrasena = "Password321!";
        campoNewPassword.sendKeys(nuevaContrasena);
        campoConfirmPassword.sendKeys(nuevaContrasena);
        
        // Verificar que los campos están cifrados
        String tipoNewPassword = campoNewPassword.getAttribute("type");
        String tipoConfirmPassword = campoConfirmPassword.getAttribute("type");
        Assert.assertEquals(tipoNewPassword, "password", "El campo nueva contraseña deberia estar cifrado");
        Assert.assertEquals(tipoConfirmPassword, "password", "El campo confirmar contraseña deberia estar cifrado");
        System.out.println("Nueva contraseña ingresada y cifrada - OK");
        
        // Pequeña pausa antes de guardar
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Paso 6. Guardar los cambios haciendo clic en 'Cambiar Contraseña'
        System.out.println("--- Paso 6: Guardar cambio de contraseña ---");
        WebElement botonCambiarPassword = driver.findElement(By.xpath("//button[.//span[contains(text(), 'Cambiar Contraseña')]]"));
        botonCambiarPassword.click();
        
        // Esperar a que se procese el cambio
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificación: SweetAlert de éxito
        System.out.println("--- Verificacion: Cambio de contraseña exitoso ---");
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
        Assert.assertEquals(contenido, "Contraseña actualizada correctamente", 
                          "El mensaje deberia ser 'Contraseña actualizada correctamente'");
        
        System.out.println("Resultado Paso 6: Contraseña actualizada correctamente - OK");
        
        // Clic en Aceptar del SweetAlert
        WebElement botonAceptarSweetAlert = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
        botonAceptarSweetAlert.click();
        
        // Esperar a que se cierre el alert
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        /************** 3. Verificacion con nueva contraseña ***************/
        
        // Paso 7. Cerrar sesión
        System.out.println("--- Paso 7: Cerrar sesión ---");
        WebElement botonSalir = driver.findElement(By.xpath("//button[.//span[contains(text(), 'Salir')]]"));
        botonSalir.click();
        
        // Esperar a que aparezca el modal de confirmación
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Confirmar el cierre de sesión en el modal
        System.out.println("--- Confirmando cierre de sesión ---");
        
        // Hacer clic en "Sí, salir"
        WebElement botonConfirmarSalir = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm') and contains(text(), 'Sí, salir')]"));
        botonConfirmarSalir.click();
        
        // Esperar a que se procese el cierre de sesión
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Navegar directamente a la página de login
        System.out.println("--- Navegando a página de login ---");
        driver.get("http://localhost:5173/login");
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que estamos en la página de login
        String urlLogin = driver.getCurrentUrl();
        Assert.assertTrue(urlLogin.contains("/login"), "Deberia estar en la pagina de login");
        System.out.println("En página de login - OK");
        
        // Paso 8. Iniciar sesión con la nueva contraseña
        System.out.println("--- Paso 8: Iniciar sesión con nueva contraseña ---");
        
        // Completar credenciales con nueva contraseña
        WebElement campoUsuarioNuevo = driver.findElement(By.id("username"));
        WebElement campoPasswordNuevo = driver.findElement(By.id("password"));
        campoUsuarioNuevo.sendKeys("usuario");
        campoPasswordNuevo.sendKeys("Password321!");
        
        WebElement botonLoginNuevo = driver.findElement(By.xpath("//button//span[contains(text(), 'Iniciar Sesión')]"));
        botonLoginNuevo.click();
        
        // Esperar login exitoso con nueva contraseña
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar SweetAlert de login exitoso con nueva contraseña
        WebElement sweetAlertLogin = driver.findElement(By.cssSelector(".swal2-popup.swal2-icon-success"));
        boolean alertLoginVisible = sweetAlertLogin.isDisplayed();
        
        WebElement alertTitleLogin = driver.findElement(By.cssSelector(".swal2-title"));
        String tituloLogin = alertTitleLogin.getText();
        
        WebElement alertContentLogin = driver.findElement(By.cssSelector(".swal2-html-container"));
        String contenidoLogin = alertContentLogin.getText();
        
        System.out.println("SweetAlert login visible? " + alertLoginVisible);
        System.out.println("Titulo login: " + tituloLogin);
        System.out.println("Contenido login: " + contenidoLogin);
        
        // Verificación 2: Login exitoso con nueva contraseña
        Assert.assertTrue(alertLoginVisible, "Deberia mostrarse SweetAlert de login exitoso");
        Assert.assertTrue(tituloLogin.contains("Éxito") || contenidoLogin.contains("Sesión iniciada correctamente"), 
                         "Deberia mostrar mensaje de login exitoso con nueva contraseña");
        
        System.out.println("Resultado Paso 8: Login exitoso con nueva contraseña - OK");
        
        /************** 4. Resultado final ***************/
        System.out.println("TEST EXITOSO: Cambio de contraseña completado correctamente");
        System.out.println("Contraseña cambiada de 'Password123!' a 'Password321!'");
        System.out.println("Nueva contraseña funciona correctamente para iniciar sesión");
    }
}