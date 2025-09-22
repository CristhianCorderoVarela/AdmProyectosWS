package cr.ac.una.admproyectosws.ws;

import cr.ac.una.admproyectosws.dto.AdministradorDto;
import cr.ac.una.admproyectosws.dto.RespuestaLogin;
import cr.ac.una.admproyectosws.model.Administrador;
import cr.ac.una.admproyectosws.utils.PasswordUtil;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

// Esto sirve para autenticar administradores via SOAP
@Stateless
@WebService(serviceName = "AuthService")
public class AuthWS {

    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    // Esto confirma que el servicio esta activo
    @WebMethod
    public String ping() { return "AuthService OK"; }

    // Esto hace login con usuario y contraseña
    @WebMethod
    public RespuestaLogin login(
            @WebParam(name = "username") String username,
            @WebParam(name = "password") String password) {

        try {
            Administrador a = em.createQuery(
                    "SELECT a FROM Administrador a " +
                    "WHERE a.usuario = :usr AND a.estado = 'ACTIVO'",
                    Administrador.class)
                .setParameter("usr", username == null ? null : username.trim())
                .getSingleResult();

            String stored = a.getPasswordPlain(); 
            String plain  = password == null ? null : password.trim();

            if (!PasswordUtil.checkFlexible(plain, stored)) {
                return new RespuestaLogin(false, "Usuario o contraseña incorrectos", null);
            }

            
            if (!PasswordUtil.isBcryptHash(stored)) {
                a.setPasswordPlain(PasswordUtil.hash(plain));
                em.merge(a);
            }

            AdministradorDto dto = new AdministradorDto(
                a.getId(), a.getNombre(), a.getApellidos(),
                a.getCedula(), a.getCorreo(), a.getUsuario(), a.getEstado()
            );
            dto.setPasswordPlain(null); 

            return new RespuestaLogin(true, "Autenticación exitosa", dto);

        } catch (NoResultException ex) {
            return new RespuestaLogin(false, "Usuario o contraseña incorrectos", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new RespuestaLogin(false, "Error del servidor: " + e.getMessage(), null);
        }
    }
}
