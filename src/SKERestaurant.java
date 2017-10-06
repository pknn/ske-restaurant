import java.util.Scanner;

public class SKERestaurant {
    private static final int PIZZA = 1, CHICKEN = 2, COKE = 3;
    private static final int QUANTITY = 0, PRICE = 1;
    static Scanner in = new Scanner(System.in);

    // just a function for display menu
    private static void menuShow() {
        int menuInput, quantityInput;
        int[][] menu = new int[3][2];
        boolean[] coupon = new boolean[3];
        System.out.println("--------- Welcome to SKE Restaurant ---------");
        System.out.println("1.) Pizza            250 Baht.");
        System.out.println("2.) Chicken          120 Baht.");
        System.out.println("3.) Coke             45 Baht.");
        System.out.println("4.) Coupon Using");
        System.out.println("5.) Total");
        System.out.println("6.) Check and Pay");
        System.out.println("7.) Exit");

        boolean isProgramFinished = false;
        while (!isProgramFinished) {
            System.out.println();
            System.out.print("Enter your Choice: ");
            menuInput = in.nextInt();
            if (isValidInput(menuInput)) {
                switch (menuInput) {
                    case PIZZA:
                    case CHICKEN:
                    case COKE:
                        System.out.print("Enter Quantity: ");
                        quantityInput = in.nextInt();
                        menu[menuInput - 1][QUANTITY] += quantityInput;
                        menu[menuInput - 1][PRICE] += addOrder(menuInput, quantityInput);
                        break;
                    case 4:
                        coupon = couponUsing();
                        break;
                    case 5:
                        calculateTip(totalShow(menu, coupon, false));
                        break;
                    case 6:
                        isProgramFinished = true;
                        calculateTip(totalShow(menu, coupon, true));
                        System.out.println("Hope to serve you again!");
                        break;
                    case 7:
                        System.out.println("Bye bye!");
                        isProgramFinished = true;
                        break;
                }
            }
        }

    }

    // check if input is between 1 - 8
    private static boolean isValidInput(int input) {
        return input > 0 && input < 8;
    }

    // calculate the price of each menu
    private static int calculatePrice(int quantity, int price) {
        return quantity * price;
    }

    // add order to each type
    private static int addOrder(int menu, int quantity) {
        int price;
        switch (menu) {
            case PIZZA:
                price = 250;
                break;
            case CHICKEN:
                price = 120;
                break;
            case COKE:
                price = 45;
                break;
            default:
                price = 0;
                break;
        }
        price = calculatePrice(quantity, price);
        return price;
    }

    // check if user use coupon or not
    private static boolean[] couponUsing() {
        boolean[] coupon = new boolean[3];
        String couponCode;
        String holder = "@";

        System.out.println("Enter coupon code (put @ sign for exit):");
        System.out.println("Ex. Children discount code:   CHXXXXXXX");
        System.out.println("    Birthday discount code:   HBXXXXXXX");
        System.out.println("    Mother day discount code: MDXXXXXXX");
        System.out.println("PS. Each type coupon and each code can be used once.");
        System.out.println("    If more than 1 coupon of each type has been used, discount will be count as 1 coupon.");
        /*You able to use CH, HB, MD following by any number or character sequence.
        * But number or character sequence are not allowed to be the same
        * additional using of each type will count as 1 usage.
        * !!! COUPON CODE ARE NOT CASE-SENSITIVE !!!
        * Any other prefix than CH, HB, MD will be rejected.
        * */

        while (true) {
            couponCode = in.next().toLowerCase();
            if (!couponCode.contains(holder)) {
                if (couponCode.startsWith("ch")) coupon[0] = true;
                else if (couponCode.startsWith("hb")) coupon[1] = true;
                else if (couponCode.startsWith("md")) coupon[2] = true;
                else if (couponCode.contains("@")) break;
                else System.out.println("Invalid Input");
                holder = couponCode.substring(1);
            } else {
                System.out.println("This Coupon has been used");
            }
        }
        return coupon;
    }

    // sum all coupon discount
    private static int discountCheck(int[][] menu, boolean[] coupon, int totalPrice) {
        int totalDiscount = 0;
        if (coupon[0]) {
            int discount = -(int) Math.floor(0.2 * totalPrice);
            System.out.printf("|  %-32s|%10d     |%n", "Children Discount 20%", discount);
            totalDiscount += discount;
        }
        if (coupon[1]) {
            int discount = -menu[2][1];
            System.out.printf("|  %-32s|%10d     |%n", "Birthday Free drink", discount);
            totalDiscount += discount;
        }
        if (coupon[2]) {
            int discount;
            if (menu[1][0] >= 2) {
                discount = -240;
            } else {
                discount = -menu[1][1];
            }
            System.out.printf("|  %-32s|%10d     |%n", "Mother day free 2 chicken", discount);
            totalDiscount += discount;
        }
        System.out.printf("|  %-32s|%10d     |%n", "Total Discount", totalDiscount);
        return totalDiscount;
    }

    // display the total
    private static int totalShow(int[][] menu, boolean[] coupon, boolean isCheckOut) {
        String name = "";
        int totalPrice = 0;
        System.out.println("+-----------MENU-----------+--QTY--+-----PRICE-----+");
        for (int i = 0; i < 3; i++) {
            if (menu[i][QUANTITY] != 0) {
                switch (i) {
                    case 0:
                        name = "Pizza";
                        break;
                    case 1:
                        name = "Chicken";
                        break;
                    case 2:
                        name = "Coke";
                        break;
                }
                System.out.printf("|  %-24s|%5d  |%9d      |%n", name, menu[i][0], menu[i][1]);
                totalPrice += menu[i][1];
            }
            if (i == 2) {
                System.out.println("+--------------------------------------------------+");
            }
        }
        totalPrice += discountCheck(menu, coupon, totalPrice);
        System.out.println("+--------------------------------------------------+");
        System.out.printf("|  %-32s|%10d     |%n", "Total", totalPrice);
        System.out.println("+--------------------------------------------------+");
        if (isCheckOut) sharingOption(totalPrice);
        return totalPrice;
    }

    // check if user want to split payment
    private static void sharingOption(int totalPrice) {
        System.out.println();
        char input;
        while (true) {
            System.out.print("Do you want to sharing (y/n): ");
            input = in.next().charAt(0);
            if (input == 'y' || input == 'n') break;
        }

        if (input == 'y') {
            System.out.print("How many people: ");
            int count = in.nextInt();
            System.out.printf("Price per each will be %.2f Baht%n", totalPrice / (double) count);
        } else {
            System.out.printf("You'll have to pay %d Baht%n", totalPrice);
        }
    }

    // calculate the tip for waiter
    private static void calculateTip(int totalPrice) {
        double percentTip;
        System.out.print("How much you want to tip in percent: ");
        while (true) {
            percentTip = in.nextDouble();
            if (percentTip < 100) break;
        }
        System.out.printf("You will have to tip %.2f Baht.", totalPrice * percentTip / 100);
    }

    public static void main(String[] args) {
        menuShow();
    }
}
