package bank_system;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp Bank quản lý danh sách khách hàng và xử lý nhập liệu.
 */
public class Bank {
    private static final Logger logger = LoggerFactory.getLogger(Bank.class);
    private List<Customer> customerList;

    public Bank() {
        this.customerList = new ArrayList<>();
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    /**
     * Cập nhật danh sách khách hàng của ngân hàng.
     *
     * @param customerList Danh sách khách hàng mới
     */
    public void setCustomerList(List<Customer> customerList) {
        if (customerList == null) {
            this.customerList = new ArrayList<>();
        } else {
            this.customerList = customerList;
        }
    }

    /**
     * Đọc danh sách khách hàng từ luồng đầu vào.
     *
     * @param inputStream Luồng dữ liệu chứa thông tin khách hàng
     */
    public void readCustomerList(InputStream inputStream) {
        logger.debug("Bắt đầu đọc dữ liệu khách hàng từ InputStream...");
        if (inputStream == null) {
            logger.error("InputStream bị null, từ chối đọc dữ liệu.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            Customer currentCustomer = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                int lastSpaceIndex = line.lastIndexOf(' ');
                if (lastSpaceIndex <= 0) {
                    continue;
                }

                String token = line.substring(lastSpaceIndex + 1).trim();
                if (token.matches("\\d{9}")) {
                    String name = line.substring(0, lastSpaceIndex).trim();
                    currentCustomer = new Customer(Long.parseLong(token), name);
                    customerList.add(currentCustomer);
                    logger.info("Đã thêm khách hàng mới: {}", name);
                } else if (currentCustomer != null) {
                    processAccountLine(line, currentCustomer);
                }
            }
        } catch (Exception e) {
            logger.error("Lỗi khi đọc file dữ liệu khách hàng", e);
        }
    }

    private void processAccountLine(String line, Customer customer) {
        String[] parts = line.split("\\s+");
        if (parts.length >= 3) {
            try {
                long accountNumber = Long.parseLong(parts[0]);
                double accountBalance = Double.parseDouble(parts[2]);

                if (Account.CHECKING_TYPE.equals(parts[1])) {
                    customer.addAccount(new CheckingAccount(accountNumber, accountBalance));
                } else if (Account.SAVINGS_TYPE.equals(parts[1])) {
                    customer.addAccount(new SavingsAccount(accountNumber, accountBalance));
                }
            } catch (NumberFormatException e) {
                logger.warn("Dữ liệu tài khoản sai định dạng: {}", line);
            }
        }
    }

    public String getCustomersInfoByIdOrder() {
        customerList.sort(Comparator.comparingLong(Customer::getIdNumber));
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < customerList.size(); i++) {
            result.append(customerList.get(i).getCustomerInfo());
            if (i < customerList.size() - 1) {
                result.append("\n");
            }
        }
        return result.toString();
    }

    public String getCustomersInfoByNameOrder() {
        List<Customer> sortedList = new ArrayList<>(customerList);
        sortedList.sort((c1, c2) -> {
            int nameCompare = c1.getFullName().compareTo(c2.getFullName());
            return nameCompare != 0 ? nameCompare : Long.compare(c1.getIdNumber(), c2.getIdNumber());
        });

        StringBuilder result = new StringBuilder();
        for (Customer c : sortedList) {
            result.append(c.getCustomerInfo()).append("\n");
        }
        return result.toString().trim();
    }
}