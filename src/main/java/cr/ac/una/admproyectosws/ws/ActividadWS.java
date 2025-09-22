package cr.ac.una.admproyectosws.ws;

import cr.ac.una.admproyectosws.dto.ActividadDto;
import cr.ac.una.admproyectosws.dto.RespuestaGeneral;
import cr.ac.una.admproyectosws.service.ActividadService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;

@Stateless
@WebService(serviceName = "ActividadService")
// Esto sirve para exponer operaciones SOAP de Actividades
public class ActividadWS {
    
    @EJB
    private ActividadService actividadService;
    
    // Esto sirve para ver si el servicio está ok
    @WebMethod
    public String ping() { 
        return "ActividadService OK"; 
    }
    
    // Esto crea una actividad y devuelve una respuesta 
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
    
    // Esto elimina una actividad por su id
    @WebMethod
    public RespuestaGeneral<Void> eliminarActividad(
            @WebParam(name = "id") Long id) {
        return actividadService.eliminar(id);
    }
    
    // Esto lista actividades pertenecientes a un proyecto
    @WebMethod
    public RespuestaGeneral<List<ActividadDto>> buscarActividadesPorProyecto(
            @WebParam(name = "proyectoId") Long proyectoId) {
        return actividadService.buscarPorProyecto(proyectoId);
    }
    
    // Esto reordena actividades según el nuevo orden
    @WebMethod
    public RespuestaGeneral<Void> reordenarActividades(
            @WebParam(name = "proyectoId") Long proyectoId,
            @WebParam(name = "nuevoOrden") List<Long> nuevoOrden) {
        return actividadService.reordenarActividades(proyectoId, nuevoOrden);
    }
}
