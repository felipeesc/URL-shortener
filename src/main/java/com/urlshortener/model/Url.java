package com.urlshortener.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;

@Table("url")
public class Url {

    @PrimaryKey
    private String shortCode;

    private String longUrl;

    private Instant createdAt;

    public Url() {}

    public Url(String shortCode, String longUrl, Instant createdAt) {
        this.shortCode = shortCode;
        this.longUrl = longUrl;
        this.createdAt = createdAt;
    }

    public String getShortCode() { return shortCode; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }

    public String getLongUrl() { return longUrl; }
    public void setLongUrl(String longUrl) { this.longUrl = longUrl; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String shortCode;
        private String longUrl;
        private Instant createdAt;

        public Builder shortCode(String shortCode) { this.shortCode = shortCode; return this; }
        public Builder longUrl(String longUrl) { this.longUrl = longUrl; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public Url build() { return new Url(shortCode, longUrl, createdAt); }
    }
}
