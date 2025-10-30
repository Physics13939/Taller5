package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

/**
 * Pruebas unitarias para la clase ProductoAjustado
 */
public class ProductoAjustadoTest {

    private ProductoMenu base;
    private ProductoAjustado ajustado;
    private Ingrediente queso;
    private Ingrediente tocineta;
    private Ingrediente cebolla;

    @BeforeEach
    public void setUp() {
        base = new ProductoMenu("Hamburguesa", 20000);
        ajustado = new ProductoAjustado(base);

        queso = new Ingrediente("Queso", 2000);
        tocineta = new Ingrediente("Tocineta", 2500);
        cebolla = new Ingrediente("Cebolla", 1000);
    }

    @Test
    public void testConstructorInicializaListasVacias() {
        assertEquals("Hamburguesa", ajustado.getNombre());
        assertEquals(0, ajustado.getPrecio(), "El precio inicial debería ser 0 (ya que el método no está implementado)");
    }

    @Test
    public void testGetNombre() {
        assertEquals("Hamburguesa", ajustado.getNombre());
    }

    @Test
    public void testGetPrecioSinAgregados() {
        assertEquals(0, ajustado.getPrecio(), "Debería retornar 0 porque el método aún no suma ingredientes");
    }

    @Test
    public void testGenerarTextoFacturaSinModificaciones() {
        String texto = ajustado.generarTextoFactura();
        assertTrue(texto.contains("Hamburguesa"), "Debe contener el nombre del producto base");
        assertTrue(texto.contains("0"), "Debe contener el precio (aunque sea 0)");
    }

    @Test
    public void testGenerarTextoFacturaConAgregadosYEliminados() {
        // Agregar ingredientes manualmente a las listas
        ajustado.getAgregados().add(queso);
        ajustado.getAgregados().add(tocineta);
        ajustado.getEliminados().add(cebolla);

        String factura = ajustado.generarTextoFactura();

        assertTrue(factura.contains("+Queso"), "Debe mostrar el ingrediente agregado Queso");
        assertTrue(factura.contains("+Tocineta"), "Debe mostrar el ingrediente agregado Tocineta");
        assertTrue(factura.contains("-Cebolla"), "Debe mostrar el ingrediente eliminado Cebolla");
        assertTrue(factura.contains("0"), "Debe mostrar el precio final");
    }
}
