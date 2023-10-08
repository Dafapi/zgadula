package dawid.pionk.lesson_1;

import java.util.Random;
import java.util.Scanner;

public class PrepareTargetService {
    public static Integer softSpecifyTargetByUser(Integer maxTarget) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Think about your target number (0-" + maxTarget + ") and press Enter");
        scan.nextLine();
        return null;
    }

    public static Integer specifyTargetByUser(Integer maxTarget) {
        Scanner scan = new Scanner(System.in);
        System.out.print("What is computer target (0-" + maxTarget + "): ");
        Integer target = null;
        while (target == null) {
            try {
                target = Integer.parseInt(scan.nextLine());
                if (target > maxTarget || target < 0) {
                    System.out.println("\nInvalid number range");
                } else {
                    return target;
                }
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid number format");
            }
        }

        return null;
    }

    public static Integer generateRandomTarget(Integer maxTarget) {
        return (new Random()).nextInt(maxTarget + 1);
    }
}
