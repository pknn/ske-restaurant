import java.util.Scanner;

/**
 * SKERestaurant is an app for restaurant
 * App can take an menuOrder, check menuOrder, calculate total price.
 *
 * @author Pakanon Pantisawat
 */

class SKERestaurant {

    final static Scanner sc = new Scanner(System.in);
    static String[] menuItems;
    static double[] menuPrices;
    static int[] menuOrder;

    public static void main(String[] args) {
        RestaurantManager.init();
        if (args.length > 1 && args[0].equals("-m")) {
            RestaurantManager.setMenu(args[1]);
        }
        menuItems = RestaurantManager.getMenuItems();
        menuPrices = RestaurantManager.getMenuPrices();
        menuOrder = new int[menuItems.length];
        openSKERestaurant();
    }

    /**
     * Wait for user's input and call a responsive method
     * also call for initialization method
     */
    private static void openSKERestaurant() {

        System.out.println(" _____ _____ _____ _____ _____ _____ _____ _____ _____ _____ _____ _____ _____ ");
        System.out.println("|   __|  |  |   __| __  |   __|   __|_   _|  _  |  |  | __  |  _  |   | |_   _|");
        System.out.println("|__   |    -|   __|    -|   __|__   | | | |     |  |  |    -|     | | | | | |");
        System.out.println("|_____|__|__|_____|__|__|_____|_____| |_| |__|__|_____|__|__|__|__|_|___| |_|");
        while (true) {
            printMenu();
            String input = sc.nextLine();
            switch (input) {
                case "p":
                    placeOrder();
                    break;
                case "c":
                    checkOrder();
                    break;
                case "d":
                    // TODO: 11/3/17 Check Promotion
                    break;
                case "o":
                    // TODO: 11/3/17 Check out
                    System.out.println("Good bye!!!");
                    break;
                case "?":
                    printMenu();
                    break;
                case "s":
                    System.exit(0);
                default:
                    System.out.println("Invalid menu");
                    break;
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("\"p\"  Place menuOrder");
        System.out.println("\"c\"  Check menuOrder");
        System.out.println("\"d\"  Check promotion");
        System.out.println("\"o\"  Check out");
        System.out.println("\"?\" See this menu again");
        System.out.print("cmd> ");
    }

    /**
     * Print all the menu items we have
     * ask user for input menu number and amount they want
     * then add to menuOrder array
     */
    private static void placeOrder() {
        System.out.println();
        System.out.println("Menu : ");
        for (int i = 0; i < menuItems.length; i++) {
            System.out.printf("%2d. %-30s Price: %8.2f Baht%n", i + 1, menuItems[i], menuPrices[i]);
        }
        System.out.println();

        System.out.print("What would you like to menuOrder (by menu number) : ");
        int orderNo = sc.nextInt();
        System.out.print("How many of them: ");
        int amount = sc.nextInt();
        menuOrder[orderNo - 1] += amount;

        System.out.printf("You've ordered %s for %d piece(s)%n%n", menuItems[orderNo - 1], menuOrder[orderNo - 1]);
    }

    /**
     * Print all order user have places and total price.
     * Formatted text
     */
    private static void checkOrder() {
        double total = 0;
        System.out.println();
        System.out.println("Your total order : ");
        for (int i = 0, j = 1; i < menuItems.length; i++) {
            if (menuOrder[i] == 0) continue;
            double price = menuOrder[i] * menuPrices[i];
            total += price;
            System.out.printf("%2d. %-30s  Amount: %3d  Price: %8.2f Baht%n", j, menuItems[i], menuOrder[i], price);
            j++;
        }
        System.out.printf("%54s: %8.2f Baht%n", "Total", total);
    }


}