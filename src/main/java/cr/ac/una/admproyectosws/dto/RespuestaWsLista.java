package cr.ac.una.admproyectosws.dto;

import cr.ac.una.admproyectosws.dto.ProyectoDto;
import cr.ac.una.admproyectosws.dto.SeguimientoProyectoDto;
import cr.ac.una.admproyectosws.dto.ActividadDto;
import cr.ac.una.admproyectosws.dto.AdministradorDto;

import jakarta.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Respuesta única del WS basada en LISTA.
 * - ok / mensaje
 * - data: lista de objetos (proyectos, seguimientos, etc.)
 *
 * SIEMPRE usa getData() para el consumidor.
 * Para crear/editar/eliminar, devolver data con 1 elemento (el afectado) o vacía.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RespuestaWsLista")
@XmlSeeAlso({ ProyectoDto.class, SeguimientoProyectoDto.class, ActividadDto.class, AdministradorDto.class })
public class RespuestaWsLista implements Serializable {

    @XmlElement(name = "ok")
    private Boolean ok = Boolean.FALSE;

    @XmlElement(name = "mensaje")
    private String mensaje;

    // Permitimos varios tipos en la lista
    @XmlElements({
            @XmlElement(name = "proyecto",    type = ProyectoDto.class),
            @XmlElement(name = "seguimiento", type = SeguimientoProyectoDto.class),
            @XmlElement(name = "actividad",   type = ActividadDto.class),
            @XmlElement(name = "administrador", type = AdministradorDto.class)
    })
    private List<Object> data = new ArrayList<>();

    public RespuestaWsLista() {}

    // -------- Getters estándar (lo que usará el cliente) --------
    public Boolean getOk() { return ok; }
    public Boolean isOk()  { return ok; } // alias práctico
    public void setOk(Boolean ok) { this.ok = ok; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    /** SIEMPRE usa getData() en el cliente */
    public List<Object> getData() { return data == null ? Collections.emptyList() : data; }
    public void setData(List<?> data) {
        this.data = (data == null) ? new ArrayList<>() : new ArrayList<>(data);
    }

    // --------- Fábricas cómodas ---------
    public static RespuestaWsLista okLista(List<?> lista, String msg) {
        RespuestaWsLista r = new RespuestaWsLista();
        r.setOk(Boolean.TRUE);
        r.setMensaje(msg);
        r.setData(lista);
        return r;
    }

    public static RespuestaWsLista okUno(Object obj, String msg) {
        RespuestaWsLista r = new RespuestaWsLista();
        r.setOk(Boolean.TRUE);
        r.setMensaje(msg);
        if (obj != null) r.setData(List.of(obj));
        return r;
    }

    public static RespuestaWsLista okVacio(String msg) {
        RespuestaWsLista r = new RespuestaWsLista();
        r.setOk(Boolean.TRUE);
        r.setMensaje(msg);
        r.setData(List.of());
        return r;
    }

    public static RespuestaWsLista error(String msg) {
        RespuestaWsLista r = new RespuestaWsLista();
        r.setOk(Boolean.FALSE);
        r.setMensaje(msg);
        r.setData(List.of());
        return r;
    }
}
