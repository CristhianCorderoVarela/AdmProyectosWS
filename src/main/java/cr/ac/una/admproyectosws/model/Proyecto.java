package cr.ac.una.admproyectosws.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PROYECTOS")
@NamedQueries({
    @NamedQuery(name = "Proyecto.findAll", 
                query = "SELECT p FROM Proyecto p"),
    @NamedQuery(name = "Proyecto.findByEstado", 
                query = "SELECT p FROM Proyecto p WHERE p.estado = :estado"),
    @NamedQuery(name = "Proyecto.findActivos", 
                query = "SELECT p FROM Proyecto p WHERE p.estado IN ('PLANIFICADO', 'EN_CURSO')"),
    @NamedQuery(name = "Proyecto.buscar", 
                query = "SELECT p FROM Proyecto p WHERE LOWER(p.nombre) LIKE LOWER(:filtro) OR LOWER(p.patrocinadorNombre) LIKE LOWER(:filtro)")
})
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "NOMBRE", nullable = false, length = 200)
    private String nombre;
    
    @Column(name = "PATROCINADOR_NOMBRE", nullable = false, length = 120)
    private String patrocinadorNombre;
    
    @Column(name = "PATROCINADOR_CORREO", nullable = false, length = 120)
    private String patrocinadorCorreo;
    
    @Column(name = "LIDER_USUARIO_NOMBRE", nullable = false, length = 120)
    private String liderUsuarioNombre;
    
    @Column(name = "LIDER_USUARIO_CORREO", nullable = false, length = 120)
    private String liderUsuarioCorreo;
    
    @Column(name = "LIDER_TECNICO_NOMBRE", nullable = false, length = 120)
    private String liderTecnicoNombre;
    
    @Column(name = "LIDER_TECNICO_CORREO", nullable = false, length = 120)
    private String liderTecnicoCorreo;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_INICIO_PLANIFICADA", nullable = false)
    private Date fechaInicioPlanificada;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_FINAL_PLANIFICADA", nullable = false)
    private Date fechaFinalPlanificada;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_INICIO_REAL")
    private Date fechaInicioReal;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_FINAL_REAL")
    private Date fechaFinalReal;
    
    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado;
    
    @Column(name = "PORCENTAJE_AVANCE")
    private Integer porcentajeAvance = 0;
    
    @Lob
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_CREACION")
    private Date fechaCreacion;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_MODIFICACION")
    private Date fechaModificacion;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREADO_POR")
    private Administrador creadoPor;
    
    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("ordenEjecucion ASC")
    private List<Actividad> actividades;
    
    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("fechaSeguimiento DESC")
    private List<SeguimientoProyecto> seguimientos;

    // Constructores
    public Proyecto() {
        this.fechaCreacion = new Date();
        this.fechaModificacion = new Date();
        this.porcentajeAvance = 0;
    }

    // Métodos de ciclo de vida
    @PreUpdate
    public void preUpdate() {
        this.fechaModificacion = new Date();
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

    public Administrador getCreadoPor() { return creadoPor; }
    public void setCreadoPor(Administrador creadoPor) { this.creadoPor = creadoPor; }

    public List<Actividad> getActividades() { return actividades; }
    public void setActividades(List<Actividad> actividades) { this.actividades = actividades; }

    public List<SeguimientoProyecto> getSeguimientos() { return seguimientos; }
    public void setSeguimientos(List<SeguimientoProyecto> seguimientos) { this.seguimientos = seguimientos; }
}