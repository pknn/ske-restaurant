import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class SKERestaurant {

    /**
     * Console dialog interprets the user's input
     * and call RestaurantManager method from the object.
     */
    private static void openSKERestaurant() throws IOException {
        RestaurantManager manager = new RestaurantManager();
        Scanner input = new Scanner(System.in);
        boolean isOpen = true;

        System.out.println("Welcome to SKE Restaurant");
        manager.init();
        while (isOpen) {
            System.out.println("What do you want to do?");
            System.out.print("(o)rder, (c)heck order, check ou(t): ");
            switch (input.next().charAt(0)) {
                case 'o':
                    manager.orderFood();
                    break;
                case 'c':
                    manager.checkOrder(false);
                    break;
                case 't':
                    manager.checkOrder(true);
                    isOpen = false;
                    break;
                default:
                    break;
            }
        }

        // Check if local time is night or day
        int time = LocalDateTime.now().getHour();
        if (time >= 18 || time <= 7) System.out.println("Have a good night!!!");
        else System.out.println("Have a good day!!!");
    }

    /**
     * Run the application
     */
    public static void main(String[] args) {
        try {
            openSKERestaurant();
        } catch (IOException e) {
            System.exit(0);
        }
    }
}