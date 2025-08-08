package sinan.kirbas.url.shortener.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sinan.kirbas.url.shortener.model.ShortenedUrlDto;
import sinan.kirbas.url.shortener.service.ShortenedUrlService;

import java.net.URI;


@RestController
@RequiredArgsConstructor
public class ShortenedUrlController {

    private final ShortenedUrlService shortenedUrlService;

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Void> getUrlById(@PathVariable String id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(shortenedUrlService.getShortenedUrl(id)));
        return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
    }

    @PostMapping("/create")
    public void createShortenedUrl(@RequestBody ShortenedUrlDto shortenedUrlDto) {
        shortenedUrlService.createShortenedUrl(shortenedUrlDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable String id) {
        shortenedUrlService.deleteShortenedUrlById(id);
    }

}
