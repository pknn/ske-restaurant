import java.util.Scanner;

/**
 * SKERestaurant is an app for restaurant
 * App can take an menuOrder, check menuOrder, calculate total price.
 *
 * @author Pakanon Pantisawat
 */

class SKERestaurant {

    private final static Scanner sc = new Scanner(System.in);
    private static String[] menuItems;
    private static double[] menuPrices;
    private static int[] menuOrder;
    private static String[] discount;

    public static void main(String[] args) {
        RestaurantManager.init();
        if (args.length > 1 && args[0].equals("-m")) {
            RestaurantManager.setMenu(args[1]);
        }
        load();
        openSKERestaurant();
    }

    private static void load() {
        menuItems = RestaurantManager.getMenuItems();
        menuPrices = RestaurantManager.getMenuPrices();
        menuOrder = new int[menuItems.length];
        discount = RestaurantManager.getPromotionItems();
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

        String input = "?";
        while (true) {
            switch (input) {
                case "p":
                    placeOrder();
                    break;
                case "c":
                    checkOrder();
                    break;
                case "d":
                    checkPromotion();
                    break;
                case "o":
                    double total = checkOut();
                    RestaurantManager.recordOrder(RestaurantManager.getOrderNum(), menuOrder, total);
                    System.out.println("Have a nice day!!");
                    System.exit(0);
                    break;
                case "m":
                    RestaurantManager.manage();
                    load();
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
            System.out.print("cmd> ");
            input = sc.nextLine().trim();
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("\"p\"  Place menu order");
        System.out.println("\"c\"  Check menu order");
        System.out.println("\"d\"  Check promotion");
        System.out.println("\"o\"  Check out");
        System.out.println("\"m\"  Manage");
        System.out.println("\"?\" See this menu again");
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

        int orderNo = getIntReply("What would you like to menuOrder (by menu number)");
        if (orderNo > menuOrder.length || orderNo == -1) {
            System.out.println("Invalid menu number");
            return;
        }

        int amount = getIntReply("How many of them");
        if (amount == -1) return;
        menuOrder[orderNo - 1] += amount;

        System.out.printf("You've ordered %s for %d piece(s)%n%n", menuItems[orderNo - 1], menuOrder[orderNo - 1]);
    }

    /**
     * Print all order user have places and total price.
     *
     * @return total price of order
     */
    private static double checkOrder() {
        double total = 0;
        System.out.println();
        System.out.println("Your total order : ");
        System.out.println();
        System.out.printf("%2s. %-40s %3s %8s%n", "No", "Title", "Qty", "Price");
        for (int i = 0, j = 1; i < menuItems.length; i++) {
            if (menuOrder[i] == 0) continue;
            double price = menuOrder[i] * menuPrices[i];
            total += price;
            System.out.printf("%2d. %-40s %3d %8.2f Baht%n", j, menuItems[i], menuOrder[i], price);
            j++;
        }
        System.out.printf("%47s: %8.2f Baht%n", "Total", total);
        return total;
    }

    /**
     * Check if there is any promotion and print list of promotion
     * else tell user's there is no promotion right now
     */
    private static void checkPromotion() {
        System.out.println();
        if (discount == null) {
            System.out.println("There is no promotion right now");
            return;
        }
        System.out.println("Current Promotion: ");
        System.out.printf("#%-11s %-40s %8s%n", "Code", "Title", "Discount");
        for (String aLine : discount) {
            String[] line = aLine.split(";");
            System.out.printf("#%-10s %-42s %6s%%%n", line[0], line[1], line[2]);
        }
    }

    /**
     * Checkout method, check if user want to use coupon
     * and print bill
     */
    private static double checkOut() {
        double discount = 0;
        System.out.print("Do you have any coupon code (y/n): ");
        String response = sc.nextLine();
        if (!response.equals("y") && !response.equals("n")) {
            System.out.println("Invalid input");
            checkOut();
            return 0;
        } else {
            if (response.equals("y")) discount = getDiscount();
        }
        double total = printBill(discount);
        double cash = 0;
        while (true) {
            System.out.print("Cash: ");
            String input = sc.nextLine();
            if (input.charAt(0) < 48 || input.charAt(0) > 57) {
                System.out.println("Invalid input");
                continue;
            }
            cash = Double.parseDouble(input);
            if (cash < total) {
                System.out.println("IS THAT JOKE? HAHAHA");
                System.out.printf("You have to pay at least %.2f Baht%n", total);
            } else {
                System.out.printf("%47s: %8.2f Baht%n", "Cash", cash);
                System.out.printf("%47s: %8.2f Baht%n", "Change", cash - total);
                break;
            }
        }
        return total;
    }

    /**
     * Check if coupon is existed
     *
     * @return discount of that coupon or 0 if coupon isn't in the list
     */
    private static double getDiscount() {
        System.out.print("Fill your coupon code: ");
        String code = sc.nextLine();
        for (String aDiscount : SKERestaurant.discount) {
            System.out.println(aDiscount);
            if (code.equalsIgnoreCase(aDiscount.split(";")[0])) {
                return Double.parseDouble(aDiscount.split(";")[2].replaceAll(" ", ""));
            }
        }
        System.out.println("Coupon not found, Proceed to checkout");
        return 0;
    }

    /**
     * Print final bill and calculate net total price
     *
     * @param discountPercent is discount percent from coupon code
     * @return net total price
     */
    private static double printBill(double discountPercent) {
        double total = checkOrder();
        System.out.println();
        double discount;
        if (discountPercent != 0) {
            discount = total * discountPercent / 100;
            System.out.printf("%36s%2.0f%%%8s: %8.2f Baht%n", "", discountPercent, "Discount", discount);
            total -= discount;
        }
        System.out.printf("%41s%2d%%%3s: %8.2f Baht%n", "", 7, "Vat", total * 7 / 100);
        total += total * 7 / 100;
        System.out.printf("%47s: %8.2f Baht%n", "Net total", total);
        return total;
    }

    /**
     * Get integer reply with checking
     *
     * @param prompt message prompt
     * @return integer
     */
    private static int getIntReply(String prompt) {
        System.out.print(prompt + ": ");
        String input = sc.nextLine();
        if (input.charAt(0) < 48 || input.charAt(0) > 57) {
            System.out.println("Invalid input");
            return -1;
        } else return Integer.parseInt(input);
    }

}