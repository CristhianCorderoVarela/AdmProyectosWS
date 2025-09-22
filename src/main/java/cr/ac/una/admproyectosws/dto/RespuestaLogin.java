package cr.ac.una.admproyectosws.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RespuestaLogin {
    private boolean ok;
    private String mensaje;
    private AdministradorDto administrador;

    // Esto crea la respuesta vacía
    public RespuestaLogin() {}

      // Esto crea la respuesta con el resultado, el mensaje y el administrador
    public RespuestaLogin(boolean ok, String mensaje, AdministradorDto administrador) {
        this.ok = ok;
        this.mensaje = mensaje;
        this.administrador = administrador;
    }

    // Getters y Setters
    public boolean isOk() { return ok; }
    public void setOk(boolean ok) { this.ok = ok; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public AdministradorDto getAdministrador() { return administrador; }
    public void setAdministrador(AdministradorDto administrador) { this.administrador = administrador; }
}
