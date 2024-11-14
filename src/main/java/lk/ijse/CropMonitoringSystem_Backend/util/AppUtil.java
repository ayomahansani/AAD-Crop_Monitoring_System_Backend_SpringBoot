package lk.ijse.CropMonitoringSystem_Backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.LocationDTO;
import org.springframework.data.geo.Point;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class AppUtil {

    // method for generating ids
    public static String generateCode(String prefix) {
        long timestamp = System.currentTimeMillis();
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 6); // Random part
        return prefix + "-" + timestamp + "-" + uniqueSuffix;
    }

    // method for converting images to Base64
    public static String convertImageToBase64(byte[] image) {
        return Base64.getEncoder().encodeToString(image);
    }

    // method to convert JSON string to Point
    public static Point convertToPoint(String locationJson) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        LocationDTO locationDTO = mapper.readValue(locationJson, LocationDTO.class);
        double x = locationDTO.getLongitude();
        double y = locationDTO.getLatitude();
        return new Point(x, y);
    }

    // method to convert a JSON array string to a List<String>
    public static List<String> convertJsonArrayToList(String jsonArrayString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonArrayString, List.class);
    }

}
