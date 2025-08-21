package cr.ac.una.admproyectosws.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ADMINISTRADORES",
       uniqueConstraints = {
         @UniqueConstraint(columnNames = "USUARIO"),
         @UniqueConstraint(columnNames = "CORREO")
       })
public class Administrador {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 80)
    private String nombre;

    @Column(name = "APELLIDOS", nullable = false, length = 120)
    private String apellidos;

    @Column(name = "CORREO", nullable = false, length = 120)
    private String correo;

    @Column(name = "USUARIO", nullable = false, length = 40)
    private String usuario;

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setPasswordPlain(String passwordPlain) {
        this.passwordPlain = passwordPlain;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPasswordPlain() {
        return passwordPlain;
    }

    public String getEstado() {
        return estado;
    }

    @Column(name = "PASSWORD_PLAIN", nullable = false, length = 255)
    private String passwordPlain;

    @Column(name = "ESTADO", nullable = false, length = 10)
    private String estado;

    // getters/setters...
}
