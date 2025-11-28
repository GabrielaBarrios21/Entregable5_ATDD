import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;
import org.testng.Assert;

/****************************************/
// TestCase 9: Verificar que el cajero pueda generar la cuenta de un pedido 
// que se encuentra en estado ENTREGADO.
//
// Precondiciones:
// - Usuario cajero con sesión iniciada
// - Existir un pedido en estado ENTREGADO
// - El pedido debe tener detalle registrado
//
// Paso 1. Ingresar al sistema como Cajero
// Resultado Esperado: Se muestra la barra de navegación de cajero
//
// Paso 2. Acceder a "Pedidos Pendientes de Pago"
// Resultado Esperado: Aparecen pedidos LISTO y ENTREGADO
//
// Paso 3. Seleccionar un pedido en estado ENTREGADO
// Resultado Esperado: Se muestra el formulario con el detalle del pedido
//
// Paso 4. Verificar el detalle mostrado
// Resultado Esperado: Los productos, cantidades y subtotales se muestran correctamente
//
// Paso 5. Verificar el total calculado
// Resultado Esperado: El monto coincide con la suma de los subtotales
//
// Paso 6. Hacer clic en "Generar Cuenta"
// Resultado Esperado: Se genera la cuenta sin errores
//
// Paso 7. Visualizar el comprobante generado
// Resultado Esperado: El sistema muestra la cuenta imprimible o descargable
/****************************************/

// Para ejecutar: mvn clean test -Dtest=TC09_GenerarCuentaPedidoEntregado

public class TC09_GenerarCuentaPedidoEntregado extends BaseTest {
    
    // Variables para almacenar información del pedido
    private String numeroPedido;
    private String mesaSeleccionada;
    
