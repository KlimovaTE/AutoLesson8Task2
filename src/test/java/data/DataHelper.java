package data;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

public class DataHelper {
    private DataHelper() {
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class User {
        private int id;
        private String login;
        private String password;
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    private static final Faker faker = new Faker();

    public static AuthInfo getFirstAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getOtherAuthInfo(AuthInfo original) {
        return new AuthInfo("petya", "123qwerty");
    }

    public static AuthInfo getInvalidPassFirstAuthInfo() {
        return new AuthInfo("vasya", faker.internet().password());
    }
}
