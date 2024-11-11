package lk.ijse.CropMonitoringSystem_Backend.util;

import java.util.Base64;
import java.util.UUID;

public class AppUtil {

    public static String generateCode(String prefix) {
        long timestamp = System.currentTimeMillis();
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 6); // Random part
        return prefix + "-" + timestamp + "-" + uniqueSuffix;
    }


    public static String imageToBase64(byte[] image) {
        return Base64.getEncoder().encodeToString(image);
    }
}
