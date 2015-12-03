package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class EntranceTicket {
	
    static int hoursStayed;

    static int transNo = 0;

    static String regNo = null;

    static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    static Date date = new Date();

    public static void entry() throws Exception
    {
        // initialises variables
        Scanner keyboard = new Scanner(System.in);

        boolean prePaid = false;

        // increments transaction number
        try
        {
            File f = new File("EntranceTicketData.txt");
            if (!f.exists()){
            	f.createNewFile();
            }
            Scanner sc = new Scanner(f);

            while (sc.hasNextLine())
            {
                String line = sc.nextLine();
                String[] details = line.split("\t");
                transNo = Integer.parseInt(details[0]);

            }
            sc.close();

        }
        finally
        {

        }

        // gets current date and time

        // user enter reg number
        System.out.print("Enter Reg Number");
        regNo = keyboard.nextLine().toUpperCase();

        // looks in pre-booked data file to see if number plate is present meaning they have pre-booked
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

                if (line != null && line.contains(regNo))
                {
                    // if number plate is present, sets pre-paid boolean to true
                    {
                        String[] details = line.split("\t");
                        hoursStayed = Integer.parseInt(details[1]);

                        prePaid = true;
                        // reads hours stayed from only the line that the number plate was stored on and sets to a
                        // variable

                    }
                   
                } else
                        prePaid = false;
            }
            scan.close();
        }
        finally
        {
        }

        // writes data to a text file
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
                "EntranceTicketData.txt", true))))
        {
            out.print(transNo + 1);
            out.print("\t" + dateFormat.format(date));
            out.print("\t" + regNo);
            if (prePaid == false)
            {
                out.print("\tno");
            }
            else if (prePaid == true)
            {
                out.print("\tyes");
            }

            System.out.println("\nTransaction Number: " + (transNo + 1));
            System.out.println("Registration Number: " + regNo);

            if (prePaid == true)
            {
                System.out.println("You have pre-paid for " + hoursStayed + " hours. ");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR, hoursStayed);
                Date exitTime = calendar.getTime();
                System.out.println("Your exit time is " + (dateFormat.format(exitTime))
                        + ". After this time you will be required to pay additional charges.");
                out.println("\t" + dateFormat.format(exitTime));
            }
            else if (prePaid == false)
            {
                System.out.println("You have not pre-paid for this ticket. Please pay on departure.");
                out.println("\tNot pre-paid");
            }
        }
        catch (IOException e)
        {
            throw new Exception(e);
        }
        {

        }

    }
}
