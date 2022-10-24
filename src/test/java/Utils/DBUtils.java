package Utils;

import lombok.*;
import lombok.experimental.UtilityClass;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;

@UtilityClass
public class DBUtils {

    @SneakyThrows
    public static Integer shouldGetBalanceCard(String cardNumber) {
        var codeSQL = "SELECT balance_in_kopecks FROM cards WHERE number = ?;";
        var runner = new QueryRunner();
        try (
                var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
        ) {
            var balance = runner.query(conn, codeSQL, new ScalarHandler<>(), cardNumber);
            return Integer.parseInt(balance.toString());
        }
    }

    @SneakyThrows
    public static void shouldSetBalance(String cardNumber, Integer cardBalance) {
        var runner = new QueryRunner();
        var codeSQL = "UPDATE cards SET balance_in_kopecks=? WHERE number =?;";
        try (
                var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
        ) {
            runner.update(conn, codeSQL, cardBalance, cardNumber);
        }
    }
}
