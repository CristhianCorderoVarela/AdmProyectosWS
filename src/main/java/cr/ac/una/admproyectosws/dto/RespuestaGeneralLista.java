package cr.ac.una.admproyectosws.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import java.util.List;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ ProyectoDto.class })
public class RespuestaGeneralLista<T> {
    
// Esto indica si la operacion salió bien o no
    private boolean ok;
    
    private String mensaje;

    
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<T> data;
    
// Esto crea la respuesta vacía
    public RespuestaGeneralLista() {
    }
    
// Esto crea la respuesta completa con estado, mensaje y la lista de datos
    public RespuestaGeneralLista(boolean ok, String mensaje, List<T> data) {
        this.ok = ok;
        this.mensaje = mensaje;
        this.data = data;
    }

    // Getters y Setters
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
