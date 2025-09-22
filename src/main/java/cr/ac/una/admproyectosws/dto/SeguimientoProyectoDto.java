package cr.ac.una.admproyectosws.dto;

import cr.ac.una.admproyectosws.model.SeguimientoProyecto;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SeguimientoProyectoDto {
    // Esto guarda los datos simples de un seguimiento para moverlos y mostrarlos
    private Long id;
    private Date fechaSeguimiento;
    private String observaciones;
    private Integer porcentajeAvance;
    private Date fechaCreacion;
    private Long proyectoId;
    private String proyectoNombre;
    private Long creadoPorId;
    private String creadoPorNombre;
    
// Esto crea un DTO vacío 
    public SeguimientoProyectoDto() {}

    // Esto carga el DTO usando una entidad ya existente, listo para enviar o mostrar
    public SeguimientoProyectoDto(SeguimientoProyecto seguimiento) {
        if (seguimiento != null) {
            this.id = seguimiento.getId();
            this.fechaSeguimiento = seguimiento.getFechaSeguimiento();
            this.observaciones = seguimiento.getObservaciones();
            this.porcentajeAvance = seguimiento.getPorcentajeAvance();
            this.fechaCreacion = seguimiento.getFechaCreacion();
            
            if (seguimiento.getProyecto() != null) {
                this.proyectoId = seguimiento.getProyecto().getId();
                this.proyectoNombre = seguimiento.getProyecto().getNombre();
            }
            
            if (seguimiento.getCreadoPor() != null) {
                this.creadoPorId = seguimiento.getCreadoPor().getId();
                this.creadoPorNombre = seguimiento.getCreadoPor().getNombre() + " " + seguimiento.getCreadoPor().getApellidos();
            }
        }
    }

    // Esto convierte el DTO en entidad para poder guardarlo en la base de datos
    public SeguimientoProyecto toEntity() {
        SeguimientoProyecto seguimiento = new SeguimientoProyecto();
        seguimiento.setId(this.id);
        seguimiento.setFechaSeguimiento(this.fechaSeguimiento);
        seguimiento.setObservaciones(this.observaciones);
        seguimiento.setPorcentajeAvance(this.porcentajeAvance);
        seguimiento.setFechaCreacion(this.fechaCreacion);
        return seguimiento;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Date getFechaSeguimiento() { return fechaSeguimiento; }
    public void setFechaSeguimiento(Date fechaSeguimiento) { this.fechaSeguimiento = fechaSeguimiento; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Integer getPorcentajeAvance() { return porcentajeAvance; }
    public void setPorcentajeAvance(Integer porcentajeAvance) { this.porcentajeAvance = porcentajeAvance; }

    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Long getProyectoId() { return proyectoId; }
    public void setProyectoId(Long proyectoId) { this.proyectoId = proyectoId; }

    public String getProyectoNombre() { return proyectoNombre; }
    public void setProyectoNombre(String proyectoNombre) { this.proyectoNombre = proyectoNombre; }

    public Long getCreadoPorId() { return creadoPorId; }
    public void setCreadoPorId(Long creadoPorId) { this.creadoPorId = creadoPorId; }

    public String getCreadoPorNombre() { return creadoPorNombre; }
    public void setCreadoPorNombre(String creadoPorNombre) { this.creadoPorNombre = creadoPorNombre; }
}
