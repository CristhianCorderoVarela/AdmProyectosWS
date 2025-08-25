package cr.ac.una.admproyectosws.ws;

import cr.ac.una.admproyectosws.dto.ProyectoDto;
import cr.ac.una.admproyectosws.dto.RespuestaGeneral;
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
    public RespuestaGeneral<List<ProyectoDto>> obtenerTodosProyectos() {
        return proyectoService.obtenerTodos();
    }
    
    @WebMethod
    public RespuestaGeneral<List<ProyectoDto>> buscarProyectosActivos() {
        return proyectoService.buscarActivos();
    }
    
    // Implementación con Streams como requiere la tarea
    @WebMethod
    public RespuestaGeneral<List<ProyectoDto>> buscarProyectosConStreams(
            @WebParam(name = "filtro") String filtro) {
        return proyectoService.buscarConStreams(filtro);
    }
}