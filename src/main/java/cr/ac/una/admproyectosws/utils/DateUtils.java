package cr.ac.una.admproyectosws.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {
    
    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    private static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    
    private DateUtils() {
        // Utility class
    }
    
    public static String formatDate(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(date);
    }
    
    public static String formatDateTime(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(DATETIME_FORMAT).format(date);
    }
    
    public static Date parseDate(String dateString) throws ParseException {
        if (dateString == null || dateString.trim().isEmpty()) return null;
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(dateString);
    }
    
    public static boolean isDateBetween(Date date, Date start, Date end) {
        if (date == null || start == null || end == null) return false;
        return !date.before(start) && !date.after(end);
    }
    
    public static Date addDays(Date date, int days) {
        if (date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
    
    public static int daysBetween(Date start, Date end) {
        if (start == null || end == null) return 0;
        long diffInMillies = Math.abs(end.getTime() - start.getTime());
        return (int) (diffInMillies / (24 * 60 * 60 * 1000));
    }
    
    public static boolean isOverdue(Date plannedEndDate) {
        if (plannedEndDate == null) return false;
        return plannedEndDate.before(new Date());
    }
}
    