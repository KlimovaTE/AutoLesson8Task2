package test;

import Utils.APIUtils;
import Utils.DBUtils;
import data.DataHelper;
import data.DataHelperBD;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MobileBankApiTest {
    private static final String token = APIUtils.shouldReturnToken(DataHelper.getFirstAuthInfo());

    @AfterAll
    public static void setClose() {
        DataHelperBD.clearingTable("auth_codes");
        DataHelperBD.clearingTable("card_transactions");
        DataHelperBD.clearingTable("cards");
        DataHelperBD.clearingTable("users");
    }

    @Test
    public void shouldInternalTransferToFirstCard() {
        String cardFrom = DataHelper.getCardFullNumberFirstUser(1);
        String cardTo = DataHelper.getCardFullNumberFirstUser(0);
        Integer amount = 100;
        int cardFromBalanceBeforeTransfer = DBUtils.shouldGetBalanceCard(cardFrom);
        int cardToBalanceBeforeTransfer = DBUtils.shouldGetBalanceCard(cardTo);
        APIUtils.shouldTransfer(cardFrom, cardTo, amount, token);
        int cardFromBalanceAfterTransfer = DBUtils.shouldGetBalanceCard(cardFrom);
        int cardToBalanceAfterTransfer = DBUtils.shouldGetBalanceCard(cardTo);
        assertEquals(cardFromBalanceBeforeTransfer - amount * 100, cardFromBalanceAfterTransfer);
        assertEquals(cardToBalanceBeforeTransfer + amount * 100, cardToBalanceAfterTransfer);
        DBUtils.shouldSetBalance(cardFrom, cardFromBalanceBeforeTransfer);
        DBUtils.shouldSetBalance(cardTo, cardToBalanceBeforeTransfer);
    }

    @Test
    public void shouldInternalTransferToSecondCard() {
        String cardFrom = DataHelper.getCardFullNumberFirstUser(0);
        String cardTo = DataHelper.getCardFullNumberFirstUser(1);
        Integer amount = 500;
        int cardFromBalanceBeforeTransfer = DBUtils.shouldGetBalanceCard(cardFrom);
        int cardToBalanceBeforeTransfer = DBUtils.shouldGetBalanceCard(cardTo);
        APIUtils.shouldTransfer(cardFrom, cardTo, amount, token);
        int cardFromBalanceAfterTransfer = DBUtils.shouldGetBalanceCard(cardFrom);
        int cardToBalanceAfterTransfer = DBUtils.shouldGetBalanceCard(cardTo);
        assertEquals(cardFromBalanceBeforeTransfer - amount * 100, cardFromBalanceAfterTransfer);
        assertEquals(cardToBalanceBeforeTransfer + amount * 100, cardToBalanceAfterTransfer);
        DBUtils.shouldSetBalance(cardFrom, cardFromBalanceBeforeTransfer);
        DBUtils.shouldSetBalance(cardTo, cardToBalanceBeforeTransfer);
    }

    @Test
    public void shouldTransferToFirstUserFirstCard() {
        String cardFrom = DataHelper.getCardFullNumberOtherUser(0);
        String cardTo = DataHelper.getCardFullNumberFirstUser(0);
        Integer amount = 5000;
        int cardToBalanceBeforeTransfer = DBUtils.shouldGetBalanceCard(cardTo);
        APIUtils.shouldTransfer(cardFrom, cardTo, amount, token);
        int cardToBalanceAfterTransfer = DBUtils.shouldGetBalanceCard(cardTo);
        assertEquals(cardToBalanceBeforeTransfer + amount * 100, cardToBalanceAfterTransfer);
        DBUtils.shouldSetBalance(cardTo, cardToBalanceBeforeTransfer);
    }

    @Test
    public void shouldTransferToFirstUserSecondCard() {
        String cardFrom = DataHelper.getCardFullNumberOtherUser(0);
        String cardTo = DataHelper.getCardFullNumberFirstUser(1);
        Integer amount = 5000;
        int cardToBalanceBeforeTransfer = DBUtils.shouldGetBalanceCard(cardTo);
        APIUtils.shouldTransfer(cardFrom, cardTo, amount, token);
        int cardToBalanceAfterTransfer = DBUtils.shouldGetBalanceCard(cardTo);
        assertEquals(cardToBalanceBeforeTransfer + amount * 100, cardToBalanceAfterTransfer);
        DBUtils.shouldSetBalance(cardTo, cardToBalanceBeforeTransfer);
    }

    @Test
    public void shouldNotTransferMinusAmount() {
        String cardFrom = DataHelper.getCardFullNumberFirstUser(1);
        String cardTo = DataHelper.getCardFullNumberFirstUser(0);
        Integer amount = -5000;
        int cardFromBalanceBeforeTransfer = DBUtils.shouldGetBalanceCard(cardFrom);
        int cardToBalanceBeforeTransfer = DBUtils.shouldGetBalanceCard(cardTo);
        APIUtils.shouldTransfer(cardFrom, cardTo, amount, token);
        int cardFromBalanceAfterTransfer = DBUtils.shouldGetBalanceCard(cardFrom);
        int cardToBalanceAfterTransfer = DBUtils.shouldGetBalanceCard(cardTo);
        assertEquals(cardFromBalanceBeforeTransfer, cardFromBalanceAfterTransfer);
        assertEquals(cardToBalanceBeforeTransfer, cardToBalanceAfterTransfer);
        DBUtils.shouldSetBalance(cardFrom, cardFromBalanceBeforeTransfer);
        DBUtils.shouldSetBalance(cardTo, cardToBalanceBeforeTransfer);
    }

    @Test
    public void shouldNotTransferAmountOverBalance() {
        String cardFrom = DataHelper.getCardFullNumberFirstUser(1);
        String cardTo = DataHelper.getCardFullNumberFirstUser(0);
        int cardFromBalanceBeforeTransfer = DBUtils.shouldGetBalanceCard(cardFrom);
        int cardToBalanceBeforeTransfer = DBUtils.shouldGetBalanceCard(cardTo);
        Integer amount = cardFromBalanceBeforeTransfer / 100 + 1;
        APIUtils.shouldTransfer(cardFrom, cardTo, amount, token);
        int cardFromBalanceAfterTransfer = DBUtils.shouldGetBalanceCard(cardFrom);
        int cardToBalanceAfterTransfer = DBUtils.shouldGetBalanceCard(cardTo);
        assertEquals(cardFromBalanceBeforeTransfer, cardFromBalanceAfterTransfer);
        assertEquals(cardToBalanceBeforeTransfer, cardToBalanceAfterTransfer);
        DBUtils.shouldSetBalance(cardFrom, cardFromBalanceBeforeTransfer);
        DBUtils.shouldSetBalance(cardTo, cardToBalanceBeforeTransfer);
    }
}


