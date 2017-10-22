import java.io.*;
import java.util.*;

class RestaurantManager {

    private static Scanner input = new Scanner(System.in);

    private List<String> menuName = new ArrayList<>();
    private List<String> order = new ArrayList<>();
    private List<Double> menuPrice = new ArrayList<>();
    private int[] menuQuantity;

    public void recordOrder(int orderNumber, String[] order, double total) throws IOException {
        String logFilePath = "data/orderLog.log";
        OutputStream os = null;
        try {
            os = new FileOutputStream(logFilePath);
        } catch (FileNotFoundException ex) {
            System.out.println("Couldn't open output file " + logFilePath);
            return;
        }
        PrintStream ps = new PrintStream(os);
        ps.println("Order number: " + orderNumber);
        for (String anOrder : order) {
            System.out.println(anOrder);
        }
        ps.println("Total: " + total);
        ps.close();
    }

    private void setMenu() {
        String menuPath = "data/menuFile.txt";
        setMenu(menuPath);
    }

    private void setMenu(String fileName) {
        ClassLoader cl = RestaurantManager.class.getClassLoader();
        InputStream fi = cl.getResourceAsStream(fileName);
        Scanner menuScanner = new Scanner(fi);
        int count = 0;
        while (menuScanner.hasNextLine()) {
            String line = menuScanner.nextLine();
            if (line.equals("") || line.startsWith("#")) {
                continue;
            }
            count += 1;
            menuName.add(line.split(";")[0]);
            menuPrice.add(Double.valueOf(line.split(";")[1].replaceAll(" ", "")));
        }
        menuQuantity = new int[count];
        menuScanner.close();
    }

    void init() throws IOException {
        setMenu();
        String logFilePath = "data/orderLog.log";
        try {
            OutputStream os = new FileOutputStream(logFilePath);
        } catch (IOException e) {
            System.out.println("Log File could not be found");
        }
    }

    /**
     * Check if there is "menuFile.txt" existing in the directory
     * and put all menu name and price in the array list.
     * If it doesn't exist, the method will ask user if they want to create new menu
     */
    public String[] getMenuItems() {
        String[] name = new String[menuName.size()];
        menuName.toArray(name);
        return name;
    }

    public double[] getPrices() {
        double[] price = new double[menuPrice.size()];
        for (int i = 0; i < menuPrice.size(); i++) {
            price[i] = menuPrice.get(i);
        }
        return price;
    }

    /**
     * Method for print entire menu from the list
     */
    private void printMenu() {
        // print menu
        for (int i = 0; i < menuName.size(); i++) {
            System.out.printf("%2d. %-20s -- %.2f Baht%n", i + 1, menuName.get(i), menuPrice.get(i));
        }
    }

    /**
     * Print the menu of food and drinks,
     * order by put order code or menu name following by / and quantity
     * check for valid input then add to the menu
     */
    void orderFood() {
        printMenu();
        System.out.println("How to order: (menu name / code)(/)(quantity)");
        System.out.print("What do you want to order: ");
        String[] order = input.nextLine().split("/");

        // check if quantity is integer
        if (order[1].charAt(0) > 57) System.out.println("Quantity is invalid");

        // check if user input menu name or order code
        if (order[0].charAt(0) > 57) {
            // check if menu name is exist
            if (menuName.indexOf(order[0]) != -1) {
                menuQuantity[menuName.indexOf(order[0])] += Integer.valueOf(order[1]);
                System.out.printf("%s added for %s%n", order[0], order[1]);
            } else {
                System.out.println("Menu name is invalid");
            }
        } else {
            int orderCode = Integer.valueOf(order[0]) - 1;

            // check if order code is valid
            if (orderCode + 1 > menuQuantity.length || orderCode < 0) System.out.println("Order code is invalid");
            else {
                menuQuantity[orderCode] += Integer.valueOf(order[1]);
                System.out.printf("%s added for %s%n", menuName.get(orderCode), order[1]);
            }
        }
    }


    /**
     * Simply print current bill and check if user want to check out
     *
     * @param isCheckOut is true if user want to check out
     */
    void checkOrder(boolean isCheckOut) throws IOException {
        double totalPrice = 0.00;

        System.out.println();
        System.out.println("Latest bill is: ");
        System.out.println("----------------------------------------------");
        System.out.printf("::%3s %-20s :: %3s :: %6s ::%n", "No.", "Menu", "Qt", "Price");
        System.out.println("----------------------------------------------");

        for (int i = 0, j = 1; i < menuName.size(); i++) {
            if (menuQuantity[i] != 0) {
                double eachNetPrice = menuPrice.get(i) * menuQuantity[i];
                totalPrice += eachNetPrice;
                System.out.printf("::%2d. %-20s :: %3d :: %6.2f ::%n", j, menuName.get(i), menuQuantity[i], eachNetPrice);
                j++;
                order.add(menuName.get(i));
            }
        }

        if (isCheckOut) {
            double discount = totalPrice * getDiscount() / 100;
            System.out.printf("::%-31s :: -%5.2f ::%n", "Discount", discount);
            totalPrice -= discount;
            String[] theOrder = new String[order.size()];
            order.toArray(theOrder);
            Random rand = new Random();
            int orderNum = rand.nextInt(10000) + 1;
            recordOrder(orderNum, theOrder, totalPrice);
        }

        System.out.println("----------------------------------------------");
        System.out.printf("::%31s :: %6.2f ::%n", "Total", totalPrice);
        System.out.println("----------------------------------------------");
        System.out.println();
    }

    /**
     * Check if user want to use coupon
     * and check if coupon is valid
     *
     * @return random value from (5, 10, 15) for discount percent
     */
    private double getDiscount() {
        Random rand = new Random();
        System.out.print("Do you have any Coupon (y/n) ** One coupon per bill** : ");
        if (input.next().toLowerCase().charAt(0) == 'y') {
            System.out.println("Coupon code must starts with SKE and following by 8 digit : Example (SKE01930024)");
            while (true) {
                System.out.print("Input coupon code: ");
                String couponCode = input.nextLine();
                if (couponCode.startsWith("SKE") && couponCode.length() == 11) {
                    return (rand.nextInt(3) + 1) * 5;
                } else System.out.println("Invalid input");
            }
        }
        return 0;
    }

}
