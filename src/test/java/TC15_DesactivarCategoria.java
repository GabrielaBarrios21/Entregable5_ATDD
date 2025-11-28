import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.Assert;

/****************************************/
// TestCase 15: Verificar que el administrador pueda desactivar una categoría y que esta desaparezca del menú
//
// Paso 1. Crear una nueva categoría (preparación)
// Paso 2. Verificar que la categoría aparece en el menú del cliente
// Paso 3. En la lista de categorías, hacer clic en "Desactivar" y confirmar
// Paso 4. Verificar que la categoría desaparece del menú del cliente
/****************************************/

// Para ejecutar: mvn clean test -Dtest=TC15_DesactivarCategoria

public class TC15_DesactivarCategoria extends BaseTest {
    
    @Test
    public void testDesactivarCategoria() {
        
        /************** 1. Preparacion de la prueba - Login y crear categoría ***********/
        System.out.println("=== INICIANDO TEST: Desactivación de Categoría ===");
        
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
        
        // Crear una nueva categoría para la prueba
        System.out.println("--- Preparacion: Crear categoría para prueba ---");
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nombreCategoria = "Cat" + timestamp.substring(timestamp.length() - 4);
        String descripcionCategoria = "descripcion";
        
        // Ir a categorías
        WebElement menuCategorias = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Categorías')]]"));
        menuCategorias.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Hacer clic en "Nueva Categoría"
        WebElement botonNuevaCategoria = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Nueva Categoría')]]"));
        botonNuevaCategoria.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Llenar y crear categoría
        WebElement campoNombre = driver.findElement(By.id("nombre"));
        WebElement campoDescripcion = driver.findElement(By.id("descripcion"));
        campoNombre.sendKeys(nombreCategoria);
        campoDescripcion.sendKeys(descripcionCategoria);
        
        WebElement botonCrearCategoria = driver.findElement(By.xpath("//button[.//span[contains(text(), 'Crear Categoría')]]"));
        botonCrearCategoria.click();
        
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Cerrar SweetAlert de éxito
        WebElement botonAceptarSweetAlert = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
        botonAceptarSweetAlert.click();
        
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Categoría creada para prueba: " + nombreCategoria);
        
        /************** 2. Verificar que la categoría aparece en el menú ***********/
        
        System.out.println("--- Paso 2: Verificar categoría en menú del cliente ---");
        
        // Ir al menú del cliente
        WebElement menuMenu = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Menú')]]"));
        menuMenu.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que la categoría aparece en el menú
        boolean categoriaEnMenu = false;
        try {
            WebElement categoriaEnMenuElement = driver.findElement(By.xpath("//h2[contains(@class, 'text-3xl') and contains(text(), '" + nombreCategoria + "')]"));
            categoriaEnMenu = categoriaEnMenuElement.isDisplayed();
            System.out.println("Categoría encontrada en menú: " + nombreCategoria);
        } catch (Exception e) {
            System.out.println("Categoría NO encontrada en menú: " + nombreCategoria);
        }
        
        Assert.assertTrue(categoriaEnMenu, "La categoría deberia aparecer en el menú del cliente");
        System.out.println("Resultado Paso 2: Categoría aparece en menú - OK");
        
        /************** 3. Desactivar la categoría ***********/
        
        System.out.println("--- Paso 3: Desactivar categoría ---");
        
        // Volver a categorías
        WebElement menuCategorias2 = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Categorías')]]"));
        menuCategorias2.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Buscar y hacer clic en el botón "Desactivar" de la categoría específica
        WebElement botonDesactivar = driver.findElement(By.xpath("//h3[contains(text(), '" + nombreCategoria + "')]/ancestor::div[contains(@class, 'card')]//button[.//span[contains(text(), 'Desactivar')]]"));
        botonDesactivar.click();
        
        // Esperar a que aparezca el modal de confirmación
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Confirmar la desactivación en el modal
        System.out.println("--- Confirmando desactivación ---");
        WebElement modalConfirmacion = driver.findElement(By.cssSelector(".swal2-popup"));
        boolean modalVisible = modalConfirmacion.isDisplayed();
        
        Assert.assertTrue(modalVisible, "Deberia mostrarse modal de confirmación");
        
        // Hacer clic en confirmar (Aceptar)
        WebElement botonConfirmarDesactivar = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
        botonConfirmarDesactivar.click();
        
        // Esperar a que se procese la desactivación
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Resultado Paso 3: Categoría desactivada - OK");
        
        /************** 4. Verificar que desaparece del menú ***********/
        
        System.out.println("--- Paso 4: Verificar que desaparece del menú ---");
        
        // Ir al menú del cliente
        WebElement menuMenu2 = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Menú')]]"));
        menuMenu2.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que la categoría NO aparece en el menú
        boolean categoriaEnMenuDespues = true;
        try {
            WebElement categoriaEnMenuElement = driver.findElement(By.xpath("//h2[contains(@class, 'text-3xl') and contains(text(), '" + nombreCategoria + "')]"));
            categoriaEnMenuDespues = categoriaEnMenuElement.isDisplayed();
            System.out.println("ERROR: Categoría todavía aparece en menú: " + nombreCategoria);
        } catch (Exception e) {
            categoriaEnMenuDespues = false;
            System.out.println("Categoría correctamente desapareció del menú: " + nombreCategoria);
        }
        
        // Verificación: La categoría NO debe aparecer en el menú
        Assert.assertFalse(categoriaEnMenuDespues, "La categoría NO deberia aparecer en el menú del cliente después de desactivarla");
        
        System.out.println("Resultado Paso 4: Categoría desaparece del menú - OK");
        
        /************** 5. Resultado final ***********/
        System.out.println("TEST EXITOSO: Desactivación de categoría completada correctamente");
        System.out.println("Categoría: " + nombreCategoria);
        System.out.println("Estado cambiado de 'Activa' a 'Inactiva'");
        System.out.println("Categoría desapareció correctamente del menú del cliente");
    }
}