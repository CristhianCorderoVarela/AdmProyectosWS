package cr.ac.una.admproyectosws.ws;

import cr.ac.una.admproyectosws.dto.ProyectoDto;
import cr.ac.una.admproyectosws.dto.ActividadDto;
import cr.ac.una.admproyectosws.dto.RespuestaGeneral;
import cr.ac.una.admproyectosws.dto.RespuestaGeneralLista;
import cr.ac.una.admproyectosws.service.ProyectoService;
import cr.ac.una.admproyectosws.service.ActividadService;
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
    
    @EJB
    private ActividadService actividadService;
    
    @WebMethod
    public String ping() {
        return "ProyectoService OK";
    }
    
    // ==================== MÉTODOS DE PROYECTOS ====================
    
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
    
    // ---------- Listados (retornan RespuestaGeneralLista<ProyectoDto>) ----------
    @WebMethod
    public RespuestaGeneralLista<ProyectoDto> obtenerTodosProyectos() {
        var r = proyectoService.obtenerTodos(); // RespuestaGeneral<List<ProyectoDto>>
        return new RespuestaGeneralLista<>(r.isOk(), r.getMensaje(), r.getData());
    }
    
    @WebMethod
    public RespuestaGeneralLista<ProyectoDto> buscarProyectosActivos() {
        var r = proyectoService.buscarActivos();
        return new RespuestaGeneralLista<>(r.isOk(), r.getMensaje(), r.getData());
    }
    
    @WebMethod
    public RespuestaGeneralLista<ProyectoDto> buscarProyectosConStreams(
            @WebParam(name = "filtro") String filtro) {
        var r = proyectoService.buscarConStreams(filtro);
        return new RespuestaGeneralLista<>(r.isOk(), r.getMensaje(), r.getData());
    }
    
    // ==================== MÉTODOS DE ACTIVIDADES ====================
    
    @WebMethod
    public RespuestaGeneral<ActividadDto> crearActividad(
            @WebParam(name = "actividad") ActividadDto actividad) {
        return actividadService.crear(actividad);
    }
    
    @WebMethod
    public RespuestaGeneral<ActividadDto> actualizarActividad(
            @WebParam(name = "actividad") ActividadDto actividad) {
        return actividadService.actualizar(actividad);
    }
    
    @WebMethod
    public RespuestaGeneral<Void> eliminarActividad(
            @WebParam(name = "id") Long id) {
        return actividadService.eliminar(id);
    }
    
    @WebMethod
    public RespuestaGeneralLista<ActividadDto> obtenerActividadesPorProyecto(
            @WebParam(name = "proyectoId") Long proyectoId) {
        var r = actividadService.buscarPorProyecto(proyectoId); // RespuestaGeneral<List<ActividadDto>>
        return new RespuestaGeneralLista<>(r.isOk(), r.getMensaje(), r.getData());
    }
    
    @WebMethod
    public RespuestaGeneral<Void> reordenarActividades(
            @WebParam(name = "proyectoId") Long proyectoId,
            @WebParam(name = "nuevoOrden") List<Long> nuevoOrden) {
        return actividadService.reordenarActividades(proyectoId, nuevoOrden);
    }
}