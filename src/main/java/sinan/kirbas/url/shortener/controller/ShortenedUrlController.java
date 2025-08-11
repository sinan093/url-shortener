package sinan.kirbas.url.shortener.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import sinan.kirbas.url.shortener.model.ShortenedUrlDto;
import sinan.kirbas.url.shortener.service.ShortenedUrlService;
import sinan.kirbas.url.shortener.util.ShortenedUrlUtil;


@RestController
@RequiredArgsConstructor
public class ShortenedUrlController {

    private final ShortenedUrlService shortenedUrlService;
    private final ShortenedUrlUtil shortenedUrlUtil;

    @GetMapping(value = "/get/{id}")
    public RedirectView getUrlById(@PathVariable String id) {
        shortenedUrlUtil.checkUrlReachability(shortenedUrlService.getShortenedUrl(id));
        return new RedirectView(shortenedUrlService.getShortenedUrl(id));
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