    @Test
    public void testGenerarCuentaPedidoEntregado() {
        
        /************** 1. Preparación: Crear pedido y llevarlo a estado ENTREGADO ***************/
        System.out.println("=== INICIANDO TEST: Generar Cuenta de Pedido ENTREGADO ===");
        
        // Primero crear un pedido como mesero 
        crearPedidoComoMesero();
        
        // Luego procesar el pedido como cocinero hasta estado LISTO
        // y finalmente el mesero lo marca como ENTREGADO
        procesarPedidoComoCocinero();
        
        /************** 2. Ejecución del Test Principal: Cajero genera cuenta ***************/
        
        // Paso 1. Ingresar al sistema como Cajero
        System.out.println("--- Paso 1: Ingresar como Cajero ---");
        loginComoCajero();
        
        // Verificar barra de navegación de cajero
        WebElement navCajero = driver.findElement(By.xpath("//nav[contains(@class, 'bg-white')]"));
        Assert.assertTrue(navCajero.isDisplayed(), "Deberia mostrarse la barra de navegacion de cajero");
        System.out.println("Resultado Paso 1: Barra de navegación de cajero visible - OK");
        
        // Paso 2. Acceder a "Pedidos Pendientes de Pago"
        System.out.println("--- Paso 2: Acceder a Pedidos Pendientes de Pago ---");
        WebElement menuPedidos = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Pedidos')]]"));
        menuPedidos.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que aparecen pedidos ENTREGADO
        boolean hayPedidosEntregado = false;
        
        try {
            WebElement pedidoEntregado = driver.findElement(By.xpath("//span[contains(text(), 'ENTREGADO')]"));
            hayPedidosEntregado = pedidoEntregado.isDisplayed();
            System.out.println("Se encontraron pedidos en estado ENTREGADO");
        } catch (Exception e) {
            System.out.println("No se encontraron pedidos en estado ENTREGADO");
        }
        
        Assert.assertTrue(hayPedidosEntregado, "Deberian aparecer pedidos ENTREGADO para generar cuenta");
        System.out.println("Resultado Paso 2: Aparecen pedidos ENTREGADO - OK");
        
        // Paso 3. Seleccionar un pedido en estado ENTREGADO
        System.out.println("--- Paso 3: Seleccionar pedido en estado ENTREGADO ---");
        
        // Buscar y seleccionar el pedido ENTREGADO que creamos
        WebElement pedidoTarget = null;
        try {
            // Buscar por número de pedido en estado ENTREGADO
            pedidoTarget = driver.findElement(By.xpath(
                "//h3[contains(text(), '" + numeroPedido + "')]/ancestor::div[contains(@class, 'card') or contains(@class, 'bg-white')][.//span[contains(text(), 'ENTREGADO')]]"
            ));
        } catch (Exception e) {
            // Si no encuentra por número, buscar cualquier pedido ENTREGADO
            try {
                pedidoTarget = driver.findElement(By.xpath(
                    "//span[contains(text(), 'ENTREGADO')]/ancestor::div[contains(@class, 'card') or contains(@class, 'bg-white')]"
                ));
            } catch (Exception e2) {
                Assert.fail("No se encontró ningún pedido en estado ENTREGADO");
            }
        }
        
        Assert.assertNotNull(pedidoTarget, "Deberia existir al menos un pedido ENTREGADO");
        pedidoTarget.click();
        
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que se muestra el detalle del pedido
        WebElement detallePedido = driver.findElement(By.xpath("//h4[contains(text(), 'Detalles:')]"));
        Assert.assertTrue(detallePedido.isDisplayed(), "Deberia mostrarse el formulario con detalle del pedido");
        System.out.println("Resultado Paso 3: Se muestra formulario con detalle del pedido - OK");
        
        // Paso 4. Verificar el detalle mostrado
        System.out.println("--- Paso 4: Verificar detalle del pedido ---");
        
        // Verificar productos y cantidades
        boolean platoPacenoEncontrado = verificarProductoEnDetalle("Plato Paceño", "1", "30");
        boolean salchipapaEncontrado = verificarProductoEnDetalle("Salchipapa", "2", "24"); 
        boolean heladoEncontrado = verificarProductoEnDetalle("Helado", "1", "10");
        
        Assert.assertTrue(platoPacenoEncontrado, "Deberia mostrarse Plato Paceño en el detalle");
        Assert.assertTrue(salchipapaEncontrado, "Deberia mostrarse Salchipapa en el detalle");
        Assert.assertTrue(heladoEncontrado, "Deberia mostrarse Helado en el detalle");
        
        System.out.println("Resultado Paso 4: Productos, cantidades y subtotales correctos - OK");
        
        // Paso 5. Verificar el total calculado
        System.out.println("--- Paso 5: Verificar total calculado ---");
        
        // Obtener el total mostrado
        WebElement totalElement = driver.findElement(By.xpath("//p[contains(text(), 'S/ 64')] | //span[contains(text(), 'S/ 64')]"));
        String totalTexto = totalElement.getText().replace("S/", "").trim();
        double totalMostrado = Double.parseDouble(totalTexto);
        
        // Calcular suma esperada (30*1 + 12*2 + 10*1 = 64)
        double totalEsperado = 64.00;
        
        Assert.assertEquals(totalMostrado, totalEsperado, 0.01, "El total deberia coincidir con la suma de subtotales");
        System.out.println("Total calculado: S/ " + totalMostrado + " - OK");
        System.out.println("Resultado Paso 5: Monto coincide con suma de subtotales - OK");
        
        // Paso 6. Hacer clic en "Generar Cuenta"
        System.out.println("--- Paso 6: Hacer clic en Generar Cuenta ---");
        
        WebElement botonGenerarCuenta = driver.findElement(By.xpath("//button[contains(text(), 'Generar Cuenta')]"));
        botonGenerarCuenta.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que se generó la cuenta sin errores
        boolean cuentaGenerada = false;
        try {
            WebElement botonImprimir = driver.findElement(By.xpath("//button[contains(text(), 'Imprimir Cuenta')]"));
            WebElement botonProcederPago = driver.findElement(By.xpath("//button[contains(text(), 'Proceder al Pago')]"));
            cuentaGenerada = botonImprimir.isDisplayed() && botonProcederPago.isDisplayed();
        } catch (Exception e) {
            // Alternativa: verificar si sigue en la misma página pero con opciones de cuenta
            cuentaGenerada = driver.getCurrentUrl().contains("/cuenta") || 
                           driver.findElements(By.xpath("//button[contains(text(), 'Imprimir')]")).size() > 0;
        }
        
        Assert.assertTrue(cuentaGenerada, "Deberia generarse la cuenta sin errores");
        System.out.println("Resultado Paso 6: Cuenta generada sin errores - OK");
        
        // Paso 7. Visualizar el comprobante generado
        System.out.println("--- Paso 7: Visualizar comprobante generado ---");
        
        // Verificar botones de impresión y pago
        WebElement botonImprimirCuenta = driver.findElement(By.xpath("//button[contains(text(), 'Imprimir Cuenta')]"));
        WebElement botonProcederPago = driver.findElement(By.xpath("//button[contains(text(), 'Proceder al Pago')]"));
        
        Assert.assertTrue(botonImprimirCuenta.isDisplayed(), "Deberia mostrarse boton Imprimir Cuenta");
        Assert.assertTrue(botonProcederPago.isDisplayed(), "Deberia mostrarse boton Proceder al Pago");
        
        System.out.println("Botones disponibles:");
        System.out.println(" - " + botonImprimirCuenta.getText());
        System.out.println(" - " + botonProcederPago.getText());
        
        System.out.println("Resultado Paso 7: Sistema muestra cuenta imprimible/descargable - OK");
        
        /************** 3. Resultado final ***************/
        System.out.println("TEST EXITOSO: Cuenta generada correctamente para pedido en estado ENTREGADO");
        System.out.println("Pedido: " + numeroPedido);
        System.out.println("Mesa: " + mesaSeleccionada);
        System.out.println("Total: S/ " + totalEsperado);
        System.out.println("Comprobante disponible para impresión y pago");
    }
    
