package com.urlshortener.service;

import com.urlshortener.dto.ShortenRequest;
import com.urlshortener.dto.ShortenResponse;
import com.urlshortener.exception.UrlNotFoundException;
import com.urlshortener.model.Url;
import com.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final ShortCodeGenerator shortCodeGenerator;

    public UrlService(UrlRepository urlRepository, ShortCodeGenerator shortCodeGenerator) {
        this.urlRepository = urlRepository;
        this.shortCodeGenerator = shortCodeGenerator;
    }

    @Value("${app.base-url}")
    private String baseUrl;

    public ShortenResponse shorten(ShortenRequest request) {
        String shortCode = shortCodeGenerator.generate(request.getUrl());

        // Garante unicidade no Cassandra antes de salvar
        while (urlRepository.existsById(shortCode)) {
            shortCode = shortCodeGenerator.generate(request.getUrl() + shortCode);
        }

        Url url = Url.builder()
                .shortCode(shortCode)
                .longUrl(request.getUrl())
                .createdAt(Instant.now())
                .build();

        urlRepository.save(url);

        return new ShortenResponse(
                baseUrl + "/" + shortCode,
                shortCode,
                request.getUrl()
        );
    }

    public String resolve(String shortCode) {
        return urlRepository.findById(shortCode)
                .map(Url::getLongUrl)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));
    }
}
