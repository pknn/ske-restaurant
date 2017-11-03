import java.io.*;
import java.util.Scanner;

/**
 * RestaurantManager is a manager class for SKERestaurant
 * Contain helper method such as collect menu from a file,
 * record menuOrder log in a file, save data for menu
 *
 * @author Pakanon Pantisawat
 */
class RestaurantManager {
    private static String[] menuItems;
    private static double[] menuPrices;
    private static String[] discountItems;
    private static int itemsCount;

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
     * Count menu in menuFile and return it as integer
     *
     * @param fileName fileName of menuFile
     * @return total number of menu in file
     */
    private static int getMenuItemsCount(String fileName) {
        int lineCount = 0;
        InputStream is = getInputStream(fileName);
        if (is == null) {
            System.err.printf("File %s was not found%n", fileName);
            return 0;
        }

        Scanner fileScanner = new Scanner(is);
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            if (line.startsWith("#") || line.equals("")) continue;
            lineCount++;
        }
        fileScanner.close();

        return lineCount;
    }

    /**
     * Read menu data and store it in array
     *
     * @param fileName
     */
    static void setMenu(String fileName) {
        InputStream is = getInputStream(fileName);
        if (is == null) {
            System.err.printf("File %s was not found%n", fileName);
            return;
        }

        itemsCount = getMenuItemsCount(fileName);
        menuItems = new String[itemsCount];
        menuPrices = new double[itemsCount];

        Scanner fileScanner = new Scanner(is);
        int menuIndex = 0;
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            if (line.startsWith("#") || line.equals("")) continue;
            menuItems[menuIndex] = line.split(";")[0];
            menuPrices[menuIndex] = Double.parseDouble(line.split(";")[1].replaceAll(" ", ""));
            menuIndex++;
        }
        fileScanner.close();
    }

    static String[] getMenuItems() {
        return menuItems;
    }

    static double[] getMenuPrices() {
        return menuPrices;
    }

    /**
     * Get items count and allocate memory for menuItems and menuPrices,
     * then call setMenu() for read menu from menuFile
     */
    static void init() {
        String menuFilePath = "data/menuFile.txt";
        setMenu(menuFilePath);
    }

    /**
     * Record menuOrder log to text file for further usage
     *
     * @param orderNumber menuOrder number of menuOrder
     * @param order       array of menuOrder listing
     * @param total       total price for menuOrder
     */
    public void recordOrder(int orderNumber, int[] order, double total) {
        String orderFilePath = "data/orderLog.txt";
        try {
            FileWriter fw = new FileWriter(orderFilePath, true);
            PrintWriter pw = new PrintWriter(fw);

            pw.printf("Order Number: %05d%n", orderNumber);
            for (int anOrder : order) {
                pw.printf("%010d%n", anOrder);
            }
            pw.printf("Total : %.2f Baht%n", total);
            pw.println();
            pw.close();
        } catch (IOException ioe) {
            System.err.printf("Cannot access file %s", orderFilePath);
        }
    }
}