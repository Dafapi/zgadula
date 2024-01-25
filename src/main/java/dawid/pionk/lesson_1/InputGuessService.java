package dawid.pionk.lesson_1;

import java.util.Random;
import java.util.Scanner;

public class InputGuessService {
    public static Integer inputPlayerGuess(String playerName, Integer minTarget, Integer maxTarget) {
        Integer guess = null;
        Scanner scan = new Scanner(System.in);

        while (guess == null) {
            System.out.print("[" + playerName + "] What is your guess (" + minTarget + " - " + maxTarget + "): ");
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

        Integer guess = minGuess + (new Random()).nextInt(maxGuess - minGuess);
        System.out.println("Computer guess: " + guess);

        return guess;
    }
}
