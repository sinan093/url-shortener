package sinan.kirbas.url.shortener.model;

import lombok.Data;

import java.util.Date;

@Data
public class ShortenedUrlDto {

    private String id;

    private String url;

    private String shortUrl;

    private Date ttl;
}
