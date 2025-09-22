package cr.ac.una.admproyectosws.dto;

import cr.ac.una.admproyectosws.model.Actividad;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class ActividadDto {
    private Long id;
    private String descripcion;
    private String encargadoNombre;
    private String encargadoCorreo;
    private String estado;
    private Date fechaInicioPlanificada;
    private Date fechaFinalPlanificada;
    private Date fechaInicioReal;
    private Date fechaFinalReal;
    private Integer ordenEjecucion;
    private Date fechaCreacion;
    private Date fechaModificacion;
    private Long proyectoId;
    private String proyectoNombre;
    
//Esto crea un DTo vacio
    public ActividadDto() {}

    //Esto arma el DTo a partir de una entidad Actividad ya existente
    public ActividadDto(Actividad actividad) {
        if (actividad != null) {
            this.id = actividad.getId();
            this.descripcion = actividad.getDescripcion();
            this.encargadoNombre = actividad.getEncargadoNombre();
            this.encargadoCorreo = actividad.getEncargadoCorreo();
            this.estado = actividad.getEstado();
            this.fechaInicioPlanificada = actividad.getFechaInicioPlanificada();
            this.fechaFinalPlanificada = actividad.getFechaFinalPlanificada();
            this.fechaInicioReal = actividad.getFechaInicioReal();
            this.fechaFinalReal = actividad.getFechaFinalReal();
            this.ordenEjecucion = actividad.getOrdenEjecucion();
            this.fechaCreacion = actividad.getFechaCreacion();
            this.fechaModificacion = actividad.getFechaModificacion();
            
            if (actividad.getProyecto() != null) {
                this.proyectoId = actividad.getProyecto().getId();
                this.proyectoNombre = actividad.getProyecto().getNombre();
            }
        }
    }

    //Esto convierte el DTO en una entidad lista para guardar o actualizar
    public Actividad toEntity() {
        Actividad actividad = new Actividad();
        actividad.setId(this.id);
        actividad.setDescripcion(this.descripcion);
        actividad.setEncargadoNombre(this.encargadoNombre);
        actividad.setEncargadoCorreo(this.encargadoCorreo);
        actividad.setEstado(this.estado);
        actividad.setFechaInicioPlanificada(this.fechaInicioPlanificada);
        actividad.setFechaFinalPlanificada(this.fechaFinalPlanificada);
        actividad.setFechaInicioReal(this.fechaInicioReal);
        actividad.setFechaFinalReal(this.fechaFinalReal);
        actividad.setOrdenEjecucion(this.ordenEjecucion);
        actividad.setFechaCreacion(this.fechaCreacion);
        actividad.setFechaModificacion(this.fechaModificacion);
        return actividad;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEncargadoNombre() { return encargadoNombre; }
    public void setEncargadoNombre(String encargadoNombre) { this.encargadoNombre = encargadoNombre; }

    public String getEncargadoCorreo() { return encargadoCorreo; }
    public void setEncargadoCorreo(String encargadoCorreo) { this.encargadoCorreo = encargadoCorreo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFechaInicioPlanificada() { return fechaInicioPlanificada; }
    public void setFechaInicioPlanificada(Date fechaInicioPlanificada) { this.fechaInicioPlanificada = fechaInicioPlanificada; }

    public Date getFechaFinalPlanificada() { return fechaFinalPlanificada; }
    public void setFechaFinalPlanificada(Date fechaFinalPlanificada) { this.fechaFinalPlanificada = fechaFinalPlanificada; }

    public Date getFechaInicioReal() { return fechaInicioReal; }
    public void setFechaInicioReal(Date fechaInicioReal) { this.fechaInicioReal = fechaInicioReal; }

    public Date getFechaFinalReal() { return fechaFinalReal; }
    public void setFechaFinalReal(Date fechaFinalReal) { this.fechaFinalReal = fechaFinalReal; }

    public Integer getOrdenEjecucion() { return ordenEjecucion; }
    public void setOrdenEjecucion(Integer ordenEjecucion) { this.ordenEjecucion = ordenEjecucion; }

    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Date getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(Date fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public Long getProyectoId() { return proyectoId; }
    public void setProyectoId(Long proyectoId) { this.proyectoId = proyectoId; }

    public String getProyectoNombre() { return proyectoNombre; }
    public void setProyectoNombre(String proyectoNombre) { this.proyectoNombre = proyectoNombre; }
}