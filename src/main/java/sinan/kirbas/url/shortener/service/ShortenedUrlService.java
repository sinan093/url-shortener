package sinan.kirbas.url.shortener.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sinan.kirbas.url.shortener.entities.ShortenedUrl;
import sinan.kirbas.url.shortener.enums.WarningMessages;
import sinan.kirbas.url.shortener.mapper.ShortenedUrlMapper;
import sinan.kirbas.url.shortener.model.ShortenedUrlDto;
import sinan.kirbas.url.shortener.repository.ShortenedUrlRepository;

import java.net.URI;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
@Service
public class ShortenedUrlService {

    private final ShortenedUrlRepository shortenedUrlRepository;
    private final ShortenedUrlMapper shortenedUrlMapper;

    @Value("${custom.check.expired.on-creation}")
    private boolean checkExpiredOnCreation;

    public String getShortenedUrl(String id) {
        ShortenedUrl shortenedUrl =
                shortenedUrlRepository.findById(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.NOT_FOUND, WarningMessages.NOT_FOUND.message));
        if(isExpired(shortenedUrl.getTtl())) {
            shortenedUrlRepository.deleteById(id);
            log.warn(WarningMessages.EXPIRED.message);
            throw new ResponseStatusException(HttpStatus.GONE, WarningMessages.EXPIRED.message);
        }
        ShortenedUrlDto shortenedUrlDto = shortenedUrlMapper.toShortenedUrlDto(shortenedUrl);
        return shortenedUrlDto.getUrl();
    }

    public void createShortenedUrl(ShortenedUrlDto shortenedUrlDto) {
        if(!isUrlValid(shortenedUrlDto.getUrl())) {
            log.warn(WarningMessages.MALFORMED_URL.message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WarningMessages.MALFORMED_URL.message);
        }
        if(shortenedUrlDto.getId() != null) {
            if(shortenedUrlRepository.findById(shortenedUrlDto.getId()).isPresent()) {
                log.warn(WarningMessages.ID_TAKEN.message);
                throw new ResponseStatusException(HttpStatus.CONFLICT, WarningMessages.ID_TAKEN.message);
            }
            ShortenedUrl shortenedUrl = new ShortenedUrl();
            shortenedUrl.setUrl(shortenedUrlDto.getUrl());
            shortenedUrl.setShortUrl(shortenedUrlDto.getId());
            shortenedUrl.setId(shortenedUrlDto.getId());
            if(checkExpiredOnCreation && isExpired(shortenedUrlDto.getTtl())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WarningMessages.TTL_FUTURE.message);
            }
            shortenedUrl.setTtl(shortenedUrlDto.getTtl());
            shortenedUrlRepository.save(shortenedUrl);
            log.trace("Shortened Url created with id: {}", shortenedUrl.getId());
        } else {
            String shortenedString = createShortenedString();
            ShortenedUrl shortenedUrl = new ShortenedUrl();
            shortenedUrl.setUrl(shortenedUrlDto.getUrl());
            shortenedUrl.setShortUrl(shortenedString);
            shortenedUrl.setId(shortenedString);
            if(checkExpiredOnCreation && isExpired(shortenedUrlDto.getTtl())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WarningMessages.TTL_FUTURE.message);
            }
            shortenedUrl.setTtl(shortenedUrlDto.getTtl());
            shortenedUrlRepository.save(shortenedUrl);
            log.trace("Shortened Url created with id: {}", shortenedString);
        }
    }

    public void deleteShortenedUrlById(String id) {
        shortenedUrlRepository.deleteById(id);
    }


    private String createShortenedString() {
        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom secureRandom = new SecureRandom();
        String shortenedString = "";
        for(int i = 0; i<=5; i++) {
            int randomNumber = secureRandom.nextInt(62);
            shortenedString = shortenedString + validChars.charAt(randomNumber);
        }
        return shortenedString;
    }

    private boolean isUrlValid(String url) {
        try {
            new URI(url).toURL();
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    private boolean isExpired(Date ttl) {
        if(ttl != null &&
                LocalDateTime.now().isAfter(LocalDateTime.ofInstant(ttl.toInstant(), ZoneId.systemDefault()))) {
            return true;
        }
        return false;
    }

}
