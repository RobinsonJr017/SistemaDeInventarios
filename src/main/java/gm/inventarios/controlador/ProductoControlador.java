package gm.inventarios.controlador;

import gm.inventarios.excepcion.RecursoNoEncontradoExcepcion;
import gm.inventarios.modelo.Producto;
import gm.inventarios.servicio.ProductoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
}