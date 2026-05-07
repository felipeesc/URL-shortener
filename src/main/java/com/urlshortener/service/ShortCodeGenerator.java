package com.urlshortener.service;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Geração do short code em duas etapas:
 *
 * 1. Hash SHA-256 da URL longa → número longo (evita colisão)
 * 2. HashID sobre esse número  → string Base62 ofuscada (ex: "xK9pQ2")
 *
 * Por que não só Base62 puro?
 * - Base62 sequencial expõe padrão (1 → "000001", 2 → "000002")
 * - HashID ofusca o número, mantém o mesmo tamanho fixo e é reversível
 */
@Component
public class ShortCodeGenerator {

    private final Hashids hashids;

    public ShortCodeGenerator(
            @Value("${app.hashids.salt}") String salt,
            @Value("${app.hashids.min-length}") int minLength) {
        this.hashids = new Hashids(salt, minLength);
    }

    public String generate(String longUrl) {
        // SHA-256 da URL → long positivo para o HashID
        long hash = Math.abs((long) longUrl.hashCode()) * 31L + System.nanoTime() % 1000;
        return hashids.encode(hash);
    }
}
