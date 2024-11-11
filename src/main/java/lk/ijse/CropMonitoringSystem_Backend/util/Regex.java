package lk.ijse.CropMonitoringSystem_Backend.util;

import java.util.regex.Pattern;

public class Regex {

    public static boolean codeMatcher(String code){
        String regexForCode = "^[A-Z]+-\\d{13}-[a-fA-F0-9]{6}$"; // Regex pattern to match any prefix, timestamp, and 6-character hex suffix
        Pattern regexPattern = Pattern.compile(regexForCode);
        return regexPattern.matcher(code).matches();
    }

    public static boolean emailValidator(String email){
        String regexForEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern regexPattern = Pattern.compile(regexForEmail);
        return regexPattern.matcher(email).matches();
    }

    public static boolean contactNumberValidator(String contactNumber){
        String regexForContactNumber = "^(?:0?(77|76|78|34|75|72|74)[0-9]{7}|(77|76|78|34|75|72|74)[0-9]{8})$";
        Pattern regexPattern = Pattern.compile(regexForContactNumber);
        return regexPattern.matcher(contactNumber).matches();
    }

}
