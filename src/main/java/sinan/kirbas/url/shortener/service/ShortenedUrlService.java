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
import sinan.kirbas.url.shortener.util.ShortenedUrlUtil;

@RequiredArgsConstructor
@Slf4j
@Service
public class ShortenedUrlService {

    private final ShortenedUrlRepository shortenedUrlRepository;
    private final ShortenedUrlMapper shortenedUrlMapper;
    private final ShortenedUrlUtil shortenedUrlUtil;

    @Value("${custom.check.expired.on-creation}")
    private boolean checkExpiredOnCreation;

    public String getShortenedUrl(String id) {
        ShortenedUrl shortenedUrl =
                shortenedUrlRepository.findById(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.NOT_FOUND, WarningMessages.NOT_FOUND.message));
        if(shortenedUrlUtil.isExpired(shortenedUrl.getTtl())) {
            shortenedUrlRepository.deleteById(id);
            log.warn(WarningMessages.EXPIRED.message);
            throw new ResponseStatusException(HttpStatus.GONE, WarningMessages.EXPIRED.message);
        }
        ShortenedUrlDto shortenedUrlDto = shortenedUrlMapper.toShortenedUrlDto(shortenedUrl);
        return shortenedUrlDto.getUrl();
    }

    public void createShortenedUrl(ShortenedUrlDto shortenedUrlDto) {
        if(!shortenedUrlUtil.isUrlValid(shortenedUrlDto.getUrl())) {
            log.warn(WarningMessages.MALFORMED_URL.message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WarningMessages.MALFORMED_URL.message);
        }
        if(shortenedUrlDto.getId() != null) {
            if(shortenedUrlRepository.findById(shortenedUrlDto.getId()).isPresent()) {
                log.warn(WarningMessages.ID_TAKEN.message);
                throw new ResponseStatusException(HttpStatus.CONFLICT, WarningMessages.ID_TAKEN.message);
            }
            ShortenedUrl shortenedUrl = shortenedUrlMapper.toShortenedUrl(shortenedUrlDto);
            shortenedUrl.setShortUrl(shortenedUrlDto.getId());
            if(checkExpiredOnCreation && shortenedUrlUtil.isExpired(shortenedUrlDto.getTtl())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WarningMessages.TTL_FUTURE.message);
            }
            shortenedUrlRepository.save(shortenedUrl);
            log.trace("Shortened Url created with id: {}", shortenedUrl.getId());
        } else {
            String shortenedString = shortenedUrlUtil.createShortenedString();
            ShortenedUrl shortenedUrl = shortenedUrlMapper.toShortenedUrl(shortenedUrlDto);
            shortenedUrl.setShortUrl(shortenedString);
            shortenedUrl.setId(shortenedString);
            if(checkExpiredOnCreation && shortenedUrlUtil.isExpired(shortenedUrlDto.getTtl())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WarningMessages.TTL_FUTURE.message);
            }
            shortenedUrlRepository.save(shortenedUrl);
            log.trace("Shortened Url created with id: {}", shortenedString);
        }
    }

    public void deleteShortenedUrlById(String id) {
        shortenedUrlRepository.deleteById(id);
    }

}
