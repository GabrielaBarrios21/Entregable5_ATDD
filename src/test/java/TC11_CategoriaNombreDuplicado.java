import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.Assert;

/****************************************/
// TestCase 11: Verificar que no se permita crear una categoría con un nombre ya existente
//
// Paso 1. Ir al módulo "Categorías" y hacer clic en "Nueva categoría"
// Resultado Esperado: Se muestra el formulario de creación
//
// Paso 2. Ingresar un nombre de categoría ya existente
// Resultado Esperado: El sistema detecta el duplicado
//
// Paso 3. Intentar guardar los datos
// Resultado Esperado: El sistema muestra el mensaje "El nombre de la categoría ya está registrado"
//                     No se crea la categoría
/****************************************/

// Para ejecutar: mvn clean test -Dtest=TC11_CategoriaNombreDuplicado

public class TC11_CategoriaNombreDuplicado extends BaseTest {
    
    @Test
    public void testCategoriaNombreDuplicado() {
        
        /************** 1. Preparacion de la prueba - Login como Admin ***********/
        System.out.println("=== INICIANDO TEST: Verificación de Categoría con Nombre Duplicado ===");
        
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
        
        // Cerrar el SweetAlert de login exitoso si existe
        try {
            WebElement botonAceptar = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
            botonAceptar.click();
            System.out.println("SweetAlert de login cerrado");
        } catch (Exception e) {
            System.out.println("No había SweetAlert de login o ya estaba cerrado");
        }
        
        System.out.println("Login como administrador exitoso");
        
        /************** 2. Logica de la prueba ***************/
        
        // Paso 1. Ir al módulo "Categorías" y hacer clic en "Nueva categoría"
        System.out.println("--- Paso 1: Acceder a Categorías y Nueva Categoría ---");
        WebElement menuCategorias = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Categorías')]]"));
        menuCategorias.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que estamos en la página de categorías
        String urlCategorias = driver.getCurrentUrl();
        Assert.assertTrue(urlCategorias.contains("/gestion-categorias"), "Deberia estar en la pagina de gestion de categorias");
        System.out.println("Resultado Paso 1: Se muestra página de categorías - OK");
        
        // Hacer clic en "Nueva Categoría"
        WebElement botonNuevaCategoria = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Nueva Categoría')]]"));
        botonNuevaCategoria.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que estamos en la página de crear categoría
        String urlCrearCategoria = driver.getCurrentUrl();
        Assert.assertTrue(urlCrearCategoria.contains("/gestion-categorias/crear"), "Deberia estar en la pagina de crear categoria");
        System.out.println("Resultado Paso 1: Se muestra formulario de creación - OK");
        System.out.println("URL actual: " + urlCrearCategoria);
        
        // Paso 2. Ingresar un nombre de categoría ya existente
        System.out.println("--- Paso 2: Ingresar nombre de categoría duplicado ---");
        
        // Usar un nombre que ya existe (Bebidas)
        String nombreCategoriaDuplicado = "Bebidas";
        String descripcionCategoria = "descripcion de prueba";
        
        // Llenar campos del formulario
        WebElement campoNombre = driver.findElement(By.id("nombre"));
        WebElement campoDescripcion = driver.findElement(By.id("descripcion"));
        
        campoNombre.sendKeys(nombreCategoriaDuplicado);
        campoDescripcion.sendKeys(descripcionCategoria);
        
        System.out.println("Campos completados:");
        System.out.println(" - Nombre (DUPLICADO): " + nombreCategoriaDuplicado);
        System.out.println(" - Descripción: " + descripcionCategoria);
        
        // Verificar que los campos tienen los datos ingresados
        Assert.assertEquals(campoNombre.getAttribute("value"), nombreCategoriaDuplicado, "El campo nombre deberia tener el valor ingresado");
        Assert.assertEquals(campoDescripcion.getAttribute("value"), descripcionCategoria, "El campo descripcion deberia tener el valor ingresado");
        
        System.out.println("Resultado Paso 2: Campos llenos con nombre duplicado - OK");
        
        // Pequeña pausa antes de guardar
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Paso 3. Intentar guardar los datos
        System.out.println("--- Paso 3: Intentar crear categoría con nombre duplicado ---");
        WebElement botonCrearCategoria = driver.findElement(By.xpath("//button[.//span[contains(text(), 'Crear Categoría')]]"));
        botonCrearCategoria.click();
        
        // Esperar a que se procese la validación
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        /************** 3. Verificacion del error ***************/
        
        // Verificación: SweetAlert de error por nombre duplicado
        System.out.println("--- Verificacion: Error por nombre duplicado ---");
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
        
        // Verificación 2: El mensaje debe ser exactamente "El nombre de la categoría ya está registrado"
        Assert.assertEquals(contenido, "El nombre de la categoría ya está registrado", 
                          "El mensaje de error deberia ser 'El nombre de la categoría ya está registrado'");
        
        System.out.println("Resultado Paso 3: Sistema muestra error por nombre duplicado - OK");
        
        // Verificación adicional: Deberíamos permanecer en la página de creación (no redirigir)
        String urlDespuesError = driver.getCurrentUrl();
        boolean permaneceEnCreacion = urlDespuesError.contains("/gestion-categorias/crear");
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
        System.out.println("TEST EXITOSO: Verificación de nombre duplicado completada correctamente");
        System.out.println("El sistema correctamente impidio crear categoría con nombre duplicado: " + nombreCategoriaDuplicado);
        System.out.println("Mensaje de error mostrado correctamente: 'El nombre de la categoría ya está registrado'");
    }
}