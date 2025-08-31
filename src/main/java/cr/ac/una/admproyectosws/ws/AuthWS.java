package cr.ac.una.admproyectosws.ws;

import cr.ac.una.admproyectosws.dto.AdministradorDto;
import cr.ac.una.admproyectosws.dto.RespuestaLogin;
import cr.ac.una.admproyectosws.model.Administrador;

import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Stateless
@WebService(serviceName = "AuthService")
public class AuthWS {

    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    @WebMethod
    public String ping() {
        return "AuthService OK";
    }

    @WebMethod
    public RespuestaLogin login(
            @WebParam(name = "username") String username,
            @WebParam(name = "password") String password) {

        try {
            // Normalizamos entradas (evita NPE por trim si viene null, sin cambiar funcionalidad)
            final String usr = (username == null) ? "" : username.trim();
            final String pwd = (password == null) ? "" : password.trim();

            // *** CAMBIO MINIMO: usar NamedQuery predefinida en la entidad ***
            Administrador a = em.createNamedQuery("Administrador.login", Administrador.class)
                    .setParameter("usuario", usr)
                    .setParameter("password", pwd)
                    .getSingleResult();

            AdministradorDto dto = new AdministradorDto(
                a.getId(),
                a.getNombre(),
                a.getApellidos(),
                a.getCedula(),
                a.getCorreo(),
                a.getUsuario(),
                a.getEstado()
            );

            return new RespuestaLogin(true, "Autenticación exitosa", dto);

        } catch (NoResultException ex) {
            return new RespuestaLogin(false, "Usuario o contraseña incorrectos", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new RespuestaLogin(false, "Error del servidor: " + e.getMessage(), null);
        }
    }
}
