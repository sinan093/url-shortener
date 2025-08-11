package sinan.kirbas.url.shortener.enums;

public enum WarningMessages {
    ID_TAKEN("ID already taken"),
    MALFORMED_URL("Malformed URL"),
    NOT_FOUND("Shortened URL not found"),
    EXPIRED("Shortened URL expired"),
    TTL_FUTURE("TTL should be in the future"),
    SITE_NOT_FOUND("The desired site doesn't exist or isn't reachable");

    public final String message;

     WarningMessages(String message) {
        this.message = message;
    }
}
