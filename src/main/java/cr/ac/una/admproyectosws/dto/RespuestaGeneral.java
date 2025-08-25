package cr.ac.una.admproyectosws.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RespuestaGeneral<T> {
    private boolean ok;
    private String mensaje;
    private T data;

    public RespuestaGeneral() {}

    public RespuestaGeneral(boolean ok, String mensaje) {
        this.ok = ok;
        this.mensaje = mensaje;
    }

    public RespuestaGeneral(boolean ok, String mensaje, T data) {
        this.ok = ok;
        this.mensaje = mensaje;
        this.data = data;
    }

    // Getters y Setters
    public boolean isOk() { return ok; }
    public void setOk(boolean ok) { this.ok = ok; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}