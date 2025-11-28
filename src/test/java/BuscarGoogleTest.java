import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.Assert;

/****************************************/
// Historia de Usuario: Como usuario quiero verificar que el boton "Buscar con Google" se despliega
//
// Prueba de Aceptacion: Verificar que el boton de busqueda tenga el texto "Buscar con Google"
//
// Paso 1. Ingresar a la pagina de Google: https://www.google.com
// Paso 2. Buscar el boton "Buscar con Google"
// 
// Resultado Esperado: El boton "Buscar con Google" debe estar presente y ser visible
/****************************************/

public class BuscarGoogleTest extends BaseTest {
    
    @Test
    public void paginaPrincipalGoogle(){
        
        /************** 1. Preparacion de la prueba ***********/
    	
    	// Paso 1. Ingresar a la pagina de Google
        String googleUrl = "https://www.google.com";
        driver.get(googleUrl);
        
        /************** 2. Logica de la prueba ***************/
        // Paso 2. Buscar el boton "Buscar con Google"
        WebElement boton = driver.findElement(By.name("btnK"));

        try{
            TimeUnit.SECONDS.sleep(3);
        }
        catch(InterruptedException e){
        	e.printStackTrace();
        }    
        
        String txtBoton = boton.getAttribute("value");
        System.out.println("Texto del boton:: "+txtBoton);
        
        /************ 3. Verificacion de la situacion esperada - Assert ***************/
        Assert.assertEquals(txtBoton, "Buscar con Google");
    }
}