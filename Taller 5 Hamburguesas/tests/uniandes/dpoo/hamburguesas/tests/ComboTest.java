package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ComboTest {

    private ProductoMenu hamburguesa;
    private ProductoMenu papas;
    private ProductoMenu gaseosa;

    private ArrayList<ProductoMenu> items;
    private Combo combo;

    @BeforeEach
    public void setUp() {
        hamburguesa = new ProductoMenu("Hamburguesa", 20000);
        papas = new ProductoMenu("Papas Fritas", 8000);
        gaseosa = new ProductoMenu("Gaseosa", 5000);

        items = new ArrayList<>();
        items.add(hamburguesa);
        items.add(papas);
        items.add(gaseosa);

        // descuento del 0.9 (equivalente a cobrar el 90% del precio total)
        combo = new Combo("Combo Clásico", 0.9, items);
    }

    @Test
    public void testConstructorYGetNombre() {
        assertEquals("Combo Clásico", combo.getNombre(), "El nombre del combo no coincide");
    }

    @Test
    public void testGetPrecioCalculadoCorrectamente() {
        // Suma de los productos = 20000 + 8000 + 5000 = 33000
        // Descuento = 0.9 → precio esperado = 33000 * 0.9 = 29700
        int esperado = (int) (33000 * 0.9);
        assertEquals(esperado, combo.getPrecio(), "El precio del combo no aplica correctamente el descuento");
    }

    @Test
    public void testGetPrecioConListaVacia() {
        Combo comboVacio = new Combo("Combo Vacío", 0.5, new ArrayList<>());
        assertEquals(0, comboVacio.getPrecio(), "Un combo sin productos debe costar 0");
    }

    @Test
    public void testGenerarTextoFacturaContieneDatos() {
        String texto = combo.generarTextoFactura();

        assertTrue(texto.contains("Combo Combo Clásico"), "Debe contener el nombre del combo");
        assertTrue(texto.contains("Descuento"), "Debe mostrar el descuento");
        assertTrue(texto.contains(String.valueOf(combo.getPrecio())), "Debe mostrar el precio calculado");
    }

    @Test
    public void testDescuentoCero() {
        Combo sinDescuento = new Combo("Combo Sin Descuento", 0.0, items);
        assertEquals(0, sinDescuento.getPrecio(), "Con descuento 0.0 el precio debe ser 0");
    }
}
