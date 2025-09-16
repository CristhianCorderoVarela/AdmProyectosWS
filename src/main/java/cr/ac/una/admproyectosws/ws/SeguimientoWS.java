package cr.ac.una.admproyectosws.ws;

import cr.ac.una.admproyectosws.dto.SeguimientoProyectoDto;
import cr.ac.una.admproyectosws.dto.RespuestaWsLista;
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
    public String ping() { return "SeguimientoService OK"; }

    // ----------- CRUD -----------
    @WebMethod
    public RespuestaWsLista crearSeguimiento(@WebParam(name = "dto") SeguimientoProyectoDto dto) {
        return seguimientoService.crear(dto);
    }

    @WebMethod
    public RespuestaWsLista actualizarSeguimiento(@WebParam(name = "dto") SeguimientoProyectoDto dto) {
        return seguimientoService.actualizar(dto);
    }

    @WebMethod
    public RespuestaWsLista eliminarSeguimiento(@WebParam(name = "id") Long id) {
        return seguimientoService.eliminar(id);
    }

    // ----------- Consultas -----------
    @WebMethod
    public RespuestaWsLista buscarSeguimientosPorProyecto(@WebParam(name = "proyectoId") Long proyectoId) {
        return seguimientoService.buscarPorProyecto(proyectoId);
    }

    @WebMethod
    public RespuestaWsLista buscarUltimoSeguimiento(@WebParam(name = "proyectoId") Long proyectoId) {
        return seguimientoService.buscarUltimo(proyectoId);
    }

    @WebMethod
    public RespuestaWsLista buscarSeguimientosPorFecha(
            @WebParam(name = "fechaInicio") Date fechaInicio,
            @WebParam(name = "fechaFin") Date fechaFin) {
        return seguimientoService.buscarPorFecha(fechaInicio, fechaFin);
    }
}
