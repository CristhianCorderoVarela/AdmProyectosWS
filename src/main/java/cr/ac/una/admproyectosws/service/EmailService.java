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

/**
  Servicio de correos para eventos de Proyectos, Actividades y Seguimientos.
  - Stateless EJB para permitir ejecución concurrente sin estado.
 */




@Stateless
public class EmailService {

   
    private static final String BRAND_NAME  = "Projex";
    private static final String BRAND_COLOR = "#0ea5e9"; 
    private static final String BRAND_ACCENT = "#0369a1"; 

    // Colores por evento (cada uno diferente)
    private static final String COLOR_PROYECTO_CREADO          = BRAND_ACCENT; 
    private static final String COLOR_PROYECTO_CAMBIO_ESTADO   = "#16a34a";    
    private static final String COLOR_ACTIVIDAD_CREADA         = "#f59e0b";    
    private static final String COLOR_ACTIVIDAD_CAMBIO_ESTADO  = "#9333ea";    
    private static final String COLOR_SEGUIMIENTO              = "#6366f1";    

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

           
            List<String> limpios = destinatarios == null ? Arrays.asList()
                    : destinatarios.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .distinct()
                    .collect(Collectors.toList()); 

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

            
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(user));

            
            InternetAddress[] addresses = limpios.stream()
                    .map(this::createAddress)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList())  
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

    
    private String wrapEmail(String preheader, String title, String contentHtml) {
        return """
        <!doctype html>
        <html>
        <head>
          <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <title>%s</title>
          <style>
            @media only screen and (max-width: 620px) {
              .container { width: 100%% !important; padding: 0 12px !important; }
              .content { padding: 16px !important; }
              h1 { font-size: 20px !important; }
            }
          </style>
        </head>
        <body style="margin:0; padding:0; background:#f4f6f8;">
          <span style="display:none; color:transparent; visibility:hidden; opacity:0; height:0; width:0; overflow:hidden;">
            %s
          </span>

          <table role="presentation" cellpadding="0" cellspacing="0" border="0" width="100%%" style="background:#f4f6f8;">
            <tr>
              <td align="center" style="padding:24px 12px;">
                <table role="presentation" cellpadding="0" cellspacing="0" border="0" width="600" class="container"
                       style="width:600px; max-width:100%%; background:#ffffff; border-radius:10px; box-shadow:0 1px 6px rgba(0,0,0,.06); overflow:hidden;">
                  <!-- Header -->
                  <tr>
                    <td align="left" style="background:%s; padding:16px 24px;">
                      <table width="100%%" cellpadding="0" cellspacing="0" role="presentation">
                        <tr>
                          <td align="left" style="color:#fff; font:700 16px/1.2 Arial,Helvetica,sans-serif;">%s</td>
                          <td align="right" style="color:#dbeafe; font:400 12px/1.2 Arial,Helvetica,sans-serif;">Notificación automática</td>
                        </tr>
                      </table>
                    </td>
                  </tr>

                  <!-- Title + Body -->
                  <tr>
                    <td class="content" style="padding:24px;">
                      <h1 style="margin:0 0 12px; font:700 22px/1.3 Arial,Helvetica,sans-serif; color:#0f172a;">
                        %s
                      </h1>

                      %s

                      <p style="margin:24px 0 0; font:12px/1.6 Arial,Helvetica,sans-serif; color:#64748b;">
                        — Equipo %s<br>
                        <span style="color:#94a3b8;">Este mensaje fue generado automáticamente.</span>
                      </p>
                    </td>
                  </tr>

                </table>
                <div style="padding-top:12px; color:#94a3b8; font:12px Arial,Helvetica,sans-serif;">
                  © %s %s
                </div>
              </td>
            </tr>
          </table>
        </body>
        </html>
        """.formatted(
            escape(title),                
            escape(preheader),            
            BRAND_ACCENT,                  
            BRAND_NAME,                   
            escape(title),                 
            contentHtml,                  
            BRAND_NAME,                   
            new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()),
            BRAND_NAME
        );
    }

    
    private String wrapEmailWithColor(String preheader, String title, String contentHtml, String headerColor) {
        return """
        <!doctype html>
        <html>
        <head>
          <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <title>%s</title>
          <style>
            @media only screen and (max-width: 620px) {
              .container { width: 100%% !important; padding: 0 12px !important; }
              .content { padding: 16px !important; }
              h1 { font-size: 20px !important; }
            }
          </style>
        </head>
        <body style="margin:0; padding:0; background:#f4f6f8;">
          <span style="display:none; color:transparent; visibility:hidden; opacity:0; height:0; width:0; overflow:hidden;">
            %s
          </span>

          <table role="presentation" cellpadding="0" cellspacing="0" border="0" width="100%%" style="background:#f4f6f8;">
            <tr>
              <td align="center" style="padding:24px 12px;">
                <table role="presentation" cellpadding="0" cellspacing="0" border="0" width="600" class="container"
                       style="width:600px; max-width:100%%; background:#ffffff; border-radius:10px; box-shadow:0 1px 6px rgba(0,0,0,.06); overflow:hidden;">
                  <!-- Header -->
                  <tr>
                    <td align="left" style="background:%s; padding:16px 24px;">
                      <table width="100%%" cellpadding="0" cellspacing="0" role="presentation">
                        <tr>
                          <td align="left" style="color:#fff; font:700 16px/1.2 Arial,Helvetica,sans-serif;">%s</td>
                          <td align="right" style="color:#e5e7eb; font:400 12px/1.2 Arial,Helvetica,sans-serif;">Notificación automática</td>
                        </tr>
                      </table>
                    </td>
                  </tr>

                  <!-- Title + Body -->
                  <tr>
                    <td class="content" style="padding:24px;">
                      <h1 style="margin:0 0 12px; font:700 22px/1.3 Arial,Helvetica,sans-serif; color:#0f172a;">
                        %s
                      </h1>

                      %s

                      <p style="margin:24px 0 0; font:12px/1.6 Arial,Helvetica,sans-serif; color:#64748b;">
                        — Equipo %s<br>
                        <span style="color:#94a3b8;">Este mensaje fue generado automáticamente.</span>
                      </p>
                    </td>
                  </tr>

                </table>
                <div style="padding-top:12px; color:#94a3b8; font:12px Arial,Helvetica,sans-serif;">
                  © %s %s
                </div>
              </td>
            </tr>
          </table>
        </body>
        </html>
        """.formatted(
            escape(title),
            escape(preheader),
            headerColor,
            BRAND_NAME,
            escape(title),
            contentHtml,
            BRAND_NAME,
            new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()),
            BRAND_NAME
        );
    }

    
    private String escape(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }

   

    
    private String construirHtmlProyecto(Proyecto proyecto, String accion) {
        String nombreProyecto = val(proyecto.getNombre());

        String cuerpo = """
          <p style="margin:0 0 12px; font:14px/1.7 Arial,Helvetica,sans-serif; color:#334155;">
            ¡Buenas! Te contamos que en <b>%s</b> se registró un nuevo proyecto.
          </p>

          <table role="presentation" cellpadding="0" cellspacing="0" border="0" width="100%%"
                 style="border-collapse:collapse; font:14px Arial,Helvetica,sans-serif; color:#0f172a;">
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#f8fafc; width:40%%;"><b>Proyecto</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#f8fafc;"><b>Patrocinador</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#f8fafc;"><b>Líder usuario</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#f8fafc;"><b>Líder técnico</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#f8fafc;"><b>Estado inicial</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#f8fafc;"><b>Inicio planificado</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#f8fafc;"><b>Fin planificado</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
          </table>

          <p style="margin:16px 0 0; font:13px/1.7 Arial,Helvetica,sans-serif; color:#475569;">
            Si no esperabas este correo, puedes ignorarlo. Para dudas, responde a este mensaje.
          </p>
        """.formatted(
            BRAND_NAME,
            escape(nombreProyecto),
            escape(val(proyecto.getPatrocinadorNombre())),
            escape(val(proyecto.getLiderUsuarioNombre())),
            escape(val(proyecto.getLiderTecnicoNombre())),
            escape(val(proyecto.getEstado())),
            escape(formatearFecha(proyecto.getFechaInicioPlanificada())),
            escape(formatearFecha(proyecto.getFechaFinalPlanificada()))
        );

        String titulo = "Nuevo proyecto creado";
        String preheader = "Se registró el proyecto " + nombreProyecto + " en " + BRAND_NAME;
        return wrapEmail(preheader, titulo, cuerpo); // celeste por defecto
    }

    
    private String construirHtmlCambioEstado(Proyecto proyecto, String estadoAnterior) {
        String nombreProyecto = val(proyecto.getNombre());
        String nuevoEstado = val(proyecto.getEstado());
        String avance = (proyecto.getPorcentajeAvance() == null) ? "—" : (proyecto.getPorcentajeAvance() + "%");

        String cuerpo = """
          <p style="margin:0 0 12px; font:14px/1.7 Arial,Helvetica,sans-serif; color:#334155;">
            El proyecto <b>%s</b> cambió su estado.
          </p>

          <table role="presentation" cellpadding="0" cellspacing="0" border="0" width="100%%"
                 style="border-collapse:collapse; font:14px Arial,Helvetica,sans-serif; color:#0f172a;">
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#ecfdf5; width:40%%;"><b>Estado anterior</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#ecfdf5;"><b>Nuevo estado</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#ecfdf5;"><b>Avance</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#ecfdf5;"><b>Fecha del cambio</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
          </table>
        """.formatted(
            escape(nombreProyecto),
            escape(val(estadoAnterior)),
            escape(nuevoEstado),
            escape(avance),
            escape(formatearFecha(new Date()))
        );

        String titulo = "Cambio de estado del proyecto";
        String preheader = "El proyecto " + nombreProyecto + " cambió a " + nuevoEstado;
        return wrapEmailWithColor(preheader, titulo, cuerpo, COLOR_PROYECTO_CAMBIO_ESTADO);
    }

    
    private String construirHtmlActividad(Actividad actividad, String accion) {
        String proyectoNombre = val(actividad.getProyecto() != null ? actividad.getProyecto().getNombre() : null);

        String cuerpo = """
          <p style="margin:0 0 12px; font:14px/1.7 Arial,Helvetica,sans-serif; color:#334155;">
            Se agregó una nueva actividad al proyecto <b>%s</b>.
          </p>

          <table role="presentation" cellpadding="0" cellspacing="0" border="0" width="100%%"
                 style="border-collapse:collapse; font:14px Arial,Helvetica,sans-serif; color:#0f172a;">
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#fffbeb; width:40%%;"><b>Descripción</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#fffbeb;"><b>Encargado</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#fffbeb;"><b>Estado</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#fffbeb;"><b>Inicio planificado</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#fffbeb;"><b>Fin planificado</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#fffbeb;"><b>Orden de ejecución</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
          </table>
        """.formatted(
            escape(proyectoNombre),
            escape(val(actividad.getDescripcion())),
            escape(val(actividad.getEncargadoNombre())),
            escape(val(actividad.getEstado())),
            escape(formatearFecha(actividad.getFechaInicioPlanificada())),
            escape(formatearFecha(actividad.getFechaFinalPlanificada())),
            escape(val(actividad.getOrdenEjecucion()))
        );

        String titulo = "Nueva actividad registrada";
        String preheader = "Se registró una nueva actividad en " + proyectoNombre;
        return wrapEmailWithColor(preheader, titulo, cuerpo, COLOR_ACTIVIDAD_CREADA);
    }

   
    private String construirHtmlCambioEstadoActividad(Actividad actividad, String estadoAnterior) {
        String proyectoNombre = val(actividad.getProyecto() != null ? actividad.getProyecto().getNombre() : null);

        String cuerpo = """
          <p style="margin:0 0 12px; font:14px/1.7 Arial,Helvetica,sans-serif; color:#334155;">
            La actividad <b>%s</b> del proyecto <b>%s</b> cambió su estado.
          </p>

          <table role="presentation" cellpadding="0" cellspacing="0" border="0" width="100%%"
                 style="border-collapse:collapse; font:14px Arial,Helvetica,sans-serif; color:#0f172a;">
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#faf5ff; width:40%%;"><b>Estado anterior</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#faf5ff;"><b>Nuevo estado</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#faf5ff;"><b>Inicio real</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#faf5ff;"><b>Fin real</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#faf5ff;"><b>Orden de ejecución</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
          </table>
        """.formatted(
            escape(val(actividad.getDescripcion())),
            escape(proyectoNombre),
            escape(val(estadoAnterior)),
            escape(val(actividad.getEstado())),
            escape(formatearFecha(actividad.getFechaInicioReal())),
            escape(formatearFecha(actividad.getFechaFinalReal())),
            escape(val(actividad.getOrdenEjecucion()))
        );

        String titulo = "Estado de actividad actualizado";
        String preheader = "La actividad " + val(actividad.getDescripcion()) + " cambió su estado";
        return wrapEmailWithColor(preheader, titulo, cuerpo, COLOR_ACTIVIDAD_CAMBIO_ESTADO);
    }

    
    private String construirHtmlSeguimiento(SeguimientoProyecto seguimiento) {
        String proyectoNombre = val(seguimiento.getProyecto() != null ? seguimiento.getProyecto().getNombre() : null);
        String avance = (seguimiento.getPorcentajeAvance() == null) ? "—" : (seguimiento.getPorcentajeAvance() + "%");
        String registradoPor = (seguimiento.getCreadoPor() == null)
                ? "—"
                : (val(seguimiento.getCreadoPor().getNombre()) + " " + val(seguimiento.getCreadoPor().getApellidos()));

        String cuerpo = """
          <p style="margin:0 0 12px; font:14px/1.7 Arial,Helvetica,sans-serif; color:#334155;">
            Se agregó un nuevo seguimiento al proyecto <b>%s</b>:
          </p>

          <blockquote style="margin:0 0 16px; padding:12px 14px; border-left:3px solid #c7d2fe; background:#eef2ff; color:#1f2937; font:14px Arial,Helvetica,sans-serif;">
            “%s”
          </blockquote>

          <table role="presentation" cellpadding="0" cellspacing="0" border="0" width="100%%"
                 style="border-collapse:collapse; font:14px Arial,Helvetica,sans-serif; color:#0f172a;">
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#eef2ff; width:40%%;"><b>Avance</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#eef2ff;"><b>Fecha del seguimiento</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
            <tr>
              <td style="padding:10px; border:1px solid #e5e7eb; background:#eef2ff;"><b>Registrado por</b></td>
              <td style="padding:10px; border:1px solid #e5e7eb;">%s</td>
            </tr>
          </table>
        """.formatted(
            escape(proyectoNombre),
            escape(val(seguimiento.getObservaciones())),
            escape(avance),
            escape(formatearFecha(seguimiento.getFechaSeguimiento())),
            escape(registradoPor)
        );

        String titulo = "Nuevo seguimiento agregado";
        String preheader = "Nuevo seguimiento en " + proyectoNombre + " (" + avance + ")";
        return wrapEmailWithColor(preheader, titulo, cuerpo, COLOR_SEGUIMIENTO);
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