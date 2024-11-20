package com.example.aurobaahmad_assignment4;

import com.google.gson.annotations.SerializedName;

/**
 * This class contains methods to hold movie information.
 *
 * @author Auroba Ahmad
 */
public class Movie {
    @SerializedName("title")
    String Title;

    @SerializedName("year")
    int Year;

    @SerializedName("sales")
    double Sales;

    /**
     * This constructor takes three parameters and initializes the variables to the parameters.
     * @param title initializes the variable Title.
     * @param year initializes the variable Year.
     * @param sales initializes the variable Sales.
     */
    public Movie(String title, int year, double sales) {
        Title = title;
        Year = year;
        Sales = sales;
    }

    /**
     * Gets the title variable.
     * @return the contents of title.
     */
    public String getTitle() {
        return Title;
    }

    /**
     * Sets the title variable.
     * @param title
     */
    public void setTitle(String title) {
        Title = title;
    }

    /**
     * Gets the year variable.
     * @return the contents of year.
     */
    public int getYear() {
        return Year;
    }

    /**
     * Sets the year variable.
     * @param year
     */
    public void setYear(int year) {
        Year = year;
    }

    /**
     * Gets the sales variable.
     * @return the contents of sales.
     */
    public double getSales() {
        return Sales;
    }

    /**
     * Sets the sales variable.
     * @param sales
     */
    public void setSales(double sales) {
        Sales = sales;
    }

    /**
     * Returns the title, year, and sales in a string.
     * @return value in string format.
     */
    @Override
    public String toString(){
        return Title + ", " + Year + ", " + Sales;
    }
}
