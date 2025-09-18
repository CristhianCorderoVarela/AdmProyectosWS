package cr.ac.una.admproyectosws.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ADMINISTRADORES",
       uniqueConstraints = {
         @UniqueConstraint(columnNames = "USUARIO"),
         @UniqueConstraint(columnNames = "CORREO"),
         @UniqueConstraint(columnNames = "CEDULA")
       })
@NamedQueries({
    @NamedQuery(name = "Administrador.findAll", 
                query = "SELECT a FROM Administrador a"),
    @NamedQuery(name = "Administrador.findByUsuario", 
                query = "SELECT a FROM Administrador a WHERE a.usuario = :usuario"),
    @NamedQuery(name = "Administrador.findByEstado", 
                query = "SELECT a FROM Administrador a WHERE a.estado = :estado"),
    @NamedQuery(name = "Administrador.login", 
                query = "SELECT a FROM Administrador a WHERE a.usuario = :usuario AND a.passwordPlain = :password AND a.estado = 'ACTIVO'")
})
public class Administrador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "NOMBRE", nullable = false, length = 80)
    private String nombre;
    
    @Column(name = "APELLIDOS", nullable = false, length = 120)
    private String apellidos;
    
    @Column(name = "CEDULA", nullable = false, length = 20, unique = true)
    private String cedula;
    
    @Column(name = "CORREO", nullable = false, length = 120, unique = true)
    private String correo;
    
    @Column(name = "USUARIO", nullable = false, length = 40, unique = true)
    private String usuario;
    
    @Column(name = "PASSWORD_PLAIN", nullable = false, length = 255)
    private String passwordPlain;
    
    @Column(name = "ESTADO", nullable = false, length = 10)
    private String estado;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_CREACION", updatable = false)
    private Date fechaCreacion;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_MODIFICACION")
    private Date fechaModificacion;

    // === Relaciones ===
    @OneToMany(mappedBy = "creadoPor", fetch = FetchType.LAZY)
    private List<Proyecto> proyectosCreados;
    
    @OneToMany(mappedBy = "creadoPor", fetch = FetchType.LAZY)
    private List<SeguimientoProyecto> seguimientosCreados;

    // === Constructores ===
    public Administrador() {}

    public Administrador(String nombre, String apellidos, String cedula, String correo, 
                         String usuario, String passwordPlain, String estado) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.correo = correo;
        this.usuario = usuario;
        this.passwordPlain = passwordPlain;
        this.estado = estado;
    }

    // === Ciclo de vida ===
    @PrePersist
    protected void prePersist() {
        Date now = new Date();
        if (this.fechaCreacion == null) this.fechaCreacion = now;
        if (this.fechaModificacion == null) this.fechaModificacion = now;
    }

    @PreUpdate
    protected void preUpdate() {
        this.fechaModificacion = new Date();
    }

    // === Getters y Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getPasswordPlain() { return passwordPlain; }
    public void setPasswordPlain(String passwordPlain) { this.passwordPlain = passwordPlain; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Date getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(Date fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public List<Proyecto> getProyectosCreados() { return proyectosCreados; }
    public void setProyectosCreados(List<Proyecto> proyectosCreados) { this.proyectosCreados = proyectosCreados; }

    public List<SeguimientoProyecto> getSeguimientosCreados() { return seguimientosCreados; }
    public void setSeguimientosCreados(List<SeguimientoProyecto> seguimientosCreados) { this.seguimientosCreados = seguimientosCreados; }
}