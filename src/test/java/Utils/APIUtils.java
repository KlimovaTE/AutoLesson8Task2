package Utils;

import data.DataHelper;
import data.DataHelperBD;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;

import static io.restassured.RestAssured.given;


@UtilityClass
public class APIUtils {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setBasePath("/api")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void shouldLogin(DataHelper.AuthInfo userData) {
        String requestBody = "{\n" +
                "  \"login\": \"" + userData.getLogin() + "\",\n" +
                "  \"password\": \"" + userData.getPassword() + "\"\n" +
                "}";
        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post("/auth")
                .then()
                .statusCode(200);
    }

    public static String shouldReturnToken(DataHelper.AuthInfo userData) {
        shouldLogin(userData);
        String code = DataHelperBD.getValidCode(userData);
        String requestBody2 = "{\n" +
                "  \"login\": \"" + userData.getLogin() + "\",\n" +
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
        return token;
    }

    public static void shouldTransfer(String cardFrom, String cardTo, Integer amount, String token) {
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
}
