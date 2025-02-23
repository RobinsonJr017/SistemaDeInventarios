package gm.inventarios.controlador;

import gm.inventarios.modelo.Producto;
import gm.inventarios.servicio.ProductoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//http://localhost:8080/inventario-app
@RequestMapping("inventario-app")
@CrossOrigin(value = "http://localhost:4200") //
public class ProductoControlador {
    //Atributo //Clase Logger Factory //metodo get logger
    private static final Logger logger = LoggerFactory.getLogger(ProductoControlador.class); //Cuando enviemos info a la consola se
    //especiofica que es apartir de esta clase y utilizamos la variable logger para poder enviar info a la consola

    @Autowired
    private ProductoServicio productoServicio; //con esto obtenemos la info de la bd

    //http://localhost:8080/inventario-app/productos
    @GetMapping("/productos")
    public List<Producto> obtenerProductos(){ //metodo
        List<Producto> productos = this.productoServicio.listarProductos(); //Obtenemos todos los productos de la bd
        logger.info("Productos obtenidos");
        productos.forEach((producto -> logger.info(producto.toString())));
        return productos;
    }

}


