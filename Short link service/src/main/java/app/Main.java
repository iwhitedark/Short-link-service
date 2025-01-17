package app;

import model.ShortLink;
import model.User;
import service.LinkService;
import service.UserService;

import java.awt.Desktop;
import java.net.URI;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws Exception {
        UserService userService = new UserService();
        LinkService linkService = new LinkService();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Добро пожаловать в сервис сокращения ссылок!");
        System.out.println("Создаем вашего уникального пользователя...");
        User user = userService.createUser();
        UUID userId = user.getUuid();
        System.out.println("Ваш уникальный идентификатор пользователя (UUID): " + userId);

        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Создать короткую ссылку");
            System.out.println("2. Показать мои ссылки");
            System.out.println("3. Перейти по короткой ссылке");
            System.out.println("4. Удалить просроченные ссылки");
            System.out.println("0. Выйти");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("Введите оригинальную ссылку:");
                    String originalUrl = scanner.nextLine();
                    System.out.println("Введите лимит переходов:");
                    int clickLimit = scanner.nextInt();
                    System.out.println("Введите срок жизни ссылки (в днях):");
                    int daysToExpire = scanner.nextInt();

                    ShortLink shortLink = linkService.createShortLink(userId, originalUrl, clickLimit, daysToExpire);
                    System.out.println("Ваша короткая ссылка: " + shortLink.getShortUrl());
                }
                case 2 -> {
                    System.out.println("Ваши ссылки:");
                    for (ShortLink link : linkService.getUserLinks(userId)) {
                        System.out.println(link.getShortUrl() + " -> " + link.getOriginalUrl() +
                                " | Переходов: " + link.getClicks() +
                                " | Срок действия: " + link.getExpirationTime());
                    }
                }
                case 3 -> {
                    System.out.println("Введите короткую ссылку:");
                    String shortUrl = scanner.nextLine();
                    ShortLink link = linkService.getLinkByShortUrl(shortUrl);

                    if (link != null && !link.isExpired()) {
                        link.registerClick();
                        System.out.println("Открытие ссылки в браузере...");
                        Desktop.getDesktop().browse(new URI(link.getOriginalUrl()));
                    } else {
                        System.out.println("Ссылка недействительна (лимит исчерпан или срок действия истек).");
                    }
                }
                case 4 -> {
                    System.out.println("Удаление просроченных ссылок...");
                    linkService.deleteExpiredLinks();
                    System.out.println("Просроченные ссылки удалены.");
                }
                case 0 -> {
                    System.out.println("Спасибо за использование сервиса! До свидания.");
                    System.exit(0);
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }
}
