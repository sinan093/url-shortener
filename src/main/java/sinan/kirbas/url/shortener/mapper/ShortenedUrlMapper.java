package sinan.kirbas.url.shortener.mapper;

import org.springframework.stereotype.Component;
import sinan.kirbas.url.shortener.entities.ShortenedUrl;
import sinan.kirbas.url.shortener.model.ShortenedUrlDto;

@Component
public class ShortenedUrlMapper {

    public ShortenedUrlDto toShortenedUrlDto(ShortenedUrl shortenedUrl) {
        ShortenedUrlDto shortenedUrlDto = new ShortenedUrlDto();
        shortenedUrlDto.setUrl(shortenedUrl.getUrl());
        shortenedUrlDto.setShortUrl(shortenedUrl.getShortUrl());
        shortenedUrlDto.setTtl(shortenedUrlDto.getTtl());
        shortenedUrlDto.setId(shortenedUrl.getId());
        return shortenedUrlDto;
    }

}
