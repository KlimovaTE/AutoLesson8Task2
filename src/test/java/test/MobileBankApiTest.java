package test;

import data.DBUtils;
import data.DataHelperBD;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


class  MobileBankApiTest {

    DBUtils dBUtils = new DBUtils();

    @AfterAll
    public static void setClose() {
        DataHelperBD.clearingTable("auth_codes");
        DataHelperBD.clearingTable("card_transactions");
        DataHelperBD.clearingTable("cards");
        DataHelperBD.clearingTable("users");
    }

    @Test
    public void shouldInternalTransferToFirstCard() {
        DBUtils dBUtils = new DBUtils();
        String cardFrom = "5559 0000 0000 0002";
        String cardTo = "5559 0000 0000 0001";
        Integer amount = 100;
        int cardFromBalanceBeforeTransfer = dBUtils.shouldGetBalanceCard(cardFrom);
        int cardToBalanceBeforeTransfer = dBUtils.shouldGetBalanceCard(cardTo);
        dBUtils.shouldTransfer(cardFrom, cardTo, amount, dBUtils.shouldReturnToken("vasya"));
        int cardFromBalanceAfterTransfer = dBUtils.shouldGetBalanceCard(cardFrom);
        int cardToBalanceAfterTransfer = dBUtils.shouldGetBalanceCard(cardTo);
        assertEquals(cardFromBalanceBeforeTransfer - amount * 100, cardFromBalanceAfterTransfer);
        assertEquals(cardToBalanceBeforeTransfer + amount * 100, cardToBalanceAfterTransfer);
        dBUtils.shouldSetBalance();
    }

    @Test
    public void shouldInternalTransferToSecondCard() {
        DBUtils dBUtils = new DBUtils();
        String cardFrom = "5559 0000 0000 0001";
        String cardTo = "5559 0000 0000 0002";
        Integer amount = 500;
        int cardFromBalanceBeforeTransfer = dBUtils.shouldGetBalanceCard(cardFrom);
        int cardToBalanceBeforeTransfer = dBUtils.shouldGetBalanceCard(cardTo);
        dBUtils.shouldTransfer(cardFrom, cardTo, amount, dBUtils.shouldReturnToken("vasya"));
        int cardFromBalanceAfterTransfer = dBUtils.shouldGetBalanceCard(cardFrom);
        int cardToBalanceAfterTransfer = dBUtils.shouldGetBalanceCard(cardTo);
        assertEquals(cardFromBalanceBeforeTransfer - amount * 100, cardFromBalanceAfterTransfer);
        assertEquals(cardToBalanceBeforeTransfer + amount * 100, cardToBalanceAfterTransfer);
        dBUtils.shouldSetBalance();
    }

    @Test
    public void shouldTransferToVasyaFirstCard() {
        DBUtils dBUtils = new DBUtils();
        String cardFrom = "5559 0000 0000 0008";
        String cardTo = "5559 0000 0000 0001";
        Integer amount = 5000;
        int cardToBalanceBeforeTransfer = dBUtils.shouldGetBalanceCard(cardTo);
        dBUtils.shouldTransfer(cardFrom, cardTo, amount, dBUtils.shouldReturnToken("vasya"));
        int cardToBalanceAfterTransfer = dBUtils.shouldGetBalanceCard(cardTo);
        assertEquals(cardToBalanceBeforeTransfer + amount * 100, cardToBalanceAfterTransfer);
        dBUtils.shouldSetBalance();
    }

    @Test
    public void shouldTransferToVasyaSecondCard() {
        DBUtils dBUtils = new DBUtils();
        String cardFrom = "5559 0000 0000 0008";
        String cardTo = "5559 0000 0000 0002";
        Integer amount = 5000;
        int cardToBalanceBeforeTransfer = dBUtils.shouldGetBalanceCard(cardTo);
        dBUtils.shouldTransfer(cardFrom, cardTo, amount, dBUtils.shouldReturnToken("vasya"));
        int cardToBalanceAfterTransfer = dBUtils.shouldGetBalanceCard(cardTo);
        assertEquals(cardToBalanceBeforeTransfer + amount * 100, cardToBalanceAfterTransfer);
        dBUtils.shouldSetBalance();
    }

    @Test
    public void shouldNotTransferMinusAmount() {
        DBUtils dBUtils = new DBUtils();
        String cardFrom = "5559 0000 0000 0008";
        String cardTo = "5559 0000 0000 0002";
        Integer amount = -5000;
        int cardToBalanceBeforeTransfer = dBUtils.shouldGetBalanceCard(cardTo);
        dBUtils.shouldTransfer(cardFrom, cardTo, amount, dBUtils.shouldReturnToken("vasya"));
        int cardToBalanceAfterTransfer = dBUtils.shouldGetBalanceCard(cardTo);
        assertEquals(cardToBalanceBeforeTransfer, cardToBalanceAfterTransfer);
        dBUtils.shouldSetBalance();
    }

    @Test
    public void shouldNotTransferAmountOverBalance() {
        DBUtils dBUtils = new DBUtils();
        String cardFrom = "5559 0000 0000 0001";
        String cardTo = "5559 0000 0000 0002";
        Integer amount = 2_000_000;
        int cardToBalanceBeforeTransfer = dBUtils.shouldGetBalanceCard(cardTo);
        dBUtils.shouldTransfer(cardFrom, cardTo, amount, dBUtils.shouldReturnToken("vasya"));
        int cardToBalanceAfterTransfer = dBUtils.shouldGetBalanceCard(cardTo);
        assertEquals(cardToBalanceBeforeTransfer, cardToBalanceAfterTransfer);
        dBUtils.shouldSetBalance();
    }
}

