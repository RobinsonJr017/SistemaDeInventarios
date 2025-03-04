package gm.inventarios.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data //relax
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Producto {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    @GeneratedValue
    @UuidGenerator
    private UUID idProducto;
    private String descripcion;
    private Double precio;
    private Integer existencia;

    public UUID getIdProducto() {
        return this.idProducto;
    }

    public void setIdProducto(UUID idProducto) {
        this.idProducto = idProducto;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return this.precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getExistencia() {
        return this.existencia;
    }

    public void setExistencia(Integer existencia) {
        this.existencia = existencia;
    }

}

