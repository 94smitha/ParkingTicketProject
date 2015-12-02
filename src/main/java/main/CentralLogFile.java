package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CentralLogFile
{
	
	private static final Logger log = Logger.getLogger(CentralLogFile.class.getName());

	
	private CentralLogFile(){}
	
    static boolean prePaid;

    static double preBookedPrice;

    public static void centralLog()
    {

        String regNo = ExitBarrier.regNumber;

        int transNo = 0;
        Date entryTime = null;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        System.out.println(regNo);
        String filePath = "C:\\Users\\A612475\\Desktop\\Project1\\TextFiles\\EntranceTicketData.txt";

        try
        {
            
			File file = new File(filePath);
            Scanner scan = new Scanner(file);

            while (scan.hasNextLine())
            {
                String line = scan.nextLine();

                if (line != null && line.contains(regNo))
                    
                {

            

                    {
                        // splits string number plate is found on at tab
                        String[] details = line.split("\t");

                        // finds entry time and assigns to variable
                        transNo = Integer.parseInt(details[0]);
                        entryTime = dateFormat.parse(details[1]);
                    }
                }
            }
        } catch (FileNotFoundException e) {

        	log.log(Level.SEVERE, "File Not Found", e);
		} 
        catch (ParseException e) {

        	log.log(Level.SEVERE, "Error Parsing", e);
		}
        finally {}


        try
        {
            File file = new File("C:\\Users\\A612475\\Desktop\\Project1\\TextFiles\\PreBookedData.txt");
            Scanner scanner = new Scanner(file);

            int lineNum = 0;
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                lineNum++;

                if (line != null && line.contains(regNo))
                {
                    // if number plate is present, sets pre-paid boolean to true

                    {
                        String[] details = line.split("\t");

                        preBookedPrice = Integer.parseInt(details[2]);
                        // reads hours stayed from only the line that the number plate was stored on and sets to a
                        // variable

                    }
                    
                }else
                        preBookedPrice = 0;
                {

                }
            }
        } catch (FileNotFoundException e) {
        	log.log(Level.SEVERE, "File Not Found", e);;
		}

        finally
        {
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
                "C:\\Users\\A612475\\Desktop\\Project1\\TextFiles\\CentralLogFile.txt", true))))
        {
        if (ExitBarrier.invalid == true)
        	{
        	System.out.println(ExitBarrier.invalid);
        		out.println("INVALID NUMBER PLATE");
        	}

        	else if (ExitBarrier.invalid == false)
        	{
        		System.out.println(ExitBarrier.invalid);
            out.print(transNo + ",");
            out.print(regNo + ",");
            out.print(dateFormat.format(entryTime) + ",");
            out.print(dateFormat.format(ExitBarrier.exitTime) + ",");
            out.print(ExitBarrier.hours + ":");
            out.print(ExitBarrier.minutes + ",");
            out.println(preBookedPrice + ExitBarrier.priceTemp + ExitBarrier.discountPrice);
        	}
        	
        } catch (IOException e) {
        	log.log(Level.SEVERE, "IO Exception", e);;
		}

    }	
}
