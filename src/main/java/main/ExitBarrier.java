package main;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExitBarrier extends EntranceTicket
{
    private static final Logger log = Logger.getLogger(ExitBarrier.class.getName());

    static boolean prePaid;

    static double priceTemp = 0;

    static String yesOrNo;

    static int lineNumber;

    static Date entryTime = null;

    static Date exitTime = new Date();

    static int hours;

    static int minutes;

    static int differenceTimes = hours;

    static int differenceMinutes;

    static int hoursBooked;

    static int differenceHours;

    static int hoursToCharge;

    static double discountPrice;

    static String regNumber;

    static boolean overstayed = false;

    static String cardNo;

    static String passOrFail;

    static String reason;

    static Date expiry;
    
    static boolean invalid = false;

    public static void exit() throws IOException, ParseException
    {
        // initialises variables
        Scanner keyboard = new Scanner(System.in);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String filePath = "EntranceTicketData.txt";

        DecimalFormat twoDP = new DecimalFormat("##.##");

        // user enters reg Number
        System.out.println();
        System.out.print("Enter your Reg Number");
        regNumber = keyboard.nextLine().toUpperCase();

        // read number plate from EntranceTicket class

        try
        {
            
			File file = new File(filePath);
			if (!file.exists()){
				file.createNewFile();
			}
            Scanner scan = new Scanner(file);
            
            int lineNum = 0;
            while (scan.hasNextLine())
            {
                String line = scan.nextLine();
                lineNum++;
                
                if (line != null && line.contains(regNumber))
                {
                	invalid = false;
                    System.out.println("\nThank you for your stay");
                	stayedFor(dateFormat, lineNum, line);
                	break;
                	
                
                }
                else if (!scan.hasNextLine() && !line.contains(regNumber))
                {
                	invalid = true;
                	CentralLogFile.centralLog();
                	System.out.println("\nIncorrect number plate. Please try again");
                	System.out.println("-------------------");
                	ExitBarrier.exit();
                }
            } 
        }

        catch (ParseException e)
        {
            log.log(Level.SEVERE, "error parseing", e);
        }
        finally
        {
        }

        try
        {
            File file = new File("PreBookedData.txt");
            if (!file.exists()){
            	file.createNewFile();
            }
            Scanner scan = new Scanner(file);

            while (scan.hasNextLine())
            {
                String line = scan.nextLine();

                if (line != null && line.contains(regNumber))
                {
                    {

                        // reads hours stayed from only the line that the number plate was stored on and sets to a
                        // variable
                        String[] details = line.split("\t");
                        hoursBooked = Integer.parseInt(details[1]);
                        System.out.println("\nYou have pre-paid for this ticket for " + hoursBooked + " hours");

                        differenceHours = hoursToCharge - hoursBooked;
                        if (differenceHours > 0)
                        {
                            overstayed = true;
                            System.out.print("You have overstayed by " + differenceHours + " hours. ");
                            Calendar mydate = Calendar.getInstance();
                            int dow = mydate.get(Calendar.DAY_OF_WEEK);
                            boolean isWeekday = (dow >= Calendar.MONDAY) && (dow <= Calendar.FRIDAY);
                            // if it is a weekday, calls the weekday method to work out the price
                            if (isWeekday == true)
                            {
                                WeekdayPrices.weekday();
                                discountPrice = Double.parseDouble(twoDP
                                        .format((WeekdayPrices.price) - (WeekdayPrices.price) / 10));
                                System.out.println("An additional charge of £ " + discountPrice + " is required");
                            }
                            // if it is a weekend, calls the weekend method to work out the price
                            else
                            {
                                WeekendPrices.weekend();
                                discountPrice = Double.parseDouble(twoDP
                                        .format((WeekendPrices.price) - (WeekendPrices.price) / 10));
                                System.out.println("An additional charge of £" + discountPrice + " is required");

                            }
                            // asks the user to enter their card number
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
                            passOrFail = "pass";
                            // asks the user to enter their expiry date in a mm/yy format
                            System.out.println("Please enter your expiration date in the format mm/yy");
                            String input = keyboard.next();
                            // checks whether the date entered is before the current date
                            SimpleDateFormat simpleDateFormatPay = new SimpleDateFormat("MM/yy");
                            dateFormat.setLenient(false);
                            try
                            {
                                expiry = simpleDateFormatPay.parse(input);
                            }
                            catch (ParseException e)
                            {
                                log.log(Level.SEVERE, "error", e);
                            }
                            boolean expired = expiry.before(new Date());
                            // if the card has expired, tells the user this and allows them to re-enter
                            while (expired == true)
                            {
                                passOrFail = "fail";
                                reason = "Card expired";
                                AuthorisationFile.authorisation();
                                System.out
                                        .println("This card has already expired.\nPlease enter your expiry date in the format mm/yy");
                                input = keyboard.next();
                                dateFormat = new SimpleDateFormat("MM/yy");
                                dateFormat.setLenient(false);
                                try
                                {
                                    expiry = dateFormat.parse(input);
                                }
                                catch (ParseException e)
                                {
                                    log.log(Level.SEVERE, "error", e);
                                }
                                expired = expiry.before(new Date());
                            }
                            passOrFail = "pass";
                            System.out.println("Thank you. Your card has been charged £" + discountPrice);

                        }
                        else
                        {
                            overstayed = false;
                            discountPrice = 0;
                        }

                    }
                }
            }
            scan.close();
        }
        finally
        {
        }

        try
        {

            if (prePaid == false)
            {
            	differenceHours = 0;
                Calendar mydate = Calendar.getInstance();
                int dow = mydate.get(Calendar.DAY_OF_WEEK);
                boolean isWeekday = (dow >= Calendar.MONDAY) && (dow <= Calendar.FRIDAY);
                // if it is a weekday, calls the weekday method to work out the price
                if (isWeekday == true)
                {
                    WeekdayPrices.weekday();
                    System.out.println("Total price is £" + WeekdayPrices.price + "0");
                    priceTemp = WeekdayPrices.price;
                }
                // if it is a weekend, calls the weekend method to work out the price
                else
                {
                    WeekendPrices.weekend();
                    System.out.println("Total price is £" + WeekendPrices.price + "0");
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
                    AuthorisationFile.authorisation();
                    System.out.println("Card numbers must be 16 digits long.\nPlease enter card number.");
                    cardNo = keyboard.next();
                }
                passOrFail = "pass";
                // asks the user to enter their expiry date in a mm/yy format
                System.out.println("Please enter your expiration date in the format mm/yy");
                String input = keyboard.next();
                // checks whether the date entered is before the current date
                SimpleDateFormat simpleDateFormatPay = new SimpleDateFormat("MM/yy");
                dateFormat.setLenient(false);
                try
                {
                    expiry = simpleDateFormatPay.parse(input);
                }
                catch (ParseException e)
                {
                    log.log(Level.SEVERE, "error", e);
                }
                boolean expired = expiry.before(new Date());
                // if the card has expired, tells the user this and allows them to re-enter
                while (expired == true)
                {
                    passOrFail = "fail";
                    reason = "Card expired";
                    AuthorisationFile.authorisation();
                    System.out.println("This card has already expired.\nPlease enter a new card");
                    System.out.println("\nEnter card number:");
                    cardNo = keyboard.next();
                    // ensures that the card number must be 16 digits in length
                    while (cardNo.length() != 16)
                    {
                        passOrFail = "fail";
                        reason = "Incorrect card number";
                        AuthorisationFile.authorisation();
                        System.out.println("Card numbers must be 16 digits long.\nPlease enter card number.");
                        cardNo = keyboard.next();
                    }
                    System.out.println("Please enter your expiration date in the format mm/yy");
                    input = keyboard.next();
                    dateFormat = new SimpleDateFormat("MM/yy");
                    dateFormat.setLenient(false);
                    try
                    {
                        expiry = dateFormat.parse(input);
                    }
                    catch (ParseException e)
                    {
                        log.log(Level.SEVERE, "error", e);
                    }
                    expired = expiry.before(new Date());
                }
                passOrFail = "pass";
                System.out.println("Thank you. Your card has been charged £" + priceTemp + "0");

                if (prePaid == true)
                {
                    System.out.println(hours);

                }

            }
        }
        finally
        {
        }

    }

	private static void stayedFor(DateFormat dateFormat, int lineNum,
			String line) throws ParseException {
		{ 
		        lineNumber = lineNum;
		        // splits string number plate is found on at tab and reads whether it is prepaid or not
		        String[] details = line.split("\t");

		        // finds entry time and assigns to variable
		        entryTime = dateFormat.parse(details[1]);

		        System.out.println("\nArrived at: " + dateFormat.format(entryTime));
		        System.out.println("Departing at: " + dateFormat.format(exitTime));

		        // finds the difference between the entry and exit time
		        long secs = (exitTime.getTime() - entryTime.getTime()) / 1000;
		        hours = (int) (secs / 3600);
		        minutes = (int) ((secs % 3600) / 60);

		        if (minutes > 0)
		        {
		            hoursToCharge = hours + 1;
		        }

		        else if (minutes == 0)
		        {
		            hoursToCharge = hours;
		        }

		        System.out.println("\nStayed for: " + hoursToCharge + " hours");

		        yesOrNo = details[3];

		        // if it is prepaid, set prepaid boolean to true, if not, set to false
		        if ("yes".equals(yesOrNo))
		        {
		            prePaid = true;

		        }
		        else if ("no".equals(yesOrNo))
		        {
		            prePaid = false;
		        }

		    }
	}

}
