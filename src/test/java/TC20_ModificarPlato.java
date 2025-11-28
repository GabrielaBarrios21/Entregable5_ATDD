import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;
import org.testng.Assert;

/****************************************/
// TestCase 20: Verificar que el administrador pueda modificar la información de un plato existente
//
// Paso 1. Crear un plato para la prueba (preparación)
// Paso 2. Seleccionar el plato y hacer clic en "Editar"
// Resultado Esperado: Se muestran los datos actuales
//
// Paso 3. Modificar nombre y descripción
// Resultado Esperado: Los campos permiten edición
//
// Paso 4. Guardar cambios
// Resultado Esperado: El sistema muestra mensaje "Plato actualizado correctamente"
//
// Paso 5. Verificar en la lista
// Resultado Esperado: Los cambios se reflejan adecuadamente
/****************************************/

// Para ejecutar: mvn clean test -Dtest=TC20_ModificarPlato

public class TC20_ModificarPlato extends BaseTest {
    
    @Test
    public void testModificarPlato() {
        
        /************** 1. Preparacion de la prueba - Login y crear plato ***********/
        System.out.println("=== INICIANDO TEST: Modificación de Plato Existente ===");
        
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
        
        // Crear un plato para la prueba
        System.out.println("--- Preparacion: Crear plato para prueba ---");
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nombrePlatoOriginal = "Plato" + timestamp.substring(timestamp.length() - 4);
        String precioPlato = "15";
        String tiempoPreparacion = "15";
        String descripcionPlatoOriginal = "descripción del plato";
        String imagenUrl = "https://img.freepik.com/foto-gratis/primer-plano-carne-asada-salsa-verduras-patatas-fritas-plato-sobre-mesa_181624-35847.jpg?semt=ais_hybrid&w=740&q=80";
        
        // Ir a platos
        WebElement menuPlatos = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Platos')]]"));
        menuPlatos.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Hacer clic en "Nuevo Plato"
        WebElement botonNuevoPlato = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Nuevo Plato')]]"));
        botonNuevoPlato.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Llenar y crear plato
        WebElement campoNombre = driver.findElement(By.id("nombre"));
        WebElement campoPrecio = driver.findElement(By.id("precio"));
        WebElement campoTiempoPreparacion = driver.findElement(By.id("tiempoPreparacion"));
        WebElement campoDescripcion = driver.findElement(By.id("descripcion"));
        WebElement campoImagen = driver.findElement(By.xpath("//input[@type='url']"));
        
        campoNombre.sendKeys(nombrePlatoOriginal);
        campoPrecio.sendKeys(precioPlato);
        campoTiempoPreparacion.sendKeys(tiempoPreparacion);
        campoDescripcion.sendKeys(descripcionPlatoOriginal);
        campoImagen.sendKeys(imagenUrl);
        
        // Seleccionar categoría "Platos Principales"
        Select selectCategoria = new Select(driver.findElement(By.id("categoriaId")));
        selectCategoria.selectByVisibleText("Platos Principales");
        
        WebElement botonCrearPlato = driver.findElement(By.xpath("//button[.//span[contains(text(), 'Crear Plato')]]"));
        botonCrearPlato.click();
        
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
        
        System.out.println("Plato creado para prueba: " + nombrePlatoOriginal);
        
        /************** 2. Modificar el plato ***************/
        
        // Paso 1. Seleccionar el plato y hacer clic en "Editar"
        System.out.println("--- Paso 1: Seleccionar plato y hacer clic en Editar ---");
        
        // Buscar y hacer clic en el botón "Editar" del plato específico
        WebElement botonEditar = driver.findElement(By.xpath("//h3[contains(text(), '" + nombrePlatoOriginal + "')]/ancestor::div[contains(@class, 'card')]//button[.//span[contains(text(), 'Editar')]]"));
        botonEditar.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que estamos en la página de edición
        String urlEditarPlato = driver.getCurrentUrl();
        Assert.assertTrue(urlEditarPlato.contains("/gestion-platos/editar"), "Deberia estar en la pagina de editar plato");
        System.out.println("Resultado Paso 1: Se muestran los datos actuales - OK");
        System.out.println("URL actual: " + urlEditarPlato);
        
        // Verificar que los campos tienen los datos originales
        WebElement campoNombreEditar = driver.findElement(By.id("nombre"));
        WebElement campoDescripcionEditar = driver.findElement(By.id("descripcion"));
        
        Assert.assertEquals(campoNombreEditar.getAttribute("value"), nombrePlatoOriginal, "El campo nombre deberia tener el valor original");
        Assert.assertEquals(campoDescripcionEditar.getAttribute("value"), descripcionPlatoOriginal, "El campo descripción deberia tener el valor original");
        
        // Paso 2. Modificar nombre y descripción
        System.out.println("--- Paso 2: Modificar nombre y descripción ---");
        
        String nombrePlatoEditado = "Editado" + timestamp.substring(timestamp.length() - 4);
        String descripcionPlatoEditado = "plato editado";
        
        // Limpiar y modificar campos
        campoNombreEditar.clear();
        campoNombreEditar.sendKeys(nombrePlatoEditado);
        
        campoDescripcionEditar.clear();
        campoDescripcionEditar.sendKeys(descripcionPlatoEditado);
        
        System.out.println("Campos modificados:");
        System.out.println(" - Nombre: " + nombrePlatoOriginal + " → " + nombrePlatoEditado);
        System.out.println(" - Descripción: " + descripcionPlatoOriginal + " → " + descripcionPlatoEditado);
        
        // Verificar que los campos tienen los nuevos datos
        Assert.assertEquals(campoNombreEditar.getAttribute("value"), nombrePlatoEditado, "El campo nombre deberia tener el nuevo valor");
        Assert.assertEquals(campoDescripcionEditar.getAttribute("value"), descripcionPlatoEditado, "El campo descripción deberia tener el nuevo valor");
        
        System.out.println("Resultado Paso 2: Campos modificados correctamente - OK");
        
        // Pequeña pausa antes de guardar
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Paso 3. Guardar cambios
        System.out.println("--- Paso 3: Guardar cambios ---");
        
        // Intentar diferentes selectores para el botón de actualización
        WebElement botonActualizarPlato = null;
        try {
            // Primero intentar con "Actualizar Plato"
            botonActualizarPlato = driver.findElement(By.xpath("//button[.//span[contains(text(), 'Actualizar Plato')]]"));
        } catch (Exception e) {
            try {
                // Si no funciona, intentar con "Guardar Cambios"
                botonActualizarPlato = driver.findElement(By.xpath("//button[.//span[contains(text(), 'Guardar Cambios')]]"));
            } catch (Exception e2) {
                try {
                    // Si tampoco funciona, buscar por tipo submit
                    botonActualizarPlato = driver.findElement(By.xpath("//button[@type='submit']"));
                } catch (Exception e3) {
                    // Último intento: buscar cualquier botón que contenga "Actualizar" o "Guardar"
                    botonActualizarPlato = driver.findElement(By.xpath("//button[contains(., 'Actualizar') or contains(., 'Guardar')]"));
                }
            }
        }
        
        botonActualizarPlato.click();
        
        // Esperar a que se procese la actualización
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificación: SweetAlert de éxito
        System.out.println("--- Verificacion: Plato actualizado exitosamente ---");
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
        Assert.assertEquals(contenido, "Plato actualizado correctamente", 
                          "El mensaje deberia ser 'Plato actualizado correctamente'");
        
        System.out.println("Resultado Paso 3: Plato actualizado correctamente - OK");
        
        // Clic en Aceptar del SweetAlert
        WebElement botonAceptarActualizacion = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
        botonAceptarActualizacion.click();
        
        // Esperar a que se cierre el alert y cargue la página
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        /************** 3. Verificacion en lista de platos ***************/
        
        // Paso 4. Verificar en la lista
        System.out.println("--- Paso 4: Verificar cambios en lista ---");
        
        // Verificar que estamos en la página de gestión de platos
        String urlFinal = driver.getCurrentUrl();
        Assert.assertTrue(urlFinal.contains("/gestion-platos"), "Deberia estar en la pagina de gestion de platos");
        
        // Buscar el plato editado en la lista por su nuevo nombre
        boolean platoEditadoEncontrado = false;
        boolean descripcionEditadaCorrecta = false;

        try {
            // Buscar la tarjeta completa del plato por su nombre
            WebElement cardPlato = driver.findElement(By.xpath(
                "//h3[contains(text(), '" + nombrePlatoEditado + "')]/ancestor::div[contains(@class, 'card')]"
            ));
            
            platoEditadoEncontrado = cardPlato.isDisplayed();
            
            // Buscar la descripción DENTRO de la misma tarjeta
            WebElement descripcionEditada = cardPlato.findElement(By.xpath(".//p[contains(@class, 'text-gray-600')]"));
            String descripcionActual = descripcionEditada.getText().trim();
            
            descripcionEditadaCorrecta = descripcionActual.equals(descripcionPlatoEditado);
            
            System.out.println("Plato editado encontrado en lista: " + nombrePlatoEditado);
            System.out.println("Descripción encontrada: '" + descripcionActual + "'");
            System.out.println("Descripción esperada: '" + descripcionPlatoEditado + "'");
            System.out.println("Descripción editada correcta: " + descripcionEditadaCorrecta);
            
        } catch (Exception e) {
            System.out.println("Plato editado NO encontrado en lista: " + nombrePlatoEditado);
            System.out.println("Error: " + e.getMessage());
        }
        
        // Verificación 2: El plato editado debe aparecer en la lista con los nuevos datos
        Assert.assertTrue(platoEditadoEncontrado, "El plato editado deberia aparecer en la lista con el nuevo nombre");
        Assert.assertTrue(descripcionEditadaCorrecta, "La descripción editada deberia ser correcta");
        
        // Verificación adicional: El plato original NO debe aparecer
        boolean platoOriginalEncontrado = true;
        try {
            WebElement platoOriginal = driver.findElement(By.xpath("//h3[contains(text(), '" + nombrePlatoOriginal + "')]"));
            platoOriginalEncontrado = platoOriginal.isDisplayed();
            System.out.println("ERROR: Plato original todavía aparece en lista: " + nombrePlatoOriginal);
        } catch (Exception e) {
            platoOriginalEncontrado = false;
            System.out.println("Plato original correctamente reemplazado: " + nombrePlatoOriginal);
        }
        
        Assert.assertFalse(platoOriginalEncontrado, "El plato original NO deberia aparecer en la lista");
        
        System.out.println("Resultado Paso 4: Cambios reflejados correctamente en lista - OK");
        
        /************** 4. Resultado final ***************/
        System.out.println("TEST EXITOSO: Modificación de plato completada correctamente");
        System.out.println("Plato modificado: " + nombrePlatoOriginal + " → " + nombrePlatoEditado);
        System.out.println("Descripción modificada: " + descripcionPlatoOriginal + " → " + descripcionPlatoEditado);
        System.out.println("Todos los cambios se reflejaron adecuadamente en la lista");
    }
}