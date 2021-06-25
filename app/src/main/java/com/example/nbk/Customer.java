package com.example.nbk;

public class Customer {
     static String FirstName;
     static float Balance;
     static String CreditCardNumber,ExpDate, LastName ,Pin;

    public Customer(float balance, String creditCardNumber, String expDate, String firstName, String lastName, String pin) {
        Balance = balance;
        CreditCardNumber = creditCardNumber;
        ExpDate = expDate;
        FirstName = firstName;
        LastName = lastName;
        Pin = pin;
    }
    public Customer(){}



    public float getBalance() {
        return Balance;
    }

    public void setBalance(float balance) {
        Balance = balance;
    }

    public static String getCreditCardNumber() {
        return CreditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        CreditCardNumber = creditCardNumber;
    }

    public static String getExpDate() {
        return ExpDate;
    }

    public void setExpDate(String expDate) {
        ExpDate = expDate;
    }

    public static String  getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public static String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public static String getPin() {
        return Pin;
    }

    public void setPin(String pin) {
        Pin = pin;
    }

}
