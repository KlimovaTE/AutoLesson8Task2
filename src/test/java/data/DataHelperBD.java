package data;

import lombok.SneakyThrows;

import java.sql.DriverManager;

public class DataHelperBD {
    @SneakyThrows
    public static String getValidCode(String login) {
        var codeSQL = "SELECT code FROM auth_codes WHERE (user_id = ALL(SELECT id FROM users WHERE login = ?)) ORDER BY created DESC LIMIT 1;";
        String codeString = "";
        try (
                var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
                var codeStmt = conn.prepareStatement(codeSQL);
        ) {
            codeStmt.setString(1, "vasya");
            try (var rs = codeStmt.executeQuery()) {
                if (rs.next()) {
                    int codeInt = rs.getInt(1);
                    codeString = Integer.toString(codeInt);
                }
            }
        }
        return codeString;
    }

    @SneakyThrows
    public static void clearingTable(String tableName) {
        var codeSQL = "Delete from " + tableName + ";";
        try (
                var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
                var codeStmt = conn.prepareStatement(codeSQL);
        ) {
            codeStmt.execute();
        }
    }
}
