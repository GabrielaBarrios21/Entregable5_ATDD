import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.Assert;

/****************************************/
// TestCase 10: Verificar que el administrador pueda crear una nueva categoría con datos válidos
//
// Paso 1. Ir al módulo "Categorías" y hacer clic en "Nueva categoría"
// Resultado Esperado: Se muestra el formulario de creación
//
// Paso 2. Ingresar un nombre único y descripción
// Resultado Esperado: Los campos permiten ingresar datos
//
// Paso 3. Hacer clic en "Crear Categoría"
// Resultado Esperado: El sistema muestra el mensaje "Categoría creada correctamente"
//
// Paso 4. Verificar la lista de categorías
// Resultado Esperado: La nueva categoría aparece en estado "Activa"
/****************************************/

// Para ejecutar: mvn clean test -Dtest=TC10_CrearCategoria

public class TC10_CrearCategoria extends BaseTest {
    
    @Test
    public void testCrearCategoria() {
        
        /************** 1. Preparacion de la prueba - Login como Admin ***********/
        System.out.println("=== INICIANDO TEST: Creación de Nueva Categoría ===");
        
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
        
        // Paso 2. Ingresar un nombre único y descripción
        System.out.println("--- Paso 2: Ingresar nombre y descripción ---");
        
        // Generar datos únicos más cortos
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nombreCategoria = "Cat" + timestamp.substring(timestamp.length() - 4); // Solo últimos 4 dígitos
        String descripcionCategoria = "descripcion";
        
        // Llenar campos del formulario
        WebElement campoNombre = driver.findElement(By.id("nombre"));
        WebElement campoDescripcion = driver.findElement(By.id("descripcion"));
        
        campoNombre.sendKeys(nombreCategoria);
        campoDescripcion.sendKeys(descripcionCategoria);
        
        System.out.println("Campos completados:");
        System.out.println(" - Nombre: " + nombreCategoria);
        System.out.println(" - Descripción: " + descripcionCategoria);
        
        // Verificar que los campos tienen los datos ingresados
        Assert.assertEquals(campoNombre.getAttribute("value"), nombreCategoria, "El campo nombre deberia tener el valor ingresado");
        Assert.assertEquals(campoDescripcion.getAttribute("value"), descripcionCategoria, "El campo descripcion deberia tener el valor ingresado");
        
        System.out.println("Resultado Paso 2: Campos llenos correctamente - OK");
        
        // Pequeña pausa antes de guardar
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Paso 3. Hacer clic en "Crear Categoría"
        System.out.println("--- Paso 3: Crear categoría ---");
        WebElement botonCrearCategoria = driver.findElement(By.xpath("//button[.//span[contains(text(), 'Crear Categoría')]]"));
        botonCrearCategoria.click();
        
        // Esperar a que se procese la creación
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificación: SweetAlert de éxito
        System.out.println("--- Verificacion: Categoría creada exitosamente ---");
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
        Assert.assertEquals(contenido, "Categoría creada correctamente", 
                          "El mensaje deberia ser 'Categoría creada correctamente'");
        
        System.out.println("Resultado Paso 3: Categoría creada correctamente - OK");
        
        // Clic en Aceptar del SweetAlert
        WebElement botonAceptarSweetAlert = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
        botonAceptarSweetAlert.click();
        
        // Esperar a que se cierre el alert y cargue la página
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        /************** 3. Verificacion en lista de categorías ***************/
        
        // Paso 4. Verificar la lista de categorías
        System.out.println("--- Paso 4: Verificar categoría en lista ---");
        
        // Verificar que estamos en la página de gestión de categorías
        String urlFinal = driver.getCurrentUrl();
        Assert.assertTrue(urlFinal.contains("/gestion-categorias"), "Deberia estar en la pagina de gestion de categorias");
        
        // Buscar la categoría en la lista por su nombre único
        boolean categoriaEncontrada = false;
        
        try {
            // Buscar el elemento que contiene el nombre de la categoría
            WebElement categoriaEnLista = driver.findElement(By.xpath("//h3[contains(@class, 'text-gray-900') and contains(text(), '" + nombreCategoria + "')]"));
            categoriaEncontrada = categoriaEnLista.isDisplayed();
            
            System.out.println("Categoría encontrada en lista: " + nombreCategoria);
            
            // Verificar que está activa - buscar el span con clase bg-green-100 cerca del nombre
            WebElement estadoCategoria = driver.findElement(By.xpath("//h3[contains(text(), '" + nombreCategoria + "')]/following-sibling::span[contains(@class, 'bg-green-100')]"));
            String textoEstado = estadoCategoria.getText();
            boolean categoriaActiva = textoEstado.equals("Activa");
            
            System.out.println("Estado: " + textoEstado);
            
            // Verificar que la categoría está activa
            Assert.assertTrue(categoriaActiva, "La categoría deberia estar en estado 'Activa'");
            
        } catch (Exception e) {
            System.out.println("Categoría no encontrada en lista: " + nombreCategoria);
            // Mostrar qué categorías hay disponibles para debug
            System.out.println("Buscando todas las categorías disponibles...");
            try {
                java.util.List<WebElement> todasCategorias = driver.findElements(By.xpath("//h3[contains(@class, 'text-gray-900')]"));
                System.out.println("Categorías encontradas en la página:");
                for (WebElement cat : todasCategorias) {
                    System.out.println(" - " + cat.getText());
                }
            } catch (Exception e2) {
                System.out.println("No se pudieron listar las categorías");
            }
        }
        
        // Verificación 2: La categoría debe aparecer en la lista
        Assert.assertTrue(categoriaEncontrada, "La nueva categoría deberia aparecer en la lista");
        
        System.out.println("Resultado Paso 4: Categoría aparece en lista como Activa - OK");
        
        /************** 4. Resultado final ***************/
        System.out.println("TEST EXITOSO: Creación de categoría completada correctamente");
        System.out.println("Categoría creada: " + nombreCategoria);
        System.out.println("Estado: Activa");
        System.out.println("Todos los pasos se ejecutaron y verificaron exitosamente");
    }
}