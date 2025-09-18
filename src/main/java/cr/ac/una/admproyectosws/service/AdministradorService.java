package cr.ac.una.admproyectosws.service;

import cr.ac.una.admproyectosws.dao.AdministradorDao;
import cr.ac.una.admproyectosws.dto.AdministradorDto;
import cr.ac.una.admproyectosws.dto.RespuestaGeneral;
import cr.ac.una.admproyectosws.model.Administrador;
import cr.ac.una.admproyectosws.utils.PasswordUtil;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class AdministradorService {

    @EJB
    private AdministradorDao administradorDao;

    public RespuestaGeneral<AdministradorDto> crear(AdministradorDto dto) {
        try {
            // Validaciones mínimas
            if (dto.getNombre() == null || dto.getNombre().trim().isEmpty())
                return new RespuestaGeneral<>(false, "El nombre es requerido");
            if (dto.getApellidos() == null || dto.getApellidos().trim().isEmpty())
                return new RespuestaGeneral<>(false, "Los apellidos son requeridos");
            if (dto.getCedula() == null || dto.getCedula().trim().isEmpty())
                return new RespuestaGeneral<>(false, "La cédula es requerida");
            if (dto.getCorreo() == null || dto.getCorreo().trim().isEmpty())
                return new RespuestaGeneral<>(false, "El correo es requerido");
            if (dto.getUsuario() == null || dto.getUsuario().trim().isEmpty())
                return new RespuestaGeneral<>(false, "El usuario es requerido");
            if (dto.getPasswordPlain() == null || dto.getPasswordPlain().trim().isEmpty())
                return new RespuestaGeneral<>(false, "La contraseña es obligatoria");

            // Unicidad
            if (administradorDao.existeUsuario(dto.getUsuario()))
                return new RespuestaGeneral<>(false, "Ya existe un administrador con ese usuario");
            if (administradorDao.existeCorreo(dto.getCorreo()))
                return new RespuestaGeneral<>(false, "Ya existe un administrador con ese correo");
            if (administradorDao.existeCedula(dto.getCedula()))
                return new RespuestaGeneral<>(false, "Ya existe un administrador con esa cédula");

            // Mapear entidad
            Administrador admin = dto.toEntity();
            if (admin.getEstado() == null) admin.setEstado("ACTIVO");

            // Hashear y guardar
            admin.setPasswordPlain(PasswordUtil.hash(dto.getPasswordPlain()));

            admin = administradorDao.crear(admin);

            AdministradorDto out = new AdministradorDto(admin);
            out.setPasswordPlain(null); // Nunca devolver password/hash
            return new RespuestaGeneral<>(true, "Administrador creado exitosamente", out);

        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al crear administrador: " + e.getMessage());
        }
    }

    public RespuestaGeneral<AdministradorDto> actualizar(AdministradorDto dto) {
        try {
            Optional<Administrador> adminOpt = administradorDao.buscarPorId(dto.getId());
            if (adminOpt.isEmpty())
                return new RespuestaGeneral<>(false, "Administrador no encontrado");

            Administrador admin = adminOpt.get();

            admin.setNombre(dto.getNombre());
            admin.setApellidos(dto.getApellidos());
            admin.setCedula(dto.getCedula());
            admin.setCorreo(dto.getCorreo());
            admin.setUsuario(dto.getUsuario());
            admin.setEstado(dto.getEstado());

            // Si viene nueva contraseña no vacía, la hasheamos y actualizamos
            if (dto.getPasswordPlain() != null && !dto.getPasswordPlain().trim().isEmpty()) {
                admin.setPasswordPlain(PasswordUtil.hash(dto.getPasswordPlain()));
            }

            admin = administradorDao.actualizar(admin);

            AdministradorDto out = new AdministradorDto(admin);
            out.setPasswordPlain(null); // Nunca devolver password/hash
            return new RespuestaGeneral<>(true, "Administrador actualizado exitosamente", out);

        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al actualizar administrador: " + e.getMessage());
        }
    }

    public RespuestaGeneral<Void> eliminar(Long id) {
        try {
            administradorDao.eliminar(id);
            return new RespuestaGeneral<>(true, "Administrador eliminado exitosamente");
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al eliminar administrador: " + e.getMessage());
        }
    }

    public RespuestaGeneral<AdministradorDto> buscarPorId(Long id) {
        try {
            Optional<Administrador> adminOpt = administradorDao.buscarPorId(id);
            if (adminOpt.isPresent()) {
                AdministradorDto out = new AdministradorDto(adminOpt.get());
                out.setPasswordPlain(null);
                return new RespuestaGeneral<>(true, "Administrador encontrado", out);
            } else {
                return new RespuestaGeneral<>(false, "Administrador no encontrado");
            }
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al buscar administrador: " + e.getMessage());
        }
    }

    public RespuestaGeneral<List<AdministradorDto>> obtenerTodos() {
        try {
            List<Administrador> administradores = administradorDao.obtenerTodos();
            List<AdministradorDto> dtos = administradores.stream()
                .map(a -> { AdministradorDto d = new AdministradorDto(a); d.setPasswordPlain(null); return d; })
                .collect(Collectors.toList());
            return new RespuestaGeneral<>(true, "Administradores obtenidos exitosamente", dtos);
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al obtener administradores: " + e.getMessage());
        }
    }

    public RespuestaGeneral<List<AdministradorDto>> buscar(String filtro) {
        try {
            List<Administrador> administradores = administradorDao.buscar(filtro);
            List<AdministradorDto> dtos = administradores.stream()
                .map(a -> { AdministradorDto d = new AdministradorDto(a); d.setPasswordPlain(null); return d; })
                .collect(Collectors.toList());
            return new RespuestaGeneral<>(true, "Búsqueda realizada exitosamente", dtos);
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error en la búsqueda: " + e.getMessage());
        }
    }
}
