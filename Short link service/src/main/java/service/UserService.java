package service;

import model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserService {
    private final Map<UUID, User> users = new HashMap<>();

    public User createUser() {
        User user = new User();
        users.put(user.getUuid(), user);
        return user;
    }

    public User getUserByUUID(UUID uuid) {
        return users.get(uuid);
    }
}
