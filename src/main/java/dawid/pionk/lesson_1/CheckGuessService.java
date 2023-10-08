package dawid.pionk.lesson_1;

import java.util.Random;
import java.util.Scanner;

public class CheckGuessService {

    public static final int GUESS_CORRECT = 0;
    public static final int GUESS_TOO_HIGH = 1;
    public static final int GUESS_TOO_LOW = 2;

    public static int letComputerDecide(Integer expectedTarget, Integer playerGuess) {
        return CheckGuessService.checkGuess(playerGuess, expectedTarget);
    }

    public static int letPlayerDecide(Integer playerGuess) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("Player guess: " + playerGuess);

            System.out.print("Is it good? (too [l]ow / too [h]igh / [c]orrect): ");
            String isGuessCorrect = scan.nextLine();

            switch (isGuessCorrect) {
                case "c":
                    System.out.println("Yeey,you made it");
                    return CheckGuessService.GUESS_CORRECT;
                case "l":
                    return CheckGuessService.GUESS_TOO_LOW;
                case "h":
                    return CheckGuessService.GUESS_TOO_HIGH;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private static int checkGuess(Integer guess, Integer expectedTarget) {
        if (guess.equals(expectedTarget)) {
            System.out.println("Yeey,you made it");
            return CheckGuessService.GUESS_CORRECT;
        } else if (guess < expectedTarget) {
            System.out.println("Too low");
            return CheckGuessService.GUESS_TOO_LOW;
        } else {
            System.out.println("Too high");
            return CheckGuessService.GUESS_TOO_HIGH;
        }
    }
}
