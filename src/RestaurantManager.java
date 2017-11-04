import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * RestaurantManager is a manager class for SKERestaurant
 * Contain helper method such as collect menu from a file,
 * record menuOrder log in a file, save data for menu
 *
 * @author Pakanon Pantisawat
 */
class RestaurantManager {
    private final static Scanner sc = new Scanner(System.in);
    private static List<String> menuItems = new ArrayList<>();
    private static List<Double> menuPrices = new ArrayList<>();
    private static List<String> promotionItems = new ArrayList<>();
    private static int orderNum;

    private RestaurantManager() {
        init();
    }

    /**
     * Get InputStream and return if file is exist
     *
     * @param menuFile is a relative file path
     * @return InputStream if file was found or none
     */
    private static InputStream getInputStream(String menuFile) {
        ClassLoader cl = RestaurantManager.class.getClassLoader();
        return cl.getResourceAsStream(menuFile);
    }

    /**
     * Read promotion data and store it in array
     */
    private static void setPromotion() {
        String fileName = "data/promotion.txt";
        InputStream is = getInputStream(fileName);
        if (is == null) {
            System.err.printf("File %s was not found%n", fileName);
            return;
        }

        Scanner fileScanner = new Scanner(is);
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            if (line.startsWith("#") || line.equals("")) continue;
            promotionItems.add(line);
        }
        fileScanner.close();
    }

    /**
     * Read menu data and store it in array
     *
     * @param fileName is menu file path
     */
    static void setMenu(String fileName) {
        InputStream is = getInputStream(fileName);
        if (is == null) {
            System.err.printf("File %s was not found%n", fileName);
            return;
        }

        Scanner fileScanner = new Scanner(is);
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            if (line.startsWith("#") || line.equals("")) continue;
            menuItems.add(line.split(";")[0]);
            menuPrices.add(Double.parseDouble(line.split(";")[1].replaceAll(" ", "")));
        }
        fileScanner.close();
    }

    static String[] getMenuItems() {
        return menuItems.toArray(new String[menuItems.size()]);
    }

    static double[] getMenuPrices() {
        double[] menuPrice = new double[menuPrices.size()];
        for (int i = 0; i < menuPrices.size(); i++) {
            menuPrice[i] = menuPrices.get(i);
        }
        return menuPrice;
    }

    static String[] getPromotionItems() {
        return promotionItems.toArray(new String[promotionItems.size()]);
    }

    static int getOrderNum() {
        return orderNum;
    }

    /**
     * Get items count and allocate memory for menuItems and menuPrices,
     * then call setMenu() for read menu from menuFile
     */
    static void init() {
        String menuFilePath = "data/menuFile.txt";
        String orderLogFilePath = "data/orderLog.txt";
        setMenu(menuFilePath);
        setPromotion();
        getLatestOrderNum(orderLogFilePath);
    }

    /**
     * Record menuOrder log to text file for further usage
     *
     * @param orderNumber menuOrder number of menuOrder
     * @param order       array of menuOrder listing
     * @param total       total price for menuOrder
     */
    static void recordOrder(int orderNumber, int[] order, double total) {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String orderFilePath = s + "/src/data/orderLog.txt";
        try {
            FileWriter fw = new FileWriter(orderFilePath, true);
            PrintWriter pw = new PrintWriter(fw);

            pw.printf("Order Number: %05d%n", orderNumber);
            pw.println();
            for (int i = 0; i < order.length; i++) {
                if (order[i] != 0)
                    pw.printf("%-20s %3d Piece(s) : %6.2f Baht%n", menuItems.get(i), order[i], order[i] * menuPrices.get(i));
            }
            pw.printf("%24sNet Total : %.2f Baht%n", "", total);
            pw.println();
            pw.close();
        } catch (IOException ioe) {
            System.err.printf("Cannot access file %s", orderFilePath);
        }
    }

    /**
     * Get latest order number from log file
     *
     * @param orderFilePath order log file path
     */
    private static void getLatestOrderNum(String orderFilePath) {
        InputStream is = getInputStream(orderFilePath);
        if (is == null) {
            orderNum = 1;
            return;
        }
        Scanner fs = new Scanner(is);
        while (fs.hasNextLine()) {
            String line = fs.nextLine();
            if (line.startsWith("Order Number:")) {
                orderNum = Integer.parseInt(line.split(":")[1].replaceAll(" ", "")) + 1;
            }
        }
    }

    /**
     * Manage menu for manager
     */
    static void manage() {
        while (true) {
            System.out.println();
            System.out.println("\"a\"  Add menu");
            System.out.println("\"p\"  Add promotion");
            System.out.println("\"x\"  Exit");
            System.out.print("cmd> ");
            Scanner sc = new Scanner(System.in);
            String s = sc.nextLine();
            switch (s) {
                case "x":
                    return;
                case "a":
                    addMenu();
                    break;
                case "p":
                    addPromotion();
                    break;
            }
        }
    }

    /**
     * Add new menu to file and list
     */
    private static void addMenu() {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String menuPath = s + "/src/data/menuFile.txt";

        try {
            FileWriter fw = new FileWriter(menuPath, true);
            PrintWriter pw = new PrintWriter(fw);

            System.out.print("Menu title: ");
            String title = sc.nextLine();
            for (String item : menuItems) {
                if (item.equalsIgnoreCase(title)) {
                    System.out.println("Item already existed");
                    return;
                }
            }

            System.out.print("Menu price: ");
            String pricePut = sc.nextLine();
            if (pricePut.charAt(0) < 48 || pricePut.charAt(0) > 57) {
                System.out.println("Invalid input");
                return;
            }
            double price = Double.parseDouble(pricePut);

            menuItems.add(title);
            menuPrices.add(price);

            pw.printf("%s;%12.1f%n", title, price);
            pw.close();
        } catch (IOException n) {
            System.out.println("Cannot access file");
        }
    }

    /**
     * Add new promotion to file and list
     */
    private static void addPromotion() {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String menuPath = s + "/src/data/promotion.txt";

        try {
            FileWriter fw = new FileWriter(menuPath, true);
            PrintWriter pw = new PrintWriter(fw);

            System.out.print("Promotion title: ");
            String title = sc.nextLine();
            for (String item : promotionItems) {
                if (item.contains(title)) {
                    System.out.println("Item already existed");
                    return;
                }
            }

            System.out.println("Promotion code: ");
            String code = sc.nextLine();
            for (String item : promotionItems) {
                if (item.contains(code)) {
                    System.out.println("Code already existed");
                    return;
                }
            }

            System.out.print("Promotion discount: ");
            String pricePut = sc.nextLine();
            if (pricePut.charAt(0) < 48 || pricePut.charAt(0) > 57) {
                System.out.println("Invalid input");
                return;
            }
            double discount = Double.parseDouble(pricePut);

            promotionItems.add(String.format("%s; %s; %.2f%n", code, title, discount));

            pw.printf("%s; %s; %.2f%n", code, title, discount);
            pw.close();
        } catch (IOException e) {
            System.out.println("Cannot access file");
        }
    }

}