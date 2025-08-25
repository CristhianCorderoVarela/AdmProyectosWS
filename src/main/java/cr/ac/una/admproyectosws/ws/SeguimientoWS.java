package cr.ac.una.admproyectosws.ws;

import cr.ac.una.admproyectosws.dto.SeguimientoProyectoDto;
import cr.ac.una.admproyectosws.dto.RespuestaGeneral;
import cr.ac.una.admproyectosws.service.SeguimientoService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.Date;
import java.util.List;

@Stateless
@WebService(serviceName = "SeguimientoService")
public class SeguimientoWS {
    
    @EJB
    private SeguimientoService seguimientoService;
    
    @WebMethod
    public String ping() { 
        return "SeguimientoService OK"; 
    }
    
    /**
    Esperar a agregar el metodo crear en seguimientoService o ver que hacer
    @WebMethod
    public RespuestaGeneral<SeguimientoProyectoDto> crearSeguimiento(
            @WebParam(name = "seguimiento") SeguimientoProyectoDto seguimiento,
            @WebParam(name = "proyectoId") Long proyectoId,
            @WebParam(name = "creadoPorId") Long creadoPorId) {
        return seguimientoService.crear(seguimiento, proyectoId, creadoPorId);
    }
    */
    
    
    
    @WebMethod
    public RespuestaGeneral<List<SeguimientoProyectoDto>> buscarSeguimientosPorProyecto(
            @WebParam(name = "proyectoId") Long proyectoId) {
        return seguimientoService.buscarPorProyecto(proyectoId);
    }
    
    @WebMethod
    public RespuestaGeneral<SeguimientoProyectoDto> buscarUltimoSeguimiento(
            @WebParam(name = "proyectoId") Long proyectoId) {
        return seguimientoService.buscarUltimo(proyectoId);
    }
    
    @WebMethod
    public RespuestaGeneral<List<SeguimientoProyectoDto>> buscarSeguimientosPorFecha(
            @WebParam(name = "fechaInicio") Date fechaInicio,
            @WebParam(name = "fechaFin") Date fechaFin) {
        return seguimientoService.buscarPorFecha(fechaInicio, fechaFin);
    }
}
