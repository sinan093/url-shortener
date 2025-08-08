package sinan.kirbas.url.shortener.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "shortened_url", schema = "url")
public class ShortenedUrl {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "url")
    private String url;

    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "ttl")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ttl;
}
