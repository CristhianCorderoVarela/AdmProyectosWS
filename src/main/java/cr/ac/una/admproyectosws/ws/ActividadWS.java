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
public class ActividadWS {
    
    @EJB
    private ActividadService actividadService;
    
    @WebMethod
    public String ping() { 
        return "ActividadService OK"; 
    }
    
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
    public RespuestaGeneral<List<ActividadDto>> buscarActividadesPorProyecto(
            @WebParam(name = "proyectoId") Long proyectoId) {
        return actividadService.buscarPorProyecto(proyectoId);
    }
    
    @WebMethod
    public RespuestaGeneral<Void> reordenarActividades(
            @WebParam(name = "proyectoId") Long proyectoId,
            @WebParam(name = "nuevoOrden") List<Long> nuevoOrden) {
        return actividadService.reordenarActividades(proyectoId, nuevoOrden);
    }
}
