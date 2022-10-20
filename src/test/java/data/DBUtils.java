package data;

import com.codeborne.selenide.conditions.Not;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;

import static io.restassured.RestAssured.given;

public class DBUtils {
    String token1;
    private RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setBasePath("/api")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public void shouldLogin(String login) {
        String requestBody = "{\n" +
                "  \"login\": \"" + login + "\",\n" +
                "  \"password\": \"qwerty123\"\n" +
                "}";
        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post("/auth")
                .then()
                .statusCode(200);
    }

    public String shouldReturnToken(String login) {
        if (token1 == null) {
            shouldLogin(login);
            String code = DataHelperBD.getValidCode(login);
            String requestBody2 = "{\n" +
                    "  \"login\": \"" + login + "\",\n" +
                    "  \"code\": \"" + code + "\"\n" +
                    "}";
            String token =
                    given()
                            .spec(requestSpec)
                            .body(requestBody2)
                            .when()
                            .post("/auth/verification")
                            .then()
                            .statusCode(200)
                            .extract()
                            .path("token");
            token1 = token;
            return token;
        } else {
            return token1;
        }
    }

    @SneakyThrows
    public Integer shouldGetBalanceCard(String cardNumber) {
        var codeSQL = "SELECT balance_in_kopecks FROM cards WHERE number = ?;";
        var runner = new QueryRunner();
        try (
                var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
        ) {
            var balance = runner.query(conn, codeSQL, new ScalarHandler<>(), cardNumber);
            return Integer.parseInt(balance.toString());
        }
    }

    public void shouldTransfer(String cardFrom, String cardTo, Integer amount, String token) {
        String requestBody3 = "{\n" +
                "  \"from\": \"" + cardFrom + "\",\n" +
                "  \"to\": \"" + cardTo + "\",\n" +
                "  \"amount\": " + amount + "\n" +
                "}";
        given()
                .spec(requestSpec)
                .auth().oauth2(token)
                .body(requestBody3)
                .when()
                .post("/transfer")
                .then()
                .statusCode(200);
    }

    @SneakyThrows
    public void shouldSetBalance() {
        var runner = new QueryRunner();
        var codeSQL = "UPDATE cards SET balance_in_kopecks=?;";
        try (
                var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
        ) {
            runner.update(conn, codeSQL, 1_000_000);
        }
    }
}
