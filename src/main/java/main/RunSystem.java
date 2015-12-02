package main;

import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

public class RunSystem
{
    static int choice;

    public static void main(String[] args) throws Exception
    {

        Scanner keyboard = new Scanner(System.in);

        do
        {
            System.out.println("--------------------------");
            System.out.println("         CAR PARK         ");
            System.out.println("--------------------------");

            System.out.println("\n1. Pre-book a ticket");
            System.out.println("2. Enter the Car Park");
            System.out.println("3. Exit the Car Park");
            System.out.println("4. Quit system");

            System.out.print("\nPlease enter your choice: ");
            choice = keyboard.nextInt();

            if (choice != 1 && choice != 2 && choice != 3 && choice != 4)
            {
                System.out.println();
            }
            else if (choice == 1)
            {
                PreBookedTicket.preBook();
                AuthorisationFile.authorisation();
            }
            else if (choice == 2)
            {
                EntranceTicket.entry();
            }
            else if (choice == 3)
            {
                ExitBarrier.exit();
                ExitBarrier.invalid = false;
                CentralLogFile.centralLog();
                AuthorisationFile.authorisation();
            }
        }
        while (choice != 4);

        System.exit(0);

    }

}
