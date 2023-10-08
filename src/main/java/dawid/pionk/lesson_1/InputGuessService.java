package dawid.pionk.lesson_1;

import java.util.Random;
import java.util.Scanner;

public class InputGuessService {
    public static Integer inputPlayerGuess(Integer maxTarget) {
        Integer guess = null;
        Scanner scan = new Scanner(System.in);

        while (guess == null) {
            System.out.print("What is your guess (0-" + maxTarget + "): ");
            try {
                guess = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format");
            }
        }

        return guess;
    }

    public static Integer inputComputerGuess(Integer maxGuess, Integer minGuess) throws Exception {
        if (maxGuess < minGuess) {
            throw new Exception("There is no possible number to guess");
        } else if (maxGuess.equals(minGuess)) {
            return minGuess;
        }

        return minGuess + (new Random()).nextInt(maxGuess - minGuess);
    }
}
