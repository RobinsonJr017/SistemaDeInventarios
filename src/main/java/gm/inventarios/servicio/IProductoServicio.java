package gm.inventarios.servicio;

import gm.inventarios.modelo.Producto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductoServicio {
    public List<Producto> listarProductos();

    public Optional<Producto> buscarProductoPorId(UUID idProducto);

    public Producto guardarProducto(Producto producto);

    public void eliminarProductoPorId(UUID idProducto);

}
