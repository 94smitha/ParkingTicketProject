package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class PreBookedTicket
{
    // initialises static variables
    static int hoursBooked;

    static double priceTemp = 0;

    static String cardNo;

    static String passOrFail;

    static String reason = "n/a";

    static Date expiry;

    public static void preBook() throws ParseException, IOException
    {
        // initialises other variables
        Scanner keyboard = new Scanner(System.in);

        String regNo;

        {
            // user enters reg number
            System.out.print("Enter Reg Number");
            regNo = keyboard.nextLine().toUpperCase();

            // asks the user how many hours they would like to stay for
            System.out.print("How many hours?");
            while (true)
                // error handling for non-numeric values
                try
                {
                    hoursBooked = Integer.parseInt(keyboard.nextLine());
                    break;
                }
                catch (NumberFormatException nfe)
                {
                    System.out.println("Please enter a numeric value");
                }
            // error handling for values below 0 and above 24
            while (hoursBooked <= 0 || hoursBooked > 24)
            {
                System.out.println("Please enter a value between 1 and 24");
                hoursBooked = keyboard.nextInt();

            }
            // finds the current day of the week
            Calendar mydate = Calendar.getInstance();
            int dow = mydate.get(Calendar.DAY_OF_WEEK);
            boolean isWeekday = (dow >= Calendar.MONDAY) && (dow <= Calendar.FRIDAY);
            // if it is a weekday, calls the weekday method to work out the price
            if (isWeekday == true)
            {
                WeekdayPrices.weekday();
                System.out.println("Total price is: £" + WeekdayPrices.price + "0");
                priceTemp = WeekdayPrices.price;
            }
            // if it is a weekend, calls the weekend method to work out the price
            else
            {
                WeekendPrices.weekend();
                System.out.println("Total price is: £" + WeekendPrices.price + "0");
                priceTemp = WeekendPrices.price;
            }

            // asks the user to enter their card number
            System.out.println("\nEnter card number:");
            cardNo = keyboard.next();
            // ensures that the card number must be 16 digits in length
            while (cardNo.length() != 16)
            {
                passOrFail = "fail";
                reason = "Incorrect card number";
                System.out.println("Card numbers must be 16 digits long.\nPlease enter card number.");
                cardNo = keyboard.next();
            }
            passOrFail = "pass";
            // asks the user to enter their expiry date in a mm/yy format
            System.out.println("Please enter your expiration date in the format mm/yy");
            String input = keyboard.next();
            // checks whether the date entered is before the current date
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
            simpleDateFormat.setLenient(false);
            expiry = simpleDateFormat.parse(input);
            boolean expired = expiry.before(new Date());
            // if the card has expired, tells the user this and allows them to re-enter
            while (expired == true)
            {
                passOrFail = "fail";
                reason = "Card expired";
                System.out.println("This card has already expired.\nPlease enter a different card.");
                System.out.println("\nEnter card number:");
                cardNo = keyboard.next();
                // ensures that the card number must be 16 digits in length
                while (cardNo.length() != 16)
                {
                    passOrFail = "fail";
                    reason = "Incorrect Card Number";
                    System.out.println("Card numbers must be 16 digits long.\nPlease enter card number.");
                    cardNo = keyboard.next();
                }

                // asks the user to enter their expiry date in a mm/yy format
                System.out.println("Please enter your expiration date in the format mm/yy");
                input = keyboard.next();
                // checks whether the date entered is before the current date
                simpleDateFormat = new SimpleDateFormat("MM/yy");
                simpleDateFormat.setLenient(false);
                expiry = simpleDateFormat.parse(input);
                expired = expiry.before(new Date());

            }
            passOrFail = "pass";
            // writes the reg number and number of hours booked to a text file
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
                    "C:\\Users\\A612475\\Desktop\\Project1\\TextFiles\\PreBookedData.txt", true)));
            {
                out.print(regNo);
                out.print("\t" + hoursBooked);
                out.println("\t" + priceTemp);

            }
            out.close();
            // confirms transaction and tells the user how much they have been charged
            System.out.println("Thank you. Your card has been charged £" + priceTemp + "0");

            hoursBooked = 0;
        }

    }

}
