package cr.ac.una.admproyectosws.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {
    
    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";  // Formato por defecto para fechas
    private static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss"; // Formato para fecha y hora.
    
    private DateUtils() {
        
    }
    // Esto sirve para formatear una Date como dd/MM/yyyy
    public static String formatDate(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(date);
    }
    // Esto sirve para formatear una Date con hora como dd/MM/yyyy HH:mm:ss
    public static String formatDateTime(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(DATETIME_FORMAT).format(date);
    }
    
    // Esto convierte texto dd/MM/yyyy en Date
    public static Date parseDate(String dateString) throws ParseException {
        if (dateString == null || dateString.trim().isEmpty()) return null;
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(dateString);
    }
     // Esto verifica si una fecha está entre start y end 
    public static boolean isDateBetween(Date date, Date start, Date end) {
        if (date == null || start == null || end == null) return false;
        return !date.before(start) && !date.after(end);
    }
    
    // Esto suma o resta días a una fecha y devuelve la nueva
    public static Date addDays(Date date, int days) {
        if (date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
    
    // Esto calcula la diferencia aproximada en días entre dos fechas
    public static int daysBetween(Date start, Date end) {
        if (start == null || end == null) return 0;
        long diffInMillies = Math.abs(end.getTime() - start.getTime());
        return (int) (diffInMillies / (24 * 60 * 60 * 1000));
    }
    
    // Esto indica si una fecha planificada
    public static boolean isOverdue(Date plannedEndDate) {
        if (plannedEndDate == null) return false;
        return plannedEndDate.before(new Date());
    }
}
    