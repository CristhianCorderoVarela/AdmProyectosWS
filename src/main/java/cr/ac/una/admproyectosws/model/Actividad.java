package cr.ac.una.admproyectosws.model;

import jakarta.persistence.*;
import java.util.Date;

// Esto representa una actividad dentro de un proyecto
@Entity
@Table(name = "ACTIVIDADES")
@NamedQueries({
    @NamedQuery(name = "Actividad.findByProyecto", 
                query = "SELECT a FROM Actividad a WHERE a.proyecto.id = :proyectoId ORDER BY a.ordenEjecucion"),
    @NamedQuery(name = "Actividad.findByEstado", 
                query = "SELECT a FROM Actividad a WHERE a.estado = :estado"),
    @NamedQuery(name = "Actividad.findByProyectoAndEstado", 
                query = "SELECT a FROM Actividad a WHERE a.proyecto.id = :proyectoId AND a.estado = :estado ORDER BY a.ordenEjecucion")
})
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTIVIDAD_SEQ")
    @SequenceGenerator(
        name = "ACTIVIDAD_SEQ",
        sequenceName = "SEQ_ACTIVIDAD_ID", 
        allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;
    
    // Esto describe que se hará
    @Column(name = "DESCRIPCION", nullable = false, length = 500)
    private String descripcion;
    
    // Esto guarda quien se encarga
    @Column(name = "ENCARGADO_NOMBRE", nullable = false, length = 120)
    private String encargadoNombre;
    
    // Esto guarda el correo del encargado
    @Column(name = "ENCARGADO_CORREO", nullable = false, length = 120)
    private String encargadoCorreo;
    
    // Esto indica en que estado va
    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado;
    
    // Esto marca fechas previstas y reales
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
    
    // Esto define el orden en que se ejecuta
    @Column(name = "ORDEN_EJECUCION", nullable = false)
    private Integer ordenEjecucion;
    
    // Esto registra cuando se creó y se modificó
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_CREACION")
    private Date fechaCreacion;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_MODIFICACION")
    private Date fechaModificacion;

     // Esto conecta la actividad con su proyecto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROYECTO_ID", nullable = false)
    private Proyecto proyecto;

   // Esto inicia las fechas al crear la actividad
    public Actividad() {
        this.fechaCreacion = new Date();
        this.fechaModificacion = new Date();
    }

    // Esto actualiza la fecha al modificar la actividad
    @PreUpdate
    public void preUpdate() {
        this.fechaModificacion = new Date();
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
    
    public Proyecto getProyecto() { return proyecto; }
    public void setProyecto(Proyecto proyecto) { this.proyecto = proyecto; }
}
