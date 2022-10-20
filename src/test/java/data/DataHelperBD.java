package data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;

public class DataHelperBD {
    @SneakyThrows
    public static String getValidCode(String login) {
        var codeSQL = "SELECT code FROM auth_codes WHERE (user_id = ALL(SELECT id FROM users WHERE login = ?)) ORDER BY created DESC LIMIT 1;";
        var runner = new QueryRunner();
        try (
                var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
        ) {
            var validCode = runner.query(conn, codeSQL, new ScalarHandler<>(), "vasy");
            return validCode.toString();
        }
    }

    @SneakyThrows
    public static void clearingTable(String tableName) {
        var runner = new QueryRunner();
        var codeSQL = "Delete from " + tableName + ";";
        try (
                var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
        ) {
            runner.update(conn, codeSQL);
        }
    }
}
