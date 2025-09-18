package cr.ac.una.admproyectosws.dto;

import cr.ac.una.admproyectosws.model.Administrador;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AdministradorDto {

    private Long id;
    private String nombre;
    private String apellidos;
    private String cedula;
    private String correo;
    private String usuario;
    private String estado;
    private String passwordPlain;          // puede ir null al enviar al cliente
    private Date fechaCreacion;
    private Date fechaModificacion;

    public AdministradorDto() {}

    public AdministradorDto(Administrador administrador) {
        if (administrador != null) {
            this.id = administrador.getId();
            this.nombre = administrador.getNombre();
            this.apellidos = administrador.getApellidos();
            this.cedula = administrador.getCedula();
            this.correo = administrador.getCorreo();
            this.usuario = administrador.getUsuario();
            this.estado = administrador.getEstado();
            // Si no quieres exponer, no asignes passwordPlain aquí
            // this.passwordPlain = administrador.getPasswordPlain();
            this.fechaCreacion = administrador.getFechaCreacion();
            this.fechaModificacion = administrador.getFechaModificacion();
        }
    }

    // >>> ARREGLO: implementar correctamente este constructor <<<
    public AdministradorDto(Long id, String nombre, String apellidos, String cedula,
                            String correo, String usuario, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.correo = correo;
        this.usuario = usuario;
        this.estado = estado;
        this.passwordPlain = null; // nunca exponer al cliente
        // fechaCreacion / fechaModificacion pueden quedar null aquí
    }

    /** Convierte el DTO en entidad (crear/actualizar). */
   public Administrador toEntity() {
    Administrador admin = new Administrador();
    admin.setId(this.id);
    admin.setNombre(this.nombre);
    admin.setApellidos(this.apellidos);
    admin.setCedula(this.cedula);
    admin.setCorreo(this.correo);
    admin.setUsuario(this.usuario);
    admin.setEstado(this.estado);
    admin.setPasswordPlain(this.passwordPlain);
        return admin;
    }

    // Getters / Setters
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

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getPasswordPlain() { return passwordPlain; }
    public void setPasswordPlain(String passwordPlain) { this.passwordPlain = passwordPlain; }

    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Date getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(Date fechaModificacion) { this.fechaModificacion = fechaModificacion; }
}