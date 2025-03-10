package gm.inventarios.servicio;

import gm.inventarios.modelo.Producto;
import gm.inventarios.repositorio.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductoServicio implements IProductoServicio{

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Override
    public List<Producto> listarProductos() {
        return this.productoRepositorio.findAll(); //Regresa todos los objetos de tipo producto de la bd
    }

    @Override
    public Optional<Producto> buscarProductoPorId(UUID idProducto) {
        return this.productoRepositorio.findById(idProducto);
    }

    @Override
    public Producto guardarProducto(Producto producto) { //El metodo save funciona si el idProducto = null hace un insert
         return this.productoRepositorio.save(producto);  //si es diferente de null se hace un update
    }

    @Override
    public void eliminarProductoPorId(UUID idProducto) {
        this.productoRepositorio.deleteById(idProducto);
    }
}