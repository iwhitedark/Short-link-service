package service;

import model.ShortLink;

import java.util.*;

public class LinkService {
    private final Map<UUID, List<ShortLink>> userLinks = new HashMap<>();
    private final String baseShortUrl = "https://short.url/";

    public ShortLink createShortLink(UUID userId, String originalUrl, int clickLimit, int daysToExpire) {
        String uniqueKey = UUID.randomUUID().toString().substring(0, 6);
        String shortUrl = baseShortUrl + uniqueKey;
        ShortLink newLink = new ShortLink(originalUrl, shortUrl, clickLimit, daysToExpire);

        userLinks.computeIfAbsent(userId, k -> new ArrayList<>()).add(newLink);
        return newLink;
    }

    public ShortLink getLinkByShortUrl(String shortUrl) {
        return userLinks.values()
                .stream()
                .flatMap(List::stream)
                .filter(link -> link.getShortUrl().equals(shortUrl))
                .findFirst()
                .orElse(null);
    }

    public void deleteExpiredLinks() {
        userLinks.values().forEach(links -> links.removeIf(ShortLink::isExpired));
    }

    public List<ShortLink> getUserLinks(UUID userId) {
        return userLinks.getOrDefault(userId, new ArrayList<>());
    }
}
