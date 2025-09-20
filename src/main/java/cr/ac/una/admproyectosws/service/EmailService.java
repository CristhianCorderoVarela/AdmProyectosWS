package cr.ac.una.admproyectosws.service;

import cr.ac.una.admproyectosws.model.Actividad;
import cr.ac.una.admproyectosws.model.Proyecto;
import cr.ac.una.admproyectosws.model.SeguimientoProyecto;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.Stateless;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

@Stateless
public class EmailService {

    @Asynchronous
    public void notificarCreacionProyecto(Proyecto proyecto) {
        try {
            System.out.println("[EmailService] INICIO notificarCreacionProyecto");
            System.out.println("[EmailService] Proyecto: " + proyecto.getNombre());

            String subject = "Nuevo Proyecto Creado: " + val(proyecto.getNombre());
            System.out.println("[EmailService] Subject creado: " + subject);

            System.out.println("[EmailService] Generando HTML...");
            String htmlContent = construirHtmlProyecto(proyecto, "creado");
            System.out.println("[EmailService] HTML generado exitosamente");

            List<String> destinatarios = Arrays.asList(
                    proyecto.getPatrocinadorCorreo(),
                    proyecto.getLiderUsuarioCorreo(),
                    proyecto.getLiderTecnicoCorreo()
            );

            System.out.println("[EmailService] Destinatarios: " + destinatarios);
            System.out.println("[EmailService] Llamando enviarCorreoHtml...");

            enviarCorreoHtml(destinatarios, subject, htmlContent);
            System.out.println("[EmailService] FIN notificarCreacionProyecto - EXITOSO");

        } catch (Exception e) {
            System.err.println("[EmailService] ERROR CRITICO en notificarCreacionProyecto:");
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println("Tipo: " + e.getClass().getSimpleName());
            e.printStackTrace();
        }
    }
    @Asynchronous
    public void notificarCambioEstadoProyecto(Proyecto proyecto, String estadoAnterior) {
        try {
            System.out.println("[EmailService] Iniciando notificación de cambio de estado");
            
            String subject = String.format(
                    "Cambio de Estado - Proyecto: %s [%s → %s]",
                    val(proyecto.getNombre()),
                    val(estadoAnterior),
                    val(proyecto.getEstado())
            );
            String htmlContent = construirHtmlCambioEstado(proyecto, estadoAnterior);

            List<String> destinatarios = Arrays.asList(
                    proyecto.getPatrocinadorCorreo(),
                    proyecto.getLiderUsuarioCorreo(),
                    proyecto.getLiderTecnicoCorreo()
            );

            enviarCorreoHtml(destinatarios, subject, htmlContent);
            System.out.println("[EmailService] Notificación de cambio de estado completada");
            
        } catch (Exception e) {
            System.err.println("[EmailService] ERROR en notificarCambioEstadoProyecto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Asynchronous
    public void notificarCreacionActividad(Actividad actividad) {
        try {
            System.out.println("[EmailService] Iniciando notificación de creación de actividad: " + actividad.getDescripcion());
            
            String subject = "Nueva Actividad Asignada: " + val(actividad.getDescripcion());
            String htmlContent = construirHtmlActividad(actividad, "asignada");

            System.out.println("[EmailService] Enviando a: " + actividad.getEncargadoCorreo());
            enviarCorreoHtml(Arrays.asList(actividad.getEncargadoCorreo()), subject, htmlContent);
            System.out.println("[EmailService] Notificación de creación de actividad completada");
            
        } catch (Exception e) {
            System.err.println("[EmailService] ERROR en notificarCreacionActividad: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Asynchronous
    public void notificarCambioEstadoActividad(Actividad actividad, String estadoAnterior) {
        try {
            System.out.println("[EmailService] Iniciando notificación de cambio de estado de actividad");
            
            String subject = String.format(
                    "Cambio de Estado - Actividad: %s [%s → %s]",
                    val(actividad.getDescripcion()),
                    val(estadoAnterior),
                    val(actividad.getEstado())
            );
            String htmlContent = construirHtmlCambioEstadoActividad(actividad, estadoAnterior);

            enviarCorreoHtml(Arrays.asList(actividad.getEncargadoCorreo()), subject, htmlContent);
            System.out.println("[EmailService] Notificación de cambio de estado de actividad completada");
            
        } catch (Exception e) {
            System.err.println("[EmailService] ERROR en notificarCambioEstadoActividad: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Asynchronous
    public void notificarSeguimientoProyecto(SeguimientoProyecto seguimiento) {
        try {
            System.out.println("[EmailService] Iniciando notificación de seguimiento");
            
            Proyecto proyecto = seguimiento.getProyecto();
            String subject = "Nuevo Seguimiento - Proyecto: " + val(proyecto.getNombre());
            String htmlContent = construirHtmlSeguimiento(seguimiento);

            List<String> destinatarios = Arrays.asList(
                    proyecto.getPatrocinadorCorreo(),
                    proyecto.getLiderUsuarioCorreo(),
                    proyecto.getLiderTecnicoCorreo()
            );

            System.out.println("[EmailService] Destinatarios: " + destinatarios);
            enviarCorreoHtml(destinatarios, subject, htmlContent);
            System.out.println("[EmailService] Notificación de seguimiento completada");
            
        } catch (Exception e) {
            System.err.println("[EmailService] ERROR en notificarSeguimientoProyecto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void enviarCorreoHtml(List<String> destinatarios, String subject, String htmlContent) {
        try {
            System.out.println("[EmailService] Iniciando envío de correo: " + subject);
            
            // CORREGIDO: Usar collect(Collectors.toList()) en lugar de toList()
            List<String> limpios = destinatarios == null ? Arrays.asList()
                    : destinatarios.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .distinct()
                    .collect(Collectors.toList()); // ESTE ERA EL PROBLEMA
            
            if (limpios.isEmpty()) {
                System.out.println("[EmailService] No hay destinatarios válidos. Se omite el envío.");
                return;
            }
            
            System.out.println("[EmailService] Destinatarios válidos: " + limpios);

            // Cargar configuración
            Properties fileProps = cargarMailProperties();
            String host = fileProps.getProperty("mail.host", "smtp.gmail.com");
            String port = fileProps.getProperty("mail.port", "587");
            String user = fileProps.getProperty("mail.user");
            String pass = fileProps.getProperty("mail.pass");
            String useTls = fileProps.getProperty("mail.tls", "true");

            System.out.println("[EmailService] Config - Host: " + host + ", Port: " + port + ", User: " + user);

            // Configuración de sesión
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.mime.charset", "UTF-8");
            props.put("mail.smtp.starttls.enable", useTls);

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, pass);
                }
            });

            // Crear mensaje
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(user));
            
            // Convertir destinatarios - CORREGIDO
            InternetAddress[] addresses = limpios.stream()
                    .map(this::createAddress)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList())  // CAMBIADO DE toArray() directo
                    .toArray(new InternetAddress[0]);
                    
            msg.setRecipients(Message.RecipientType.TO, addresses);
            msg.setSubject(subject, StandardCharsets.UTF_8.name());

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, "text/html; charset=UTF-8");

            MimeMultipart mixed = new MimeMultipart("mixed");
            mixed.addBodyPart(htmlPart);
            msg.setContent(mixed);

            // Enviar
            System.out.println("[EmailService] Enviando mensaje...");
            Transport.send(msg);
            System.out.println("[EmailService] ¡CORREO ENVIADO EXITOSAMENTE!");
            System.out.println("[EmailService] Destinatarios: " + String.join(", ", limpios));

        } catch (Exception e) {
            System.err.println("[EmailService] ERROR CRÍTICO enviando correo: " + e.getMessage());
            System.err.println("[EmailService] Tipo de error: " + e.getClass().getSimpleName());
            e.printStackTrace();
        }
    }

    private InternetAddress createAddress(String email) {
        try {
            InternetAddress addr = new InternetAddress(email, true);
            System.out.println("[EmailService] Email válido: " + email);
            return addr;
        } catch (Exception e) {
            System.err.println("[EmailService] Email INVÁLIDO: " + email + " - " + e.getMessage());
            return null;
        }
    }

    private String construirHtmlProyecto(Proyecto proyecto, String accion) {
        return String.format("""
        <html>
          <body style="font-family: Arial, sans-serif">
            <h2 style="color:#2E86C1">Nuevo proyecto creado</h2>
            <p>Se ha registrado un nuevo proyecto en el sistema.</p>

            <table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse">
              <tr><th align="left">Proyecto</th><td>%s</td></tr>
              <tr><th align="left">Patrocinador</th><td>%s</td></tr>
              <tr><th align="left">Líder usuario</th><td>%s</td></tr>
              <tr><th align="left">Líder técnico</th><td>%s</td></tr>
              <tr><th align="left">Estado inicial</th><td>%s</td></tr>
              <tr><th align="left">Inicio planificado</th><td>%s</td></tr>
              <tr><th align="left">Fin planificado</th><td>%s</td></tr>
            </table>

            <p>— Equipo AdmProyectos<br>
            <small>Este mensaje fue generado automáticamente.</small></p>
          </body>
        </html>
        """,
                val(proyecto.getNombre()),
                val(proyecto.getPatrocinadorNombre()),
                val(proyecto.getLiderUsuarioNombre()),
                val(proyecto.getLiderTecnicoNombre()),
                val(proyecto.getEstado()),
                formatearFecha(proyecto.getFechaInicioPlanificada()),
                formatearFecha(proyecto.getFechaFinalPlanificada())
        );
    }
    private String construirHtmlCambioEstado(Proyecto proyecto, String estadoAnterior) {
        return """
        <html>
          <body style="font-family: Arial, sans-serif; color:#333;">
            <h2 style="color:#117A65; margin-bottom:6px;">Actualización de estado del proyecto</h2>
            <p>El proyecto <b>%s</b> ha cambiado de estado.</p>

            <ul>
              <li><b>Estado anterior:</b> %s</li>
              <li><b>Nuevo estado:</b> %s</li>
              <li><b>Porcentaje de avance:</b> %s</li>
              <li><b>Fecha del cambio:</b> %s</li>
            </ul>

            <p>— Equipo AdmProyectos<br>
            <small>Este mensaje fue generado automáticamente.</small></p>
          </body>
        </html>
        """.formatted(
                val(proyecto.getNombre()),
                val(estadoAnterior),
                val(proyecto.getEstado()),
                proyecto.getPorcentajeAvance() == null ? "—" : proyecto.getPorcentajeAvance() + "%",
                formatearFecha(new Date())
        );
    }
    
    
    
    private String construirHtmlActividad(Actividad actividad, String accion) {
        return String.format("""
        <html>
          <body style="font-family: Arial, sans-serif">
            <h2 style="color:#AF601A">Nueva actividad registrada</h2>
            <p>Se ha agregado una actividad al proyecto <b>%s</b>.</p>

            <table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse">
              <tr><th align="left">Descripción</th><td>%s</td></tr>
              <tr><th align="left">Encargado</th><td>%s</td></tr>
              <tr><th align="left">Estado</th><td>%s</td></tr>
              <tr><th align="left">Inicio planificado</th><td>%s</td></tr>
              <tr><th align="left">Fin planificado</th><td>%s</td></tr>
              <tr><th align="left">Orden de ejecución</th><td>%s</td></tr>
            </table>

            <p>— Equipo AdmProyectos<br>
            <small>Este mensaje fue generado automáticamente.</small></p>
          </body>
        </html>
        """,
                val(actividad.getProyecto() != null ? actividad.getProyecto().getNombre() : null),
                val(actividad.getDescripcion()),
                val(actividad.getEncargadoNombre()),
                val(actividad.getEstado()),
                formatearFecha(actividad.getFechaInicioPlanificada()),
                formatearFecha(actividad.getFechaFinalPlanificada()),
                val(actividad.getOrdenEjecucion())
        );
    }

    private String construirHtmlCambioEstadoActividad(Actividad actividad, String estadoAnterior) {
        return String.format("""
        <html>
          <body style="font-family: Arial, sans-serif">
            <h2 style="color:#6C3483">Estado de actividad actualizado</h2>
            <p>La actividad <b>%s</b> en el proyecto <b>%s</b> ha cambiado de estado.</p>

            <ul>
              <li><b>Estado anterior:</b> %s</li>
              <li><b>Nuevo estado:</b> %s</li>
              <li><b>Fecha inicio real:</b> %s</li>
              <li><b>Fecha fin real:</b> %s</li>
              <li><b>Orden de ejecución:</b> %s</li>
            </ul>

            <p>— Equipo AdmProyectos<br>
            <small>Este mensaje fue generado automáticamente.</small></p>
          </body>
        </html>
        """,
                val(actividad.getDescripcion()),
                val(actividad.getProyecto() != null ? actividad.getProyecto().getNombre() : null),
                val(estadoAnterior),
                val(actividad.getEstado()),
                formatearFecha(actividad.getFechaInicioReal()),
                formatearFecha(actividad.getFechaFinalReal()),
                val(actividad.getOrdenEjecucion())
        );
    }

    private String construirHtmlSeguimiento(SeguimientoProyecto seguimiento) {
        return String.format("""
        <html>
          <body style="font-family: Arial, sans-serif">
            <h2 style="color:#2E4053">Nuevo seguimiento agregado</h2>
            <p>El proyecto <b>%s</b> tiene un nuevo seguimiento:</p>

            <blockquote style="border-left:3px solid #ccc">
              "%s"
            </blockquote>

            <table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse">
              <tr><th align="left">Porcentaje de avance</th><td>%s</td></tr>
              <tr><th align="left">Fecha del seguimiento</th><td>%s</td></tr>
              <tr><th align="left">Registrado por</th><td>%s</td></tr>
            </table>

            <p>— Equipo AdmProyectos<br>
            <small>Este mensaje fue generado automáticamente.</small></p>
          </body>
        </html>
        """,
                val(seguimiento.getProyecto() != null ? seguimiento.getProyecto().getNombre() : null),
                val(seguimiento.getObservaciones()),
                seguimiento.getPorcentajeAvance() == null ? "—" : seguimiento.getPorcentajeAvance() + "%%",
                formatearFecha(seguimiento.getFechaSeguimiento()),
                seguimiento.getCreadoPor() == null
                        ? "—"
                        : (val(seguimiento.getCreadoPor().getNombre()) + " " + val(seguimiento.getCreadoPor().getApellidos()))
        );
    }

    private Properties cargarMailProperties() {
        System.out.println("[EmailService] Cargando mail.properties...");
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("mail.properties")) {
            if (input == null) {
                System.err.println("[EmailService] ERROR: mail.properties NO encontrado");
                throw new RuntimeException("No se encontró mail.properties en resources");
            }
            Properties props = new Properties();
            props.load(input);
            System.out.println("[EmailService] mail.properties cargado exitosamente");
            return props;
        } catch (IOException ex) {
            System.err.println("[EmailService] ERROR de IO: " + ex.getMessage());
            throw new RuntimeException("Error cargando mail.properties: " + ex.getMessage(), ex);
        }
    }

    private String formatearFecha(Date fecha) {
        if (fecha == null) return "—";
        return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
    }

    private String val(Object o) {
        if (o == null) return "—";
        String s = String.valueOf(o).trim();
        return s.isEmpty() ? "—" : s;
    }
}