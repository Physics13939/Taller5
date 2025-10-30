package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.*;
import org.junit.jupiter.api.*;

import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class PedidoTest {

    private Pedido pedido;
    private ProductoMenu hamburguesa;
    private ProductoMenu papas;

    @BeforeEach
    public void setUp() {
        // Reiniciar contador estático de pedidos antes de cada prueba
        try {
            var field = Pedido.class.getDeclaredField("numeroPedidos");
            field.setAccessible(true);
            field.setInt(null, 0);
        } catch (Exception e) {
            fail("No se pudo reiniciar el contador de pedidos: " + e.getMessage());
        }

        pedido = new Pedido("Juan Pérez", "Calle 123");
        hamburguesa = new ProductoMenu("Hamburguesa", 20000);
        papas = new ProductoMenu("Papas Fritas", 8000);
    }

    @Test
    public void testConstructorInicializaCampos() {
        assertEquals("Juan Pérez", pedido.getNombreCliente());
        assertEquals(0, pedido.getIdPedido(), "El primer pedido debe tener ID 0");

        // Crear otro pedido para verificar incremento del contador
        Pedido pedido2 = new Pedido("Laura", "Carrera 45");
        assertEquals(1, pedido2.getIdPedido(), "El ID del segundo pedido debe ser 1");
    }

    @Test
    public void testAgregarProductoYTotalConIVA() {
        pedido.agregarProducto(hamburguesa);
        pedido.agregarProducto(papas);

        // Precio neto: 20000 + 8000 = 28000
        // IVA (19%): 5320 → total esperado = 33320
        int esperado = 28000 + (int)(28000 * 0.19);
        assertEquals(esperado, pedido.getPrecioTotalPedido(), "El total con IVA no coincide");
    }

    @Test
    public void testGenerarTextoFacturaContieneTodo() {
        pedido.agregarProducto(hamburguesa);
        pedido.agregarProducto(papas);

        String factura = pedido.generarTextoFactura();

        assertTrue(factura.contains("Cliente: Juan Pérez"), "Debe incluir el nombre del cliente");
        assertTrue(factura.contains("Dirección: Calle 123"), "Debe incluir la dirección");
        assertTrue(factura.contains("Hamburguesa"), "Debe incluir el producto Hamburguesa");
        assertTrue(factura.contains("Papas Fritas"), "Debe incluir el producto Papas Fritas");
        assertTrue(factura.contains("Precio Neto"), "Debe incluir el precio neto");
        assertTrue(factura.contains("IVA"), "Debe incluir el IVA");
        assertTrue(factura.contains("Precio Total"), "Debe incluir el precio total");
    }

    @Test
    public void testGuardarFacturaCreaArchivoCorrectamente() throws Exception {
        pedido.agregarProducto(hamburguesa);
        File archivo = File.createTempFile("factura_test", ".txt");
        archivo.deleteOnExit();

        pedido.guardarFactura(archivo);

        assertTrue(archivo.exists(), "El archivo de factura debe existir");
        String contenido = new String(java.nio.file.Files.readAllBytes(archivo.toPath()));

        assertTrue(contenido.contains("Hamburguesa"), "El archivo debe contener los productos");
        assertTrue(contenido.contains("Precio Total"), "El archivo debe incluir el total");
    }

    @Test
    public void testGuardarFacturaLanzaExcepcionSiArchivoInvalido() {
        pedido.agregarProducto(hamburguesa);
        File archivo = new File("/ruta/que/no/existe/factura.txt");

        assertThrows(FileNotFoundException.class, () -> {
            pedido.guardarFactura(archivo);
        }, "Debe lanzar FileNotFoundException si no se puede crear el archivo");
    }

    @Test
    public void testGenerarTextoFacturaSinProductos() {
        String factura = pedido.generarTextoFactura();

        assertTrue(factura.contains("Cliente: Juan Pérez"));
        assertTrue(factura.contains("Precio Neto:  0"));
        assertTrue(factura.contains("Precio Total: 0"));
    }
}
