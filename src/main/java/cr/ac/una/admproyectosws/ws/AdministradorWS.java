package cr.ac.una.admproyectosws.ws;

import cr.ac.una.admproyectosws.dto.AdministradorDto;
import cr.ac.una.admproyectosws.dto.RespuestaGeneral;
import cr.ac.una.admproyectosws.service.AdministradorService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.Collections;
import java.util.List;

@Stateless
@WebService(serviceName = "AdministradorService")
// Esto sirve para exponer operaciones SOAP relacionadas con administradores
public class AdministradorWS {
    
    @EJB
    private AdministradorService administradorService;
    
    // Esto verifica que el servicio esté ok/activo
    @WebMethod
    public String ping() { 
        return "AdministradorService OK"; 
    }
    
     // Esto crea un administrador nuevo y devuelve una respuesta 
    @WebMethod
    public RespuestaGeneral<AdministradorDto> crearAdministrador(
            @WebParam(name = "administrador") AdministradorDto administrador) {
        return administradorService.crear(administrador);
    }
    
    // Esto actualiza un administrador existente
    @WebMethod
    public RespuestaGeneral<AdministradorDto> actualizarAdministrador(
            @WebParam(name = "administrador") AdministradorDto administrador) {
        return administradorService.actualizar(administrador);
    }
    
  //Esto elimina un administrador por su id
    @WebMethod
    public RespuestaGeneral<Void> eliminarAdministrador(
            @WebParam(name = "id") Long id) {
        return administradorService.eliminar(id);
    }
    
      // Esto busca un administrador por id
    @WebMethod
    public RespuestaGeneral<AdministradorDto> buscarAdministradorPorId(
            @WebParam(name = "id") Long id) {
        return administradorService.buscarPorId(id);
    }
    
    // Esto lista todos los administradores.
    @WebMethod
    public RespuestaGeneral<List<AdministradorDto>> obtenerTodosAdministradores() {
        return administradorService.obtenerTodos();
    }
    
    // Esto busca administradores usando un filtro de texto.
    @WebMethod
    public RespuestaGeneral<List<AdministradorDto>> buscarAdministradores(
            @WebParam(name = "filtro") String filtro) {
        return administradorService.buscar(filtro);
    }
    
    // Esto devuelve la lista en crudo
    @WebMethod
    public List<AdministradorDto> obtenerTodosPlano() {
        RespuestaGeneral<List<AdministradorDto>> rg = administradorService.obtenerTodos();
        if (rg != null && Boolean.TRUE.equals(rg.isOk()) && rg.getData() != null) {
            return rg.getData();
        }
        return Collections.emptyList();
    }
}