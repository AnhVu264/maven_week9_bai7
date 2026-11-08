package bank_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tài khoản vãng lai.
 */
public class CheckingAccount extends Account {
    private static final Logger logger = LoggerFactory.getLogger(CheckingAccount.class);

    public CheckingAccount(long accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public void deposit(double amount) {
        double initialBalance = getBalance();
        try {
            doDepositing(amount);
            double finalBalance = getBalance();
            Transaction t = new Transaction(
                    Transaction.TYPE_DEPOSIT_CHECKING,
                    amount,
                    initialBalance,
                    finalBalance);
            addTransaction(t);
            logger.info("Nạp tiền vãng lai thành công: +{}", amount);
        } catch (BankException e) {
            logger.warn("Lỗi nạp tiền vãng lai: {}", e.getMessage()); // Đã sửa System.out
        }
    }

    @Override
    public void withdraw(double amount) {
        double initialBalance = getBalance();
        try {
            doWithdrawing(amount);
            double finalBalance = getBalance();
            Transaction t = new Transaction(
                    Transaction.TYPE_WITHDRAW_CHECKING,
                    amount,
                    initialBalance,
                    finalBalance);
            addTransaction(t);
            logger.info("Rút tiền vãng lai thành công: -{}", amount);
        } catch (BankException e) {
            logger.warn("Lỗi rút tiền vãng lai: {}", e.getMessage()); // Đã sửa System.out
        }
    }
}