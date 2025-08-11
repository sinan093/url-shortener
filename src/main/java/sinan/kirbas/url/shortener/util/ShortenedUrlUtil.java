package sinan.kirbas.url.shortener.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import sinan.kirbas.url.shortener.enums.WarningMessages;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
public class ShortenedUrlUtil {

    public String createShortenedString() {
        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom secureRandom = new SecureRandom();
        String shortenedString = "";
        for(int i = 0; i<=5; i++) {
            int randomNumber = secureRandom.nextInt(62);
            shortenedString = shortenedString + validChars.charAt(randomNumber);
        }
        return shortenedString;
    }

    public boolean isUrlValid(String url) {
        try {
            new URI(url).toURL();
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    public boolean isExpired(Date ttl) {
        if(ttl != null &&
                LocalDateTime.now().isAfter(LocalDateTime.ofInstant(ttl.toInstant(), ZoneId.systemDefault()))) {
            return true;
        }
        return false;
    }

    public void checkUrlReachability(String urlToReach) {
        try {
            URL url = new URI(urlToReach).toURL();
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            int responseCode = httpURLConnection.getResponseCode();
            log.debug("Response Code for {} is {}", urlToReach, responseCode);
        } catch (UnknownHostException unknownHostException) {
            log.warn(WarningMessages.SITE_NOT_FOUND.message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, WarningMessages.SITE_NOT_FOUND.message);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
