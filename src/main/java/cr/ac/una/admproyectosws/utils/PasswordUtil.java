package cr.ac.una.admproyectosws.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Esto valida si el texto cumple el formato de hash BCrypt
    private static final String BCRYPT_REGEX = "^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$";

     // Esto sirve para generar un hash BCrypt seguro
    public static String hash(String plain) {
        if (plain == null) return null;
        return BCrypt.hashpw(plain, BCrypt.gensalt());
    }

    // Esto dice si un valor parece un hash BCrypt
    public static boolean isBcryptHash(String value) {
        return value != null && value.matches(BCRYPT_REGEX);
    }

    // Esto compara una contraseña con lo almacenado
    public static boolean checkFlexible(String plain, String stored) {
        if (plain == null || stored == null) return false;
        if (isBcryptHash(stored)) {
            return BCrypt.checkpw(plain, stored);
        } else {
            
            return plain.equals(stored);
        }
    }
}
