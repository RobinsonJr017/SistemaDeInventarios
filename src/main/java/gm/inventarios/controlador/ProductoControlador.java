package gm.inventarios.controlador;

import gm.inventarios.modelo.Producto;
import gm.inventarios.servicio.ProductoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/producto")
//@CrossOrigin(value = "http://localhost:4200")
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
    public Producto agregarProducto(@RequestBody Producto producto){
        logger.info("Producto a agregar: " + producto);
        return this.productoServicio.guardarProducto(producto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable UUID id){ //metodo ese id no e usa //ya
        logger.info("Productos obtenidos");
        return this.productoServicio.buscarProductoPorId(id)
                .map(producto -> ResponseEntity.ok().body(producto))
                .orElse(ResponseEntity.notFound().build());
    }
}