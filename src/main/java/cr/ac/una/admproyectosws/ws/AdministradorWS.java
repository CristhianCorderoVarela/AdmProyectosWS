package cr.ac.una.admproyectosws.ws;

import cr.ac.una.admproyectosws.dto.AdministradorDto;
import cr.ac.una.admproyectosws.dto.RespuestaGeneral;
import cr.ac.una.admproyectosws.service.AdministradorService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;

@Stateless
@WebService(serviceName = "AdministradorService")
public class AdministradorWS {
    
    @EJB
    private AdministradorService administradorService;
    
    @WebMethod
    public String ping() { 
        return "AdministradorService OK"; 
    }
    
    @WebMethod
    public RespuestaGeneral<AdministradorDto> crearAdministrador(
            @WebParam(name = "administrador") AdministradorDto administrador) {
        return administradorService.crear(administrador);
    }
    
    @WebMethod
    public RespuestaGeneral<AdministradorDto> actualizarAdministrador(
            @WebParam(name = "administrador") AdministradorDto administrador) {
        return administradorService.actualizar(administrador);
    }
    
    @WebMethod
    public RespuestaGeneral<Void> eliminarAdministrador(
            @WebParam(name = "id") Long id) {
        return administradorService.eliminar(id);
    }
    
    @WebMethod
    public RespuestaGeneral<AdministradorDto> buscarAdministradorPorId(
            @WebParam(name = "id") Long id) {
        return administradorService.buscarPorId(id);
    }
    
    @WebMethod
    public RespuestaGeneral<List<AdministradorDto>> obtenerTodosAdministradores() {
        return administradorService.obtenerTodos();
    }
    
    @WebMethod
    public RespuestaGeneral<List<AdministradorDto>> buscarAdministradores(
            @WebParam(name = "filtro") String filtro) {
        return administradorService.buscar(filtro);
    }
}