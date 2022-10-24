package data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.List;

public class DataHelper {
    private DataHelper() {
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

    public static String getCardFullNumberFirstUser(int indexOfCard) {
        List<String> numberCards = List.of("5559 0000 0000 0001", "5559 0000 0000 0002");
        return numberCards.get(indexOfCard);
    }

    public static String getCardFullNumberOtherUser(int indexOfCard) {
        List<String> numberCards = List.of("5559 0000 0000 0008");
        return numberCards.get(indexOfCard);
    }
}
