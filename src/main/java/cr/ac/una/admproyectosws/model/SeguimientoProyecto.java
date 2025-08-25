package cr.ac.una.admproyectosws.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SEGUIMIENTO_PROYECTO")
@NamedQueries({
    @NamedQuery(name = "SeguimientoProyecto.findByProyecto", 
                query = "SELECT s FROM SeguimientoProyecto s WHERE s.proyecto.id = :proyectoId ORDER BY s.fechaSeguimiento DESC"),
    @NamedQuery(name = "SeguimientoProyecto.findUltimoByProyecto", 
                query = "SELECT s FROM SeguimientoProyecto s WHERE s.proyecto.id = :proyectoId ORDER BY s.fechaSeguimiento DESC"),
    @NamedQuery(name = "SeguimientoProyecto.findByFecha", 
                query = "SELECT s FROM SeguimientoProyecto s WHERE s.fechaSeguimiento BETWEEN :fechaInicio AND :fechaFin")
})
public class SeguimientoProyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_SEGUIMIENTO", nullable = false)
    private Date fechaSeguimiento;
    
    @Lob
    @Column(name = "OBSERVACIONES", nullable = false)
    private String observaciones;
    
    @Column(name = "PORCENTAJE_AVANCE", nullable = false)
    private Integer porcentajeAvance;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_CREACION")
    private Date fechaCreacion;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROYECTO_ID", nullable = false)
    private Proyecto proyecto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREADO_POR", nullable = false)
    private Administrador creadoPor;

    // Constructores
    public SeguimientoProyecto() {
        this.fechaCreacion = new Date();
    }

    public SeguimientoProyecto(Proyecto proyecto, Date fechaSeguimiento, String observaciones, 
                              Integer porcentajeAvance, Administrador creadoPor) {
        this();
        this.proyecto = proyecto;
        this.fechaSeguimiento = fechaSeguimiento;
        this.observaciones = observaciones;
        this.porcentajeAvance = porcentajeAvance;
        this.creadoPor = creadoPor;
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

    public Proyecto getProyecto() { return proyecto; }
    public void setProyecto(Proyecto proyecto) { this.proyecto = proyecto; }

    public Administrador getCreadoPor() { return creadoPor; }
    public void setCreadoPor(Administrador creadoPor) { this.creadoPor = creadoPor; }

}