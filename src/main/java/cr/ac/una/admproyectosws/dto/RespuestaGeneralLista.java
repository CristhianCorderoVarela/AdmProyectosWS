package cr.ac.una.admproyectosws.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import java.util.List;

/**
 * Respuesta para operaciones que retornan listas.
 * Se define como clase independiente (no hereda del genérico) para evitar
 * problemas de JAXB con campos genéricos y acceso por FIELD.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ ProyectoDto.class })
public class RespuestaGeneralLista<T> {

    private boolean ok;
    private String mensaje;

    // <items><item>...</item></items>
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<T> data;

    public RespuestaGeneralLista() {
    }

    public RespuestaGeneralLista(boolean ok, String mensaje, List<T> data) {
        this.ok = ok;
        this.mensaje = mensaje;
        this.data = data;
    }

    // Getters / Setters
    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
