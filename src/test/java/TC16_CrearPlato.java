import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;
import org.testng.Assert;

/****************************************/
// TestCase 16: Verificar que el administrador pueda crear un nuevo plato
//
// Paso 1. Ir al módulo "Platos" y hacer clic en "Nuevo Plato"
// Resultado Esperado: Se muestra el formulario de creación
//
// Paso 2. Completar los campos del formulario
// Resultado Esperado: Los campos permiten ingresar datos
//
// Paso 3. Hacer clic en "Crear Plato"
// Resultado Esperado: El sistema muestra el mensaje "Plato creado correctamente"
//
// Paso 4. Verificar que el plato aparece en el menú del cliente
/****************************************/

// Para ejecutar: mvn clean test -Dtest=TC16_CrearPlato

public class TC16_CrearPlato extends BaseTest {
    
    @Test
    public void testCrearPlato() {
        
        /************** 1. Preparacion de la prueba - Login como Admin ***********/
        System.out.println("=== INICIANDO TEST: Creación de Nuevo Plato ===");
        
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
        
        // Paso 1. Ir al módulo "Platos" y hacer clic en "Nuevo Plato"
        System.out.println("--- Paso 1: Acceder a Platos y Nuevo Plato ---");
        WebElement menuPlatos = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Platos')]]"));
        menuPlatos.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que estamos en la página de platos
        String urlPlatos = driver.getCurrentUrl();
        Assert.assertTrue(urlPlatos.contains("/gestion-platos"), "Deberia estar en la pagina de gestion de platos");
        System.out.println("Resultado Paso 1: Se muestra página de platos - OK");
        
        // Hacer clic en "Nuevo Plato"
        WebElement botonNuevoPlato = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Nuevo Plato')]]"));
        botonNuevoPlato.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que estamos en la página de crear plato
        String urlCrearPlato = driver.getCurrentUrl();
        Assert.assertTrue(urlCrearPlato.contains("/gestion-platos/crear"), "Deberia estar en la pagina de crear plato");
        System.out.println("Resultado Paso 1: Se muestra formulario de creación - OK");
        System.out.println("URL actual: " + urlCrearPlato);
        
        // Paso 2. Completar los campos del formulario
        System.out.println("--- Paso 2: Completar campos del formulario ---");
        
        // Generar datos únicos para evitar conflictos
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nombrePlato = "Plato" + timestamp.substring(timestamp.length() - 4);
        String precioPlato = "15";
        String tiempoPreparacion = "15";
        String descripcionPlato = "descripción del plato";
        String imagenUrl = "https://img.freepik.com/foto-gratis/primer-plano-carne-asada-salsa-verduras-patatas-fritas-plato-sobre-mesa_181624-35847.jpg?semt=ais_hybrid&w=740&q=80";
        
        // Llenar campos del formulario
        WebElement campoNombre = driver.findElement(By.id("nombre"));
        WebElement campoPrecio = driver.findElement(By.id("precio"));
        WebElement campoTiempoPreparacion = driver.findElement(By.id("tiempoPreparacion"));
        WebElement campoDescripcion = driver.findElement(By.id("descripcion"));
        WebElement campoImagen = driver.findElement(By.xpath("//input[@type='url']"));
        
        campoNombre.sendKeys(nombrePlato);
        campoPrecio.sendKeys(precioPlato);
        campoTiempoPreparacion.sendKeys(tiempoPreparacion);
        campoDescripcion.sendKeys(descripcionPlato);
        campoImagen.sendKeys(imagenUrl);
        
        // Seleccionar categoría "Platos Principales"
        Select selectCategoria = new Select(driver.findElement(By.id("categoriaId")));
        selectCategoria.selectByVisibleText("Platos Principales");
        
        System.out.println("Campos completados:");
        System.out.println(" - Nombre: " + nombrePlato);
        System.out.println(" - Precio: " + precioPlato);
        System.out.println(" - Tiempo Preparación: " + tiempoPreparacion);
        System.out.println(" - Descripción: " + descripcionPlato);
        System.out.println(" - Categoría: Platos Principales");
        System.out.println(" - Imagen URL: " + imagenUrl);
        
        // Verificar que los campos tienen los datos ingresados
        Assert.assertEquals(campoNombre.getAttribute("value"), nombrePlato, "El campo nombre deberia tener el valor ingresado");
        Assert.assertEquals(campoPrecio.getAttribute("value"), precioPlato, "El campo precio deberia tener el valor ingresado");
        Assert.assertEquals(campoTiempoPreparacion.getAttribute("value"), tiempoPreparacion, "El campo tiempo preparación deberia tener el valor ingresado");
        Assert.assertEquals(campoDescripcion.getAttribute("value"), descripcionPlato, "El campo descripción deberia tener el valor ingresado");
        Assert.assertEquals(campoImagen.getAttribute("value"), imagenUrl, "El campo imagen deberia tener el valor ingresado");
        
        System.out.println("Resultado Paso 2: Campos llenos correctamente - OK");
        
        // Pequeña pausa antes de guardar
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Paso 3. Hacer clic en "Crear Plato"
        System.out.println("--- Paso 3: Crear plato ---");
        WebElement botonCrearPlato = driver.findElement(By.xpath("//button[.//span[contains(text(), 'Crear Plato')]]"));
        botonCrearPlato.click();
        
        // Esperar a que se procese la creación
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificación: SweetAlert de éxito
        System.out.println("--- Verificacion: Plato creado exitosamente ---");
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
        Assert.assertEquals(contenido, "Plato creado correctamente", 
                          "El mensaje deberia ser 'Plato creado correctamente'");
        
        System.out.println("Resultado Paso 3: Plato creado correctamente - OK");
        
        // Clic en Aceptar del SweetAlert
        WebElement botonAceptarSweetAlert = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
        botonAceptarSweetAlert.click();
        
        // Esperar a que se cierre el alert y cargue la página
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        /************** 3. Verificacion en menú del cliente ***************/
        
        // Paso 4. Verificar que el plato aparece en el menú del cliente
        System.out.println("--- Paso 4: Verificar plato en menú del cliente ---");
        
        // Ir al menú del cliente
        WebElement menuMenu = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Menú')]]"));
        menuMenu.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Buscar el plato en el menú por su nombre único
        boolean platoEnMenu = false;
        boolean precioCorrecto = false;
        boolean categoriaCorrecta = false;
        
        try {
            // Buscar el elemento que contiene el nombre del plato
            WebElement platoEnMenuElement = driver.findElement(By.xpath("//h3[contains(@class, 'text-gray-900') and contains(text(), '" + nombrePlato + "')]"));
            platoEnMenu = platoEnMenuElement.isDisplayed();
            
            // Verificar el precio
            WebElement precioElement = driver.findElement(By.xpath("//h3[contains(text(), '" + nombrePlato + "')]/following-sibling::span[contains(@class, 'text-primary-600')]"));
            String textoPrecio = precioElement.getText();
            precioCorrecto = textoPrecio.contains("$15.00");
            
            // Verificar la categoría
            WebElement categoriaElement = driver.findElement(By.xpath("//h3[contains(text(), '" + nombrePlato + "')]/ancestor::div[contains(@class, 'p-5')]//span[contains(@class, 'text-primary-500')]"));
            String textoCategoria = categoriaElement.getText();
            categoriaCorrecta = textoCategoria.equals("Platos Principales");
            
            System.out.println("Plato encontrado en menú: " + nombrePlato);
            System.out.println("Precio: " + textoPrecio);
            System.out.println("Categoría: " + textoCategoria);
            
        } catch (Exception e) {
            System.out.println("Plato NO encontrado en menú: " + nombrePlato);
        }
        
        // Verificación 2: El plato debe aparecer en el menú con los datos correctos
        Assert.assertTrue(platoEnMenu, "El nuevo plato deberia aparecer en el menú del cliente");
        Assert.assertTrue(precioCorrecto, "El precio deberia ser $15.00");
        Assert.assertTrue(categoriaCorrecta, "La categoría deberia ser 'Platos Principales'");
        
        System.out.println("Resultado Paso 4: Plato aparece en menú con datos correctos - OK");
        
        /************** 4. Resultado final ***************/
        System.out.println("TEST EXITOSO: Creación de plato completada correctamente");
        System.out.println("Plato creado: " + nombrePlato);
        System.out.println("Precio: $15.00");
        System.out.println("Categoría: Platos Principales");
        System.out.println("Todos los pasos se ejecutaron y verificaron exitosamente");
    }
}