package com.example.aurobaahmad_assignment4;

/**
 * This class contains methods to validate movie information.
 *
 * @author Auroba Ahmad
 */
public class Validation {

    /**
     * This method validates the title.
     * @param title Variable that holds the title being validated.
     * @return A String with validation information.
     */
    public static String validateTitle(String title) {
        //title cannot be empty
        if(title.isEmpty()){
            return "Title cannot be empty";
        }
        //first letter must be capitalized
        if(title.matches("[a-z][A-Za-z\\d\\-:. ]+")) {
            return "First letter must be capitalized";
        }
        //must match criteria
        if(!title.matches("[A-Z][A-Za-z\\d\\-:. ]+")) {
            return "Title can contain letters, numbers, hyphens, colons, periods, and spaces";
        }
        //return empty string if the title matches criteria
        return "";
    }

    /**
     * This method validates the year.
     * @param year Variable that holds the year being validated.
     * @return A String with validation information.
     */
    public static String validateYear(String year) {
        //year cannot be empty
        if(year.isEmpty()) {
            return "Year cannot be empty";
        }
        //must contain digits
        if(year.matches("\\D+")) {
            return "Year must only contain digits";
        }
        //must be for digits
        if(!year.matches("\\d{4}")) {
            return "Year must be four digits";
        }
        //return empty string if the year matches criteria
        return "";
    }

    /**
     * This method validates the sales.
     * @param sales Variable that holds the sale being validated.
     * @return A String with validation information.
     */
    public static String validateSales(String sales) {
        //sales cannot be empty
        if(sales.isEmpty()) {
            return "Sales cannot be empty";
        }
        //must match criteria
        if(!sales.matches("\\d*\\.?\\d+")) {
            return "Sales can only contain digits. " +
                   "The decimal point is optional. " +
                   "If the decimal point is included there must be one number " +
                    "before and at least one number after it.";
        }
        //return empty string if the sale matches criteria
        return "";
    }
}


