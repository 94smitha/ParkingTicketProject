package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthorisationFile
{

    private AuthorisationFile()
    {
    }
    
    private static final Logger log = Logger.getLogger(AuthorisationFile.class.getName());

    static int transNo = 0;

    static String transType;

    static String cardNo;

    static String passOrFail;

    static String reason;

    static Date expiry;

    @SuppressWarnings("null")
	public static void authorisation() throws IOException
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        DateFormat expiryFormat = new SimpleDateFormat("MM/yy");
        Date date = new Date();

        try
        {
            File f = new File("AuthorisationFile.txt");
            if(!f.exists()){
            	f.createNewFile();
            }
            Scanner sc = new Scanner(f);

            while (sc.hasNextLine())
            {
                String line = sc.nextLine();
                String[] details = line.split(",");
                transNo = Integer.parseInt(details[0]);

            }
            sc.close();
        }finally {}

            if (RunSystem.choice == 1)
            {
                transType = "P";
                cardNo = PreBookedTicket.cardNo;
                passOrFail = PreBookedTicket.passOrFail;
                reason = PreBookedTicket.reason;
                expiry = PreBookedTicket.expiry;
            }
            else if (RunSystem.choice == 3)
            {
                cardNo = ExitBarrier.cardNo;
                passOrFail = ExitBarrier.passOrFail;
                reason = ExitBarrier.reason;
                expiry = ExitBarrier.expiry;

                if (ExitBarrier.overstayed)
                {
                    transType = "O";
                }
                else
                {
                    transType = "D";
                }
            }

            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
                    "AuthorisationFile.txt", true))))
            {
                out.print((transNo + 1) + ",");
               
                if ("fail".equals(passOrFail))
                {
                    out.print("fail" + ",");
                    out.println(reason);
                }
                else if ("pass".equals(passOrFail))

                {
                	 out.print(transType + ",");
                     out.print(cardNo + " ");
                     out.print(expiryFormat.format(expiry) + ",");
                     out.print(dateFormat.format(date) + ",");
                	out.print("pass" + ",");
                    out.println("n/a");
                }

            }

        }
    }

