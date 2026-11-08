package bank_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tài khoản tiết kiệm áp dụng các quy định giới hạn rút tiền.
 */
public class SavingsAccount extends Account {
    private static final Logger logger = LoggerFactory.getLogger(SavingsAccount.class);
    private static final double MAX_WITHDRAW_AMOUNT = 1000.0;
    private static final double MIN_BALANCE = 5000.0;

    public SavingsAccount(long accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public void deposit(double amount) {
        logger.debug("Bắt đầu xử lý giao dịch nạp tiền cho tài khoản: {}", getAccountNumber());
        double initialBalance = getBalance();
        try {
            doDepositing(amount);
            double finalBalance = getBalance();
            Transaction transaction = new Transaction(
                    Transaction.TYPE_DEPOSIT_SAVINGS, amount, initialBalance, finalBalance);
            addTransaction(transaction);
            logger.info("Nạp tiền thành công. Tài khoản: {}, Số tiền: +{}", getAccountNumber(), amount);
        } catch (InvalidFundingAmountException e) {
            logger.warn("Lỗi nạp tiền tài khoản {}: {}", getAccountNumber(), e.getMessage());
        }
    }

    @Override
    public void withdraw(double amount) {
        logger.debug("Bắt đầu xử lý giao dịch rút tiền cho tài khoản: {}", getAccountNumber());
        double initialBalance = getBalance();
        try {
            if (amount > MAX_WITHDRAW_AMOUNT) {
                throw new InvalidFundingAmountException(amount);
            }
            if (initialBalance - amount < MIN_BALANCE) {
                throw new InsufficientFundsException(amount);
            }

            doWithdrawing(amount);
            double finalBalance = getBalance();

            Transaction transaction = new Transaction(
                    Transaction.TYPE_WITHDRAW_SAVINGS, amount, initialBalance, finalBalance);
            addTransaction(transaction);
            logger.info("Rút tiền thành công. Tài khoản: {}, Số tiền: -{}", getAccountNumber(), amount);
        } catch (BankException e) {
            logger.warn("Từ chối giao dịch rút tiền tài khoản {}: {}", getAccountNumber(), e.getMessage());
        }
    }
}