package model;

import java.time.LocalDateTime;

public class ShortLink {
    private String originalUrl;
    private String shortUrl;
    private int clickLimit;
    private int clicks;
    private LocalDateTime creationTime;
    private LocalDateTime expirationTime;

    public ShortLink(String originalUrl, String shortUrl, int clickLimit, int daysToExpire) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.clickLimit = clickLimit;
        this.clicks = 0;
        this.creationTime = LocalDateTime.now();
        this.expirationTime = creationTime.plusDays(daysToExpire);
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime) || clicks >= clickLimit;
    }

    public void registerClick() {
        if (!isExpired()) {
            clicks++;
        }
    }

    public int getClicks() {
        return clicks;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }
}
