package bank_system;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp đại diện cho một giao dịch ngân hàng.
 */
public class Transaction {
    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

    public static final int TYPE_DEPOSIT_CHECKING = 1;
    public static final int TYPE_WITHDRAW_CHECKING = 2;
    public static final int TYPE_DEPOSIT_SAVINGS = 3;
    public static final int TYPE_WITHDRAW_SAVINGS = 4;

    private int type;
    private double amount;
    private double initialBalance;
    private double finalBalance;

    public Transaction(int type, double amount, double initialBalance, double finalBalance) {
        this.type = type;
        this.amount = amount;
        this.initialBalance = initialBalance;
        this.finalBalance = finalBalance;
    }

    // Các getter và setter tương tự mã gốc... (đã ẩn bớt để ngắn gọn)

    /**
     * Lấy tên chuỗi mô tả dựa trên mã loại giao dịch.
     *
     * @param transactionType Mã loại giao dịch
     * @return Tên chuỗi tương ứng
     */
    public static String getTypeString(int transactionType) {
        switch (transactionType) {
            case TYPE_DEPOSIT_CHECKING:
                return "Nạp tiền vãng lai";
            case TYPE_WITHDRAW_CHECKING:
                return "Rút tiền vãng lai";
            case TYPE_DEPOSIT_SAVINGS:
                return "Nạp tiền tiết kiệm";
            case TYPE_WITHDRAW_SAVINGS:
                return "Rút tiền tiết kiệm";
            default:
                return "Không rõ";
        }
    }

    /**
     * Trả về chuỗi tóm tắt thông tin của giao dịch hiện tại.
     *
     * @return Chuỗi tóm tắt định dạng sẵn
     */
    public String getTransactionSummary() {
        logger.debug("Trích xuất tóm tắt cho loại giao dịch ID: {}", this.type);

        String typeStr = getTypeString(this.type);
        String initialStr = formatMoney(this.initialBalance);
        String amountStr = formatMoney(this.amount);
        String finalStr = formatMoney(this.finalBalance);

        return String.format(Locale.US,
                "- Kiểu giao dịch: %s. Số dư ban đầu: $%s. Số tiền: $%s. Số dư cuối: $%s.",
                typeStr, initialStr, amountStr, finalStr);
    }

    private String formatMoney(double value) {
        return String.format(Locale.US, "%.2f", value);
    }
}