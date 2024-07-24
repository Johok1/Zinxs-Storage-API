package zinxs.wiki.utils;

public class FilenameValidator {

    public static final String REGEX_PATTERN = "^[A-Za-z0-9.]{1,255}$";

    public static boolean validateStringFilenameUsingRegex(String filename) {
        if (filename == null) {
            return false;
        }
        return filename.matches(REGEX_PATTERN);
    }

    public static String sanitizeFilename(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "default_filename.txt";
        }
        return filename.replaceAll("[\\\\/:*?\"<>|]", "_");
    }


}
