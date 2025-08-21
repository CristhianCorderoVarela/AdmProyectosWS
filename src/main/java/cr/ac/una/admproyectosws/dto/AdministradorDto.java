package cr.ac.una.admproyectosws.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AdministradorDto {
    private Long id;
    private String nombre;
    private String apellidos;
    private String usuario;
    private String correo;
    private String estado;

    public AdministradorDto() {}

    
    public AdministradorDto(Long id, String nombre, String apellidos,
                            String usuario, String correo, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.usuario = usuario;
        this.correo = correo;
        this.estado = estado;
    }

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; } public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; } public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getUsuario() { return usuario; } public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getCorreo() { return correo; } public void setCorreo(String correo) { this.correo = correo; }
    public String getEstado() { return estado; } public void setEstado(String estado) { this.estado = estado; }
}
