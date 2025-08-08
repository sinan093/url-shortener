package sinan.kirbas.url.shortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sinan.kirbas.url.shortener.entities.ShortenedUrl;

@Repository
public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, String> {

}
