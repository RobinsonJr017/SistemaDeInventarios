package gm.inventarios.controlador;

import gm.inventarios.excepcion.RecursoNoEncontradoExcepcion;
import gm.inventarios.modelo.Producto;
import gm.inventarios.servicio.ProductoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/producto")
@CrossOrigin(value = "http://localhost:4200")
public class ProductoControlador {
    //Atributo //Clase Logger Factory //metodo get logger
    private static final Logger logger = LoggerFactory.getLogger(ProductoControlador.class); //Cuando enviemos info a la consola se
    //especiofica que es apartir de esta clase y utilizamos la variable logger para poder enviar info a la consola

    @Autowired
    private final ProductoServicio productoServicio; //con esto obtenemos la info de la bd

    public ProductoControlador (ProductoServicio productoServicio){
        this.productoServicio = productoServicio;
    }
    //http://localhost:8080/inventario-app/productos
    @GetMapping("/getall")
    public List<Producto> obtenerProductos(){
        List<Producto> productos = this.productoServicio.listarProductos(); //Obtenemos todos los productos de la bd
        logger.info("Productos obtenidos");
        productos.forEach((producto -> logger.info(producto.toString())));
        return productos;
    }

    @PostMapping("/insert")
    public ResponseEntity<Producto> agregarProducto(@RequestBody Producto producto) {
        logger.info("Producto a agregar: " + producto);

        try {
            // Validar que el producto no sea nulo
            if (producto == null) {
                logger.error("El producto no puede ser nulo");
                return ResponseEntity.badRequest().build();
            }

            // Validar campos obligatorios
            if (producto.getDescripcion() == null || producto.getDescripcion().isEmpty()) {
                logger.error("La descripción del producto no puede estar vacía");
                return ResponseEntity.badRequest().build();
            }
            if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
                logger.error("El precio del producto debe ser mayor que 0");
                return ResponseEntity.badRequest().build();
            }
            if (producto.getExistencia() == null || producto.getExistencia() < 0) {
                logger.error("La existencia del producto no puede ser negativa");
                return ResponseEntity.badRequest().build();
            }

            // Guardar el producto en la base de datos
            Producto nuevoProducto = this.productoServicio.guardarProducto(producto);

            // Devolver el producto creado con el código de estado 201 (CREATED)
            return ResponseEntity.status(201).body(nuevoProducto);
        } catch (Exception e) {
            logger.error("Error al guardar el producto: " + e.getMessage());
            return ResponseEntity.status(500).build();  // Error interno del servidor
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable UUID id) {
        // Buscar el producto por ID
        Optional<Producto> productoOptional = this.productoServicio.buscarProductoPorId(id);

        // Verificar si el producto existe
        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get(); // Obtener el producto del Optional
            return ResponseEntity.ok(producto); // Devuelve 200 OK con el producto
        } else {
            // Lanza una excepción personalizada si el producto no se encuentra
            throw new RecursoNoEncontradoExcepcion("No se encontró el id: " + id);
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Producto> editarProducto(@PathVariable UUID id, @RequestBody Producto producto) {
        try {
            // Validar que el producto no sea nulo
            if (producto == null) {
                logger.error("El producto no puede ser nulo");
                return ResponseEntity.badRequest().build();
            }

            // Validar campos obligatorios
            if (producto.getDescripcion() == null || producto.getDescripcion().isEmpty()) {
                logger.error("La descripción del producto no puede estar vacía");
                return ResponseEntity.badRequest().build();
            }
            if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
                logger.error("El precio del producto debe ser mayor que 0");
                return ResponseEntity.badRequest().build();
            }
            if (producto.getExistencia() == null || producto.getExistencia() < 0) {
                logger.error("La existencia del producto no puede ser negativa");
                return ResponseEntity.badRequest().build();
            }

            // Buscar el producto por ID
            Optional<Producto> productoOptional = this.productoServicio.buscarProductoPorId(id);

            if (productoOptional.isPresent()) {
                Producto productoExistente = productoOptional.get();
                productoExistente.setDescripcion(producto.getDescripcion());
                productoExistente.setPrecio(producto.getPrecio());
                productoExistente.setExistencia(producto.getExistencia());

                // Guardar el producto actualizado
                Producto productoActualizado = this.productoServicio.guardarProducto(productoExistente);
                return ResponseEntity.ok(productoActualizado); // Devuelve 200 OK con el producto actualizado
            } else {
                throw new RecursoNoEncontradoExcepcion("No se encontró el id: " + id);
            }
        } catch (Exception e) {
            logger.error("Error al actualizar el producto: " + e.getMessage());
            return ResponseEntity.status(500).build();  // Error interno del servidor
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarProducto(@PathVariable UUID id) {
        try {
            logger.info("Intentando eliminar producto con ID: " + id);

            // Verificar si el producto existe
            if (!productoServicio.buscarProductoPorId(id).isPresent()) {
                logger.error("No se encontró el producto con ID: " + id);
                throw new RecursoNoEncontradoExcepcion("No se encontró el producto con ID: " + id);
            }

            // Eliminar el producto
            productoServicio.eliminarProductoPorId(id);
            logger.info("Producto eliminado correctamente");

            // Preparar respuesta
            Map<String, Boolean> respuesta = new HashMap<>();
            respuesta.put("eliminado", Boolean.TRUE);
            return ResponseEntity.ok(respuesta);

        } catch (RecursoNoEncontradoExcepcion ex) {
            logger.error("Error al eliminar producto: " + ex.getMessage());
            throw ex; // Re-lanzamos para que se maneje por el ExceptionHandler
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar producto: " + e.getMessage());
            Map<String, Boolean> respuesta = new HashMap<>();
            respuesta.put("eliminado", Boolean.FALSE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }
}