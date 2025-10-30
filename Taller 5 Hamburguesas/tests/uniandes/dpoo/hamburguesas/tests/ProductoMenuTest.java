package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest {

    private ProductoMenu producto;

    @BeforeEach
    public void setUp() {
        producto = new ProductoMenu("Hamburguesa Clásica", 15000);
    }

    @Test
    public void testConstructorInicializaCorrectamente() {
        assertNotNull(producto, "El producto no debería ser nulo");
        assertEquals("Hamburguesa Clásica", producto.getNombre(), "El nombre no coincide con el esperado");
        assertEquals(15000, producto.getPrecio(), "El precio base no coincide con el esperado");
    }

    @Test
    public void testGetNombre() {
        assertEquals("Hamburguesa Clásica", producto.getNombre());
    }

    @Test
    public void testGetPrecio() {
        assertEquals(15000, producto.getPrecio());
    }

    @Test
    public void testGenerarTextoFactura() {
        String esperado = "Hamburguesa Clásica\n            15000\n";
        assertEquals(esperado, producto.generarTextoFactura());
    }

    @Test
    public void testGenerarTextoFacturaConValoresDiferentes() {
        ProductoMenu papas = new ProductoMenu("Papas Fritas", 5000);
        String esperado = "Papas Fritas\n            5000\n";
        assertEquals(esperado, papas.generarTextoFactura());
    }
}