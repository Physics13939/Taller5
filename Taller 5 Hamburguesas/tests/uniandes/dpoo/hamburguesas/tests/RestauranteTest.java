package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.*;

import org.junit.jupiter.api.*;

import uniandes.dpoo.hamburguesas.excepciones.*;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class RestauranteTest {

    private Restaurante restaurante;
    private File archivoIngredientes;
    private File archivoMenu;
    private File archivoCombos;

    @BeforeEach
    public void setUp() throws IOException {
        restaurante = new Restaurante();

        // Crear archivos temporales de prueba
        archivoIngredientes = File.createTempFile("ingredientes", ".txt");
        archivoMenu = File.createTempFile("menu", ".txt");
        archivoCombos = File.createTempFile("combos", ".txt");

        // Datos de ejemplo
        try (PrintWriter out = new PrintWriter(archivoIngredientes)) {
            out.println("Queso;500");
            out.println("Tocineta;800");
        }

        try (PrintWriter out = new PrintWriter(archivoMenu)) {
            out.println("Hamburguesa;20000");
            out.println("Papas;8000");
            out.println("Gaseosa;5000");
        }

        try (PrintWriter out = new PrintWriter(archivoCombos)) {
            out.println("Combo Clasico;10%;Hamburguesa;Papas;Gaseosa");
        }
    }

    @AfterEach
    public void tearDown() {
        archivoIngredientes.delete();
        archivoMenu.delete();
        archivoCombos.delete();
    }

    @Test
    public void testCargarInformacionRestauranteExitoso() throws Exception {
        restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);

        assertEquals(2, restaurante.getIngredientes().size(), "Debe cargar 2 ingredientes");
        assertEquals(3, restaurante.getMenuBase().size(), "Debe cargar 3 productos base");
        assertEquals(1, restaurante.getMenuCombos().size(), "Debe cargar 1 combo");
    }

    @Test
    public void testCargarIngredientesRepetidosLanzaExcepcion() throws Exception {
        try (PrintWriter out = new PrintWriter(archivoIngredientes)) {
            out.println("Queso;500");
            out.println("Queso;600");
        }

        assertThrows(IngredienteRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
        });
    }

    @Test
    public void testCargarMenuRepetidoLanzaExcepcion() throws Exception {
        try (PrintWriter out = new PrintWriter(archivoMenu)) {
            out.println("Hamburguesa;20000");
            out.println("Hamburguesa;25000");
        }

        assertThrows(ProductoRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
        });
    }

    @Test
    public void testCargarComboConProductoFaltanteLanzaExcepcion() throws Exception {
        try (PrintWriter out = new PrintWriter(archivoCombos)) {
            out.println("Combo Errado;5%;Inexistente");
        }

        assertThrows(ProductoFaltanteException.class, () -> {
            restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
        });
    }

    @Test
    public void testIniciarPedidoYGetPedidoEnCurso() throws Exception {
        restaurante.iniciarPedido("Ana", "Calle 1");
        Pedido pedido = restaurante.getPedidoEnCurso();

        assertNotNull(pedido, "Debe haber un pedido en curso");
        assertEquals("Ana", pedido.getNombreCliente());
    }

    @Test
    public void testIniciarPedidoYaExistenteLanzaExcepcion() throws Exception {
        restaurante.iniciarPedido("Carlos", "Calle 2");

        assertThrows(YaHayUnPedidoEnCursoException.class, () -> {
            restaurante.iniciarPedido("Laura", "Calle 3");
        });
    }

    @Test
    public void testCerrarYGuardarPedidoSinPedidoEnCursoLanzaExcepcion() {
        assertThrows(NoHayPedidoEnCursoException.class, () -> {
            restaurante.cerrarYGuardarPedido();
        });
    }

    @Test
    public void testCerrarYGuardarPedidoExitosamente() throws Exception {
        restaurante.iniciarPedido("Mario", "Carrera 9");
        Pedido pedido = restaurante.getPedidoEnCurso();

        // Crear carpeta facturas
        new File("./facturas/").mkdirs();

        // Añadir un producto al pedido
        pedido.agregarProducto(new ProductoMenu("Hamburguesa", 20000));

        restaurante.cerrarYGuardarPedido();

        // Validar que ya no haya pedido en curso
        assertNull(restaurante.getPedidoEnCurso(), "El pedido en curso debe quedar en null");
    }

    @Test
    public void testGettersDeListasIniciales() {
        assertNotNull(restaurante.getPedidos());
        assertNotNull(restaurante.getIngredientes());
        assertNotNull(restaurante.getMenuBase());
        assertNotNull(restaurante.getMenuCombos());
    }


    @Test
    public void testCargarInformacionRestauranteConArchivoInvalido() {
        File archivoInexistente = new File("noexiste.txt");
        assertThrows(IOException.class, () -> {
            restaurante.cargarInformacionRestaurante(archivoInexistente, archivoMenu, archivoCombos);
        });
    }
}
