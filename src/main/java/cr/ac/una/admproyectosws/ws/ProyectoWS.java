package cr.ac.una.admproyectosws.ws;

import cr.ac.una.admproyectosws.dto.ProyectoDto;
import cr.ac.una.admproyectosws.dto.RespuestaGeneral;
import cr.ac.una.admproyectosws.dto.RespuestaGeneralLista;
import cr.ac.una.admproyectosws.service.ProyectoService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;

@Stateless
@WebService(serviceName = "ProyectoService")
public class ProyectoWS {
    
    @EJB
    private ProyectoService proyectoService;
    
    @WebMethod
    public String ping() { 
        return "ProyectoService OK"; 
    }
    
    @WebMethod
    public RespuestaGeneral<ProyectoDto> crearProyecto(
            @WebParam(name = "proyecto") ProyectoDto proyecto,
            @WebParam(name = "creadoPorId") Long creadoPorId) {
        return proyectoService.crear(proyecto, creadoPorId);
    }
    
    @WebMethod
    public RespuestaGeneral<ProyectoDto> actualizarProyecto(
            @WebParam(name = "proyecto") ProyectoDto proyecto) {
        return proyectoService.actualizar(proyecto);
    }
    
    @WebMethod
    public RespuestaGeneral<Void> eliminarProyecto(
            @WebParam(name = "id") Long id) {
        return proyectoService.eliminar(id);
    }
    
    @WebMethod
    public RespuestaGeneral<ProyectoDto> buscarProyectoPorId(
            @WebParam(name = "id") Long id) {
        return proyectoService.buscarPorId(id);
    }
    @WebMethod
    // Antes: public RespuestaGeneral<List<ProyectoDto>> obtenerTodosProyectos()
public RespuestaGeneralLista<ProyectoDto> obtenerTodosProyectos() {
    var r = proyectoService.obtenerTodos(); // ya devuelve RespuestaGeneral<List<ProyectoDto>>
    var out = new RespuestaGeneralLista<ProyectoDto>();
    out.setOk(r.isOk());
    out.setMensaje(r.getMensaje());
    out.setData(r.getData());
    return out;
}
@WebMethod
// Antes: RespuestaGeneral<List<ProyectoDto>> buscarProyectosActivos()
public RespuestaGeneralLista<ProyectoDto> buscarProyectosActivos() {
    var r = proyectoService.buscarActivos();
    var out = new RespuestaGeneralLista<ProyectoDto>();
    out.setOk(r.isOk());
    out.setMensaje(r.getMensaje());
    out.setData(r.getData());
    return out;
}
@WebMethod
// Antes: RespuestaGeneral<List<ProyectoDto>> buscarProyectosConStreams(String filtro)
public RespuestaGeneralLista<ProyectoDto> buscarProyectosConStreams(String filtro) {
    var r = proyectoService.buscarConStreams(filtro);
    var out = new RespuestaGeneralLista<ProyectoDto>();
    out.setOk(r.isOk());
    out.setMensaje(r.getMensaje());
    out.setData(r.getData());
    return out;
}

}