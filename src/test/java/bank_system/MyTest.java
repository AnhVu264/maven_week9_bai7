package bank_system;

import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyTest {
    @Test
    public void testFilePathHandling_Buggy() {
        String expectedPath = "logs" + File.separator + "transaction.log";
        File logFile = new File("logs", "transaction.log");
        assertEquals(expectedPath, logFile.getPath(), "Đường dẫn file log không đúng");
    }
}

// Kiểm tra tính năng của bài 6