    /************** MÉTODOS AUXILIARES ***************/
    
    private void crearPedidoComoMesero() {
        System.out.println("--- PREPARACION: Creando pedido como mesero ---");
        
        // Login como mesero
        driver.get("http://localhost:5173/");
        
        WebElement loginLink = driver.findElement(By.xpath("//a[contains(text(), 'Iniciar Sesión')]"));
        loginLink.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        WebElement campoUsuario = driver.findElement(By.id("username"));
        WebElement campoPassword = driver.findElement(By.id("password"));
        campoUsuario.sendKeys("mesero1");
        campoPassword.sendKeys("Password123!");
        
        WebElement botonLogin = driver.findElement(By.xpath("//button//span[contains(text(), 'Iniciar Sesión')]"));
        botonLogin.click();
        
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Cerrar SweetAlert de login
        try {
            WebElement botonAceptar = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
            botonAceptar.click();
        } catch (Exception e) {
            // Continuar si no hay alert
        }
        
        // Ir a Pedidos -> Nuevo Pedido
        WebElement menuPedidos = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Pedidos')]]"));
        menuPedidos.click();
        
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        WebElement botonNuevoPedido = driver.findElement(By.xpath("//a[contains(@href, '/tomar-pedido')]"));
        botonNuevoPedido.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Seleccionar mesa
        mesaSeleccionada = "M10";
        Select selectMesa = new Select(driver.findElement(By.tagName("select")));
        selectMesa.selectByVisibleText(mesaSeleccionada);
        
        // Seleccionar platos
        seleccionarPlato("Plato Paceño", 1);
        seleccionarPlato("Salchipapa", 2); 
        seleccionarPlato("Helado", 1);
        
        // Crear pedido
        WebElement botonCrearPedido = driver.findElement(By.xpath("//button[contains(text(), 'Crear Pedido')]"));
        botonCrearPedido.click();
        
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar SweetAlert de éxito
        WebElement sweetAlert = driver.findElement(By.cssSelector(".swal2-popup.swal2-icon-success"));
        WebElement alertTitle = driver.findElement(By.cssSelector(".swal2-title"));
        Assert.assertEquals(alertTitle.getText(), "Éxito", "Deberia mostrarse alerta de éxito");
        
        // Cerrar alerta
        WebElement botonAceptar = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
        botonAceptar.click();
        
        // Obtener número de pedido (de la lista)
        try {
            TimeUnit.SECONDS.sleep(2);
            WebElement ultimoPedido = driver.findElement(By.xpath("(//h3[contains(text(), 'Pedido #')])[1]"));
            numeroPedido = ultimoPedido.getText().replace("Pedido #", "").trim();
            System.out.println("Pedido creado: " + numeroPedido);
        } catch (Exception e) {
            numeroPedido = "ORD" + System.currentTimeMillis();
            System.out.println("No se pudo obtener número de pedido, usando: " + numeroPedido);
        }
        
        // Cerrar sesión
        cerrarSesion();
    }
    
