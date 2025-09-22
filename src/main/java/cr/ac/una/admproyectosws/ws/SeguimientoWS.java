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

// Esto expone un servicio SOAP para gestionar seguimientos de proyecto
@Stateless
@WebService(serviceName = "SeguimientoService")
public class SeguimientoWS {

    @EJB
    private SeguimientoService seguimientoService;

    // Esto confirma que el servicio está activo
    @WebMethod
    public String ping() { return "SeguimientoService OK"; }

    // Esto crea un seguimiento y devuelve el resultado
    @WebMethod
    public RespuestaWsLista crearSeguimiento(@WebParam(name = "dto") SeguimientoProyectoDto dto) {
        return seguimientoService.crear(dto);
    }

    // Esto actualiza un seguimiento existente
    @WebMethod
    public RespuestaWsLista actualizarSeguimiento(@WebParam(name = "dto") SeguimientoProyectoDto dto) {
        return seguimientoService.actualizar(dto);
    }
// Esto elimina un seguimiento por su id
    @WebMethod
    public RespuestaWsLista eliminarSeguimiento(@WebParam(name = "id") Long id) {
        return seguimientoService.eliminar(id);
    }

    // Esto lista seguimientos asociados a un proyecto
    @WebMethod
    public RespuestaWsLista buscarSeguimientosPorProyecto(@WebParam(name = "proyectoId") Long proyectoId) {
        return seguimientoService.buscarPorProyecto(proyectoId);
    }

    
    // Esto obtiene el último seguimiento registrado del proyecto
    @WebMethod
    public RespuestaWsLista buscarUltimoSeguimiento(@WebParam(name = "proyectoId") Long proyectoId) {
        return seguimientoService.buscarUltimo(proyectoId);
    }

    // Esto filtra seguimientos por rango de fechas
    @WebMethod
    public RespuestaWsLista buscarSeguimientosPorFecha(
            @WebParam(name = "fechaInicio") Date fechaInicio,
            @WebParam(name = "fechaFin") Date fechaFin) {
        return seguimientoService.buscarPorFecha(fechaInicio, fechaFin);
    }
}
