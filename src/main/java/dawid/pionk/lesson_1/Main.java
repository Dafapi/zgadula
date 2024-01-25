package dawid.pionk.lesson_1;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        String answer = "y";
        while (answer.equals("y")) {
            Game game = new Game();
            game.start();

            System.out.print("Do you want to play another game? [y]es: ");
            answer = scan.nextLine();
        }
    }
}