    private void procesarPedidoComoCocinero() {
        System.out.println("--- PREPARACION: Procesando pedido como cocinero ---");
        
        // Login como cocinero
        driver.get("http://localhost:5173/");
        
        WebElement loginLink = driver.findElement(By.xpath("//a[contains(text(), 'Iniciar Sesión')]"));
        loginLink.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        WebElement campoUsuario = driver.findElement(By.id("username"));
        WebElement campoPassword = driver.findElement(By.id("password"));
        campoUsuario.sendKeys("cocinero1");
        campoPassword.sendKeys("Password123!");
        
        WebElement botonLogin = driver.findElement(By.xpath("//button//span[contains(text(), 'Iniciar Sesión')]"));
        botonLogin.click();
        
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Cerrar SweetAlert de login
        try {
            WebElement botonAceptar = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
            botonAceptar.click();
        } catch (Exception e) {
            // Continuar si no hay alert
        }
        
        // Ir a Pedidos Cocinero
        WebElement menuPedidosCocinero = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Pedidos')]]"));
        menuPedidosCocinero.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Buscar y procesar el pedido
        WebElement pedido = driver.findElement(By.xpath("//p[contains(text(), '" + mesaSeleccionada + "')]/ancestor::div[contains(@class, 'card')]"));
        
        // Iniciar preparación
        WebElement botonIniciarPreparacion = pedido.findElement(By.xpath(".//button[contains(text(), 'Iniciar Preparación')]"));
        botonIniciarPreparacion.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Confirmar SweetAlert
        WebElement botonAceptarPreparacion = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
        botonAceptarPreparacion.click();
        
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Marcar como listo
        WebElement botonMarcarListo = driver.findElement(By.xpath("//button[contains(text(), 'Marcar como Listo')]"));
        botonMarcarListo.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Confirmar SweetAlert
        WebElement botonAceptarListo = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
        botonAceptarListo.click();
        
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Pedido procesado hasta estado LISTO");
        
        // Cerrar sesión como cocinero
        cerrarSesion();
        
        // Ahora el MESERO marca como ENTREGADO
        System.out.println("--- PREPARACION: Mesero marca pedido como ENTREGADO ---");
        marcarPedidoComoEntregado();
    }
    
    private void marcarPedidoComoEntregado() {
        // Login como mesero
        driver.get("http://localhost:5173/");
        
        WebElement loginLink = driver.findElement(By.xpath("//a[contains(text(), 'Iniciar Sesión')]"));
        loginLink.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        WebElement campoUsuario = driver.findElement(By.id("username"));
        WebElement campoPassword = driver.findElement(By.id("password"));
        campoUsuario.sendKeys("mesero1");
        campoPassword.sendKeys("Password123!");
        
        WebElement botonLogin = driver.findElement(By.xpath("//button//span[contains(text(), 'Iniciar Sesión')]"));
        botonLogin.click();
        
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Cerrar SweetAlert de login
        try {
            WebElement botonAceptar = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
            botonAceptar.click();
        } catch (Exception e) {
            // Continuar si no hay alert
        }
        
        // Ir a Pedidos del mesero
        WebElement menuPedidos = driver.findElement(By.xpath("//a[.//span[contains(text(), 'Pedidos')]]"));
        menuPedidos.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Buscar el pedido en estado LISTO (debería tener botón "Marcar Entregado")
        WebElement pedidoListo = null;
        try {
            // Buscar por mesa específica
            pedidoListo = driver.findElement(By.xpath(
                "//p[contains(text(), '" + mesaSeleccionada + "')]/ancestor::div[contains(@class, 'card')][.//span[contains(text(), 'Listo')]]"
            ));
        } catch (Exception e) {
            // Si no encuentra por mesa, buscar cualquier pedido LISTO
            pedidoListo = driver.findElement(By.xpath(
                "//span[contains(text(), 'Listo')]/ancestor::div[contains(@class, 'card')]"
            ));
        }
        
        Assert.assertNotNull(pedidoListo, "Deberia existir al menos un pedido en estado LISTO");
        
        // Hacer clic en "Marcar Entregado"
        WebElement botonMarcarEntregado = pedidoListo.findElement(By.xpath(".//button[contains(text(), 'Marcar Entregado')]"));
        botonMarcarEntregado.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar SweetAlert de éxito
        WebElement sweetAlert = driver.findElement(By.cssSelector(".swal2-popup.swal2-icon-success"));
        WebElement alertTitle = driver.findElement(By.cssSelector(".swal2-title"));
        WebElement alertContent = driver.findElement(By.cssSelector(".swal2-html-container"));
        
        Assert.assertEquals(alertTitle.getText(), "Éxito", "Deberia mostrarse alerta de éxito");
        Assert.assertTrue(alertContent.getText().contains("entregado") || alertContent.getText().contains("Entregado"), 
                         "Deberia confirmar que el pedido fue marcado como entregado");
        
        System.out.println("SweetAlert de entrega: " + alertTitle.getText() + " - " + alertContent.getText());
        
        // Cerrar SweetAlert
        WebElement botonAceptarEntrega = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
        botonAceptarEntrega.click();
        
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Pedido marcado como ENTREGADO por el mesero");
        
        // Cerrar sesión como mesero
        cerrarSesion();
    }
    
