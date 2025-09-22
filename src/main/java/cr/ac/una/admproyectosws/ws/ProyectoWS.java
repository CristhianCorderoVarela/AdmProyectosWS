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


// Esto sirve para exponer operaciones de proyectos y actividades vía SOAP
@Stateless
@WebService(serviceName = "ProyectoService")
public class ProyectoWS {
    
    @EJB
    private ProyectoService proyectoService;
    
    @EJB
    private ActividadService actividadService;
    
    //Esto verifica que el servicio este ok
    @WebMethod
    public String ping() {
        return "ProyectoService OK";
    }
    
    
    // Esto crea un proyecto y registra quien lo creó
    @WebMethod
    public RespuestaGeneral<ProyectoDto> crearProyecto(
            @WebParam(name = "proyecto") ProyectoDto proyecto,
            @WebParam(name = "creadoPorId") Long creadoPorId) {
        return proyectoService.crear(proyecto, creadoPorId);
    }
    
    // Esto actualiza los datos de un proyecto
    @WebMethod
    public RespuestaGeneral<ProyectoDto> actualizarProyecto(
            @WebParam(name = "proyecto") ProyectoDto proyecto) {
        return proyectoService.actualizar(proyecto);
    }
    
    // Esto elimina un proyecto por id
    @WebMethod
    public RespuestaGeneral<Void> eliminarProyecto(
            @WebParam(name = "id") Long id) {
        return proyectoService.eliminar(id);
    }
    
    // Esto busca un proyecto por id
    @WebMethod
    public RespuestaGeneral<ProyectoDto> buscarProyectoPorId(
            @WebParam(name = "id") Long id) {
        return proyectoService.buscarPorId(id);
    }
    
    // Esto devuelve todos los proyectos
    @WebMethod
    public RespuestaGeneralLista<ProyectoDto> obtenerTodosProyectos() {
        var r = proyectoService.obtenerTodos(); 
        return new RespuestaGeneralLista<>(r.isOk(), r.getMensaje(), r.getData());
    }
    
    // Esto devuelve proyectos activos
    @WebMethod
    public RespuestaGeneralLista<ProyectoDto> buscarProyectosActivos() {
        var r = proyectoService.buscarActivos();
        return new RespuestaGeneralLista<>(r.isOk(), r.getMensaje(), r.getData());
    }
    
    // Esto busca proyectos por filtro usando streams
    @WebMethod
    public RespuestaGeneralLista<ProyectoDto> buscarProyectosConStreams(
            @WebParam(name = "filtro") String filtro) {
        var r = proyectoService.buscarConStreams(filtro);
        return new RespuestaGeneralLista<>(r.isOk(), r.getMensaje(), r.getData());
    }
    
   
    // Esto crea una actividad asociada a un proyecto
    @WebMethod
    public RespuestaGeneral<ActividadDto> crearActividad(
            @WebParam(name = "actividad") ActividadDto actividad) {
        return actividadService.crear(actividad);
    }
    
    // Esto actualiza una actividad existente
    @WebMethod
    public RespuestaGeneral<ActividadDto> actualizarActividad(
            @WebParam(name = "actividad") ActividadDto actividad) {
        return actividadService.actualizar(actividad);
    }
    
    // Esto elimina una actividad por id
    @WebMethod
    public RespuestaGeneral<Void> eliminarActividad(
            @WebParam(name = "id") Long id) {
        return actividadService.eliminar(id);
    }
    
     // Esto lista actividades de un proyecto
    @WebMethod
    public RespuestaGeneralLista<ActividadDto> obtenerActividadesPorProyecto(
            @WebParam(name = "proyectoId") Long proyectoId) {
        var r = actividadService.buscarPorProyecto(proyectoId); 
        return new RespuestaGeneralLista<>(r.isOk(), r.getMensaje(), r.getData());
    }
    
    // Esto reordena las actividades según el nuevo orden recibido
    @WebMethod
    public RespuestaGeneral<Void> reordenarActividades(
            @WebParam(name = "proyectoId") Long proyectoId,
            @WebParam(name = "nuevoOrden") List<Long> nuevoOrden) {
        return actividadService.reordenarActividades(proyectoId, nuevoOrden);
    }
}