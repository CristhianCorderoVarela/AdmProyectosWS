package cr.ac.una.admproyectosws.utils;

public final class Constants {
    
    // Estados de Proyecto
    public static final String PROYECTO_PLANIFICADO = "PLANIFICADO";
    public static final String PROYECTO_EN_CURSO = "EN_CURSO";
    public static final String PROYECTO_SUSPENDIDO = "SUSPENDIDO";
    public static final String PROYECTO_FINALIZADO = "FINALIZADO";
    
    // Estados de Actividad
    public static final String ACTIVIDAD_PLANIFICADA = "PLANIFICADA";
    public static final String ACTIVIDAD_EN_CURSO = "EN_CURSO";
    public static final String ACTIVIDAD_POSTERGADA = "POSTERGADA";
    public static final String ACTIVIDAD_FINALIZADA = "FINALIZADA";
    
    // Estados de Administrador
    public static final String ADMINISTRADOR_ACTIVO = "ACTIVO";
    public static final String ADMINISTRADOR_INACTIVO = "INACTIVO";
    
    // Configuración de Email
    public static final String EMAIL_FROM = "sistema@una.ac.cr";
    public static final String EMAIL_SUBJECT_PREFIX = "[Sistema Proyectos UNA] ";
    
    // Configuración de Excel
    public static final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String EXCEL_FILE_EXTENSION = ".xlsx";
    
    // Validaciones
    public static final int MAX_NOMBRE_LENGTH = 200;
    public static final int MAX_DESCRIPCION_LENGTH = 500;
    public static final int MAX_CORREO_LENGTH = 120;
    public static final int MIN_PASSWORD_LENGTH = 3;
    
    private Constants() {
        
    }
}