    private void loginComoCajero() {
        driver.get("http://localhost:5173/");
        
        WebElement loginLink = driver.findElement(By.xpath("//a[contains(text(), 'Iniciar Sesión')]"));
        loginLink.click();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        WebElement campoUsuario = driver.findElement(By.id("username"));
        WebElement campoPassword = driver.findElement(By.id("password"));
        campoUsuario.sendKeys("cajero1");
        campoPassword.sendKeys("Password123!");
        
        WebElement botonLogin = driver.findElement(By.xpath("//button//span[contains(text(), 'Iniciar Sesión')]"));
        botonLogin.click();
        
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        // Cerrar SweetAlert de login si existe
        try {
            WebElement botonAceptar = driver.findElement(By.xpath("//button[contains(@class, 'swal2-confirm')]"));
            botonAceptar.click();
        } catch (Exception e) {
            // Continuar si no hay alert
        }
    }
    
    private void seleccionarPlato(String nombrePlato, int cantidad) {
        try {
            // Buscar el plato por nombre
            WebElement plato = driver.findElement(By.xpath("//h3[contains(text(), '" + nombrePlato + "')]/ancestor::div[contains(@class, 'p-4') or contains(@class, 'hover:bg-gray-50')]"));
            
            // Hacer clic en el botón "+" la cantidad de veces necesaria
            WebElement botonMas = plato.findElement(By.xpath(".//button[not(@disabled) and text()=' + ']"));
            for (int i = 0; i < cantidad; i++) {
                botonMas.click();
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            System.out.println("Seleccionado: " + cantidad + "x " + nombrePlato);
            
        } catch (Exception e) {
            System.out.println("No se pudo seleccionar: " + nombrePlato);
        }
    }
    
    private boolean verificarProductoEnDetalle(String producto, String cantidadEsperada, String subtotalEsperado) {
        try {
            // Buscar el producto en el detalle
            WebElement productoElement = driver.findElement(By.xpath(
                "//div[contains(text(), '" + cantidadEsperada + "x " + producto + "')] | " +
                "//span[contains(text(), '" + cantidadEsperada + "x " + producto + "')]"
            ));
            
            // Verificar subtotal
            WebElement subtotalElement = driver.findElement(By.xpath(
                "//div[contains(text(), '" + cantidadEsperada + "x " + producto + "')]/following-sibling::div[contains(text(), 'S/ " + subtotalEsperado + "')] | " +
                "//span[contains(text(), '" + cantidadEsperada + "x " + producto + "')]/following-sibling::span[contains(text(), 'S/ " + subtotalEsperado + "')]"
            ));
            
            return productoElement.isDisplayed() && subtotalElement.isDisplayed();
            
        } catch (Exception e) {
            System.out.println("Producto no encontrado en detalle: " + producto);
            return false;
        }
    }
    
    private void cerrarSesion() {
        try {
            WebElement botonSalir = driver.findElement(By.xpath("//button[.//span[contains(text(), 'Salir')]]"));
            botonSalir.click();
            
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            
            // Confirmar cierre de sesión
            WebElement botonConfirmarSalir = driver.findElement(By.xpath("//button[contains(text(), 'Sí, salir')]"));
            botonConfirmarSalir.click();
            
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            System.out.println("No se pudo cerrar sesión: " + e.getMessage());
        }
    }
}