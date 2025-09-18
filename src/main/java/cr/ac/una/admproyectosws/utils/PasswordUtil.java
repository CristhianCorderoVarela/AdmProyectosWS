package cr.ac.una.admproyectosws.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private static final String BCRYPT_REGEX = "^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$";

    public static String hash(String plain) {
        if (plain == null) return null;
        return BCrypt.hashpw(plain, BCrypt.gensalt());
    }

    public static boolean isBcryptHash(String value) {
        return value != null && value.matches(BCRYPT_REGEX);
    }

    /** Compara soportando ambas modalidades: hash BCrypt o texto plano legacy. */
    public static boolean checkFlexible(String plain, String stored) {
        if (plain == null || stored == null) return false;
        if (isBcryptHash(stored)) {
            return BCrypt.checkpw(plain, stored);
        } else {
            // modo legacy: en BD está en claro
            return plain.equals(stored);
        }
    }
}
