package sinan.kirbas.url.shortener.mapper;

import org.springframework.stereotype.Component;
import sinan.kirbas.url.shortener.entities.ShortenedUrl;
import sinan.kirbas.url.shortener.model.ShortenedUrlDto;

@Component
public class ShortenedUrlMapper {

    public ShortenedUrlDto toShortenedUrlDto(ShortenedUrl shortenedUrl) {
        ShortenedUrlDto shortenedUrlDto = new ShortenedUrlDto();
        shortenedUrlDto.setId(shortenedUrl.getId());
        shortenedUrlDto.setUrl(shortenedUrl.getUrl());
        shortenedUrlDto.setShortUrl(shortenedUrl.getShortUrl());
        shortenedUrlDto.setTtl(shortenedUrlDto.getTtl());
        return shortenedUrlDto;
    }

    public ShortenedUrl toShortenedUrl(ShortenedUrlDto shortenedUrlDto) {
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setId(shortenedUrlDto.getId());
        shortenedUrl.setUrl(shortenedUrlDto.getUrl());
        shortenedUrl.setShortUrl(shortenedUrlDto.getShortUrl());
        shortenedUrl.setTtl(shortenedUrlDto.getTtl());
        return shortenedUrl;
    }

}
