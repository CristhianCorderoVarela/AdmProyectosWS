package cr.ac.una.admproyectosws.dto;

import cr.ac.una.admproyectosws.model.Proyecto;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProyectoDto {
    private Long id;
    private String nombre;
    private String patrocinadorNombre;
    private String patrocinadorCorreo;
    private String liderUsuarioNombre;
    private String liderUsuarioCorreo;
    private String liderTecnicoNombre;
    private String liderTecnicoCorreo;
    private Date fechaInicioPlanificada;
    private Date fechaFinalPlanificada;
    private Date fechaInicioReal;
    private Date fechaFinalReal;
    private String estado;
    private Integer porcentajeAvance;
    private String descripcion;
    private Date fechaCreacion;
    private Date fechaModificacion;
    private Long creadoPorId;
    private String creadoPorNombre;
    private List<ActividadDto> actividades;
    private SeguimientoProyectoDto ultimoSeguimiento;

    public ProyectoDto() {}

    public ProyectoDto(Proyecto proyecto) {
        if (proyecto != null) {
            this.id = proyecto.getId();
            this.nombre = proyecto.getNombre();
            this.patrocinadorNombre = proyecto.getPatrocinadorNombre();
            this.patrocinadorCorreo = proyecto.getPatrocinadorCorreo();
            this.liderUsuarioNombre = proyecto.getLiderUsuarioNombre();
            this.liderUsuarioCorreo = proyecto.getLiderUsuarioCorreo();
            this.liderTecnicoNombre = proyecto.getLiderTecnicoNombre();
            this.liderTecnicoCorreo = proyecto.getLiderTecnicoCorreo();
            this.fechaInicioPlanificada = proyecto.getFechaInicioPlanificada();
            this.fechaFinalPlanificada = proyecto.getFechaFinalPlanificada();
            this.fechaInicioReal = proyecto.getFechaInicioReal();
            this.fechaFinalReal = proyecto.getFechaFinalReal();
            this.estado = proyecto.getEstado();
            this.porcentajeAvance = proyecto.getPorcentajeAvance();
            this.descripcion = proyecto.getDescripcion();
            this.fechaCreacion = proyecto.getFechaCreacion();
            this.fechaModificacion = proyecto.getFechaModificacion();
            
            if (proyecto.getCreadoPor() != null) {
                this.creadoPorId = proyecto.getCreadoPor().getId();
                this.creadoPorNombre = proyecto.getCreadoPor().getNombre() + " " + proyecto.getCreadoPor().getApellidos();
            }
            
            if (proyecto.getActividades() != null) {
                this.actividades = proyecto.getActividades().stream()
                        .map(ActividadDto::new)
                        .collect(Collectors.toList());
            }
            
            if (proyecto.getSeguimientos() != null && !proyecto.getSeguimientos().isEmpty()) {
                this.ultimoSeguimiento = new SeguimientoProyectoDto(proyecto.getSeguimientos().get(0));
            }
        }
    }

    public Proyecto toEntity() {
        Proyecto proyecto = new Proyecto();
        proyecto.setId(this.id);
        proyecto.setNombre(this.nombre);
        proyecto.setPatrocinadorNombre(this.patrocinadorNombre);
        proyecto.setPatrocinadorCorreo(this.patrocinadorCorreo);
        proyecto.setLiderUsuarioNombre(this.liderUsuarioNombre);
        proyecto.setLiderUsuarioCorreo(this.liderUsuarioCorreo);
        proyecto.setLiderTecnicoNombre(this.liderTecnicoNombre);
        proyecto.setLiderTecnicoCorreo(this.liderTecnicoCorreo);
        proyecto.setFechaInicioPlanificada(this.fechaInicioPlanificada);
        proyecto.setFechaFinalPlanificada(this.fechaFinalPlanificada);
        proyecto.setFechaInicioReal(this.fechaInicioReal);
        proyecto.setFechaFinalReal(this.fechaFinalReal);
        proyecto.setEstado(this.estado);
        proyecto.setPorcentajeAvance(this.porcentajeAvance);
        proyecto.setDescripcion(this.descripcion);
        proyecto.setFechaCreacion(this.fechaCreacion);
        proyecto.setFechaModificacion(this.fechaModificacion);
        return proyecto;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPatrocinadorNombre() { return patrocinadorNombre; }
    public void setPatrocinadorNombre(String patrocinadorNombre) { this.patrocinadorNombre = patrocinadorNombre; }

    public String getPatrocinadorCorreo() { return patrocinadorCorreo; }
    public void setPatrocinadorCorreo(String patrocinadorCorreo) { this.patrocinadorCorreo = patrocinadorCorreo; }

    public String getLiderUsuarioNombre() { return liderUsuarioNombre; }
    public void setLiderUsuarioNombre(String liderUsuarioNombre) { this.liderUsuarioNombre = liderUsuarioNombre; }

    public String getLiderUsuarioCorreo() { return liderUsuarioCorreo; }
    public void setLiderUsuarioCorreo(String liderUsuarioCorreo) { this.liderUsuarioCorreo = liderUsuarioCorreo; }

    public String getLiderTecnicoNombre() { return liderTecnicoNombre; }
    public void setLiderTecnicoNombre(String liderTecnicoNombre) { this.liderTecnicoNombre = liderTecnicoNombre; }

    public String getLiderTecnicoCorreo() { return liderTecnicoCorreo; }
    public void setLiderTecnicoCorreo(String liderTecnicoCorreo) { this.liderTecnicoCorreo = liderTecnicoCorreo; }

    public Date getFechaInicioPlanificada() { return fechaInicioPlanificada; }
    public void setFechaInicioPlanificada(Date fechaInicioPlanificada) { this.fechaInicioPlanificada = fechaInicioPlanificada; }

    public Date getFechaFinalPlanificada() { return fechaFinalPlanificada; }
    public void setFechaFinalPlanificada(Date fechaFinalPlanificada) { this.fechaFinalPlanificada = fechaFinalPlanificada; }

    public Date getFechaInicioReal() { return fechaInicioReal; }
    public void setFechaInicioReal(Date fechaInicioReal) { this.fechaInicioReal = fechaInicioReal; }

    public Date getFechaFinalReal() { return fechaFinalReal; }
    public void setFechaFinalReal(Date fechaFinalReal) { this.fechaFinalReal = fechaFinalReal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getPorcentajeAvance() { return porcentajeAvance; }
    public void setPorcentajeAvance(Integer porcentajeAvance) { this.porcentajeAvance = porcentajeAvance; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Date getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(Date fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public Long getCreadoPorId() { return creadoPorId; }
    public void setCreadoPorId(Long creadoPorId) { this.creadoPorId = creadoPorId; }

    public String getCreadoPorNombre() { return creadoPorNombre; }
    public void setCreadoPorNombre(String creadoPorNombre) { this.creadoPorNombre = creadoPorNombre; }

    public List<ActividadDto> getActividades() { return actividades; }
    public void setActividades(List<ActividadDto> actividades) { this.actividades = actividades; }

    public SeguimientoProyectoDto getUltimoSeguimiento() { return ultimoSeguimiento; }
    public void setUltimoSeguimiento(SeguimientoProyectoDto ultimoSeguimiento) { this.ultimoSeguimiento = ultimoSeguimiento; }
}
