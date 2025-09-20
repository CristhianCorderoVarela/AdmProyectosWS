package cr.ac.una.admproyectosws.service;

import cr.ac.una.admproyectosws.dao.SeguimientoProyectoDao;
import cr.ac.una.admproyectosws.dao.ProyectoDao;
import cr.ac.una.admproyectosws.dao.AdministradorDao;
import cr.ac.una.admproyectosws.dto.SeguimientoProyectoDto;
import cr.ac.una.admproyectosws.dto.RespuestaWsLista;
import cr.ac.una.admproyectosws.model.SeguimientoProyecto;
import cr.ac.una.admproyectosws.model.Proyecto;
import cr.ac.una.admproyectosws.model.Administrador;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class SeguimientoService {
    
    @EJB private EmailService emailService;
    @EJB private SeguimientoProyectoDao seguimientoDao;
    @EJB private ProyectoDao proyectoDao;
    @EJB private AdministradorDao administradorDao;

    @PersistenceContext(unitName = "ProyectoPU")
    private EntityManager em;

    // ====== CRUD ======

    public RespuestaWsLista crear(SeguimientoProyectoDto dto) {
    try {
        validar(dto, true);

        Proyecto proyecto = em.find(Proyecto.class, dto.getProyectoId());
        if (proyecto == null) return RespuestaWsLista.error("Proyecto no existe");

        Administrador creador = em.find(Administrador.class, dto.getCreadoPorId());
        if (creador == null) return RespuestaWsLista.error("Administrador no existe");

        SeguimientoProyecto ent = new SeguimientoProyecto();
        copiar(dto, ent, proyecto, creador);
        em.persist(ent);

        if (dto.getPorcentajeAvance() != null) {
            proyecto.setPorcentajeAvance(dto.getPorcentajeAvance());
        }

        em.flush();

        try {
            emailService.notificarSeguimientoProyecto(ent);
        } catch (Exception ex) {
            System.err.println("Error enviando notificación de seguimiento: " + ex.getMessage());
        }

        return RespuestaWsLista.okUno(new SeguimientoProyectoDto(ent), "Seguimiento creado");
    } catch (Exception e) {
        return RespuestaWsLista.error("Error al crear seguimiento: " + e.getMessage());
    }
}


    public RespuestaWsLista actualizar(SeguimientoProyectoDto dto) {
        try {
            if (dto.getId() == null) return RespuestaWsLista.error("Id es requerido");

            validar(dto, false);

            SeguimientoProyecto ent = em.find(SeguimientoProyecto.class, dto.getId());
            if (ent == null) return RespuestaWsLista.error("Seguimiento no existe");

            Proyecto proyecto = (dto.getProyectoId() != null)
                    ? em.find(Proyecto.class, dto.getProyectoId())
                    : ent.getProyecto();

            Administrador creador = (dto.getCreadoPorId() != null)
                    ? em.find(Administrador.class, dto.getCreadoPorId())
                    : ent.getCreadoPor();

            copiar(dto, ent, proyecto, creador);
            ent = em.merge(ent);

            if (dto.getPorcentajeAvance() != null) {
                proyecto.setPorcentajeAvance(dto.getPorcentajeAvance());
            }

            em.flush();
            return RespuestaWsLista.okUno(new SeguimientoProyectoDto(ent), "Seguimiento actualizado");
        } catch (Exception e) {
            return RespuestaWsLista.error("Error al actualizar seguimiento: " + e.getMessage());
        }
    }

    public RespuestaWsLista eliminar(Long id) {
        try {
            if (id == null) return RespuestaWsLista.error("Id es requerido");

            SeguimientoProyecto ent = em.find(SeguimientoProyecto.class, id);
            if (ent == null) return RespuestaWsLista.error("Seguimiento no existe");

            em.remove(ent);
            return RespuestaWsLista.okVacio("Seguimiento eliminado");
        } catch (Exception e) {
            return RespuestaWsLista.error("Error al eliminar seguimiento: " + e.getMessage());
        }
    }

    // ====== Consultas ======

    public RespuestaWsLista buscarPorProyecto(Long proyectoId) {
        try {
            List<SeguimientoProyecto> seguimientos = seguimientoDao.buscarPorProyecto(proyectoId);
            List<SeguimientoProyectoDto> dtos = seguimientos.stream()
                    .map(SeguimientoProyectoDto::new)
                    .collect(Collectors.toList());
            return RespuestaWsLista.okLista(dtos, "Seguimientos obtenidos");
        } catch (Exception e) {
            return RespuestaWsLista.error("Error al obtener seguimientos: " + e.getMessage());
        }
    }

    public RespuestaWsLista buscarUltimo(Long proyectoId) {
        try {
            Optional<SeguimientoProyecto> seguimientoOpt = seguimientoDao.buscarUltimoPorProyecto(proyectoId);
            return seguimientoOpt.isPresent()
                    ? RespuestaWsLista.okUno(new SeguimientoProyectoDto(seguimientoOpt.get()), "Último seguimiento encontrado")
                    : RespuestaWsLista.error("No se encontraron seguimientos para este proyecto");
        } catch (Exception e) {
            return RespuestaWsLista.error("Error al buscar último seguimiento: " + e.getMessage());
        }
    }

    public RespuestaWsLista buscarPorFecha(Date fechaInicio, Date fechaFin) {
        try {
            List<SeguimientoProyecto> seguimientos = seguimientoDao.buscarPorFecha(fechaInicio, fechaFin);
            List<SeguimientoProyectoDto> dtos = seguimientos.stream()
                    .map(SeguimientoProyectoDto::new)
                    .collect(Collectors.toList());
            return RespuestaWsLista.okLista(dtos, "Seguimientos por fecha obtenidos");
        } catch (Exception e) {
            return RespuestaWsLista.error("Error al buscar seguimientos por fecha: " + e.getMessage());
        }
    }

    // ====== Helpers ======
    private void validar(SeguimientoProyectoDto d, boolean esCrear) {
        if (esCrear && d.getProyectoId() == null)
            throw new IllegalArgumentException("proyectoId es requerido");
        if (esCrear && d.getCreadoPorId() == null)
            throw new IllegalArgumentException("creadoPorId es requerido");
        if (d.getPorcentajeAvance() != null &&
                (d.getPorcentajeAvance() < 0 || d.getPorcentajeAvance() > 100))
            throw new IllegalArgumentException("porcentajeAvance debe estar entre 0 y 100");
    }

    private void copiar(SeguimientoProyectoDto d, SeguimientoProyecto e, Proyecto p, Administrador a) {
        if (p != null) e.setProyecto(p);
        if (a != null) e.setCreadoPor(a);
        if (d.getFechaSeguimiento() != null) e.setFechaSeguimiento(d.getFechaSeguimiento());
        e.setObservaciones(d.getObservaciones());
        e.setPorcentajeAvance(d.getPorcentajeAvance());
    }
}
