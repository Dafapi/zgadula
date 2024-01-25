package dawid.pionk.lesson_1;

import java.util.Random;
import java.util.Scanner;

public class PrepareTargetService {
    public static Integer softSpecifyTargetByUser(Integer maxTarget) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Think about your target number (0-" + maxTarget + ")");
        System.out.println("Press Enter when ready to play");
        scan.nextLine();
        return null;
    }

    public static Integer generateRandomTarget(Integer minTarget, Integer maxTarget) {
        return minTarget + (new Random()).nextInt(maxTarget + 1 - minTarget);
    }
}
