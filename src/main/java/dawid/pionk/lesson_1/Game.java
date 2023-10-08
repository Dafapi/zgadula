package dawid.pionk.lesson_1;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Game {

    private static final int PLAYER_GUESS_MODE = 0;
    private static final int COMPUTER_GUESS_MODE = 1;
    private static final int COMPETE_GUESS_MODE = 1;
    Integer target, maxTarget, lastPlayerScore, selectedMode;
    Player player;

    public Game() {
        this.maxTarget = 100;
    }

    public Game(Integer maxTarget) {
        this.maxTarget = maxTarget;
    }


    public void start() throws Exception {
        this.choseGameMode();
        this.prepareEnvironment();
        this.prepareTarget();

        Integer playerGuess, checkResult = CheckGuessService.GUESS_TOO_LOW;
        while (!checkResult.equals(CheckGuessService.GUESS_CORRECT)) {
            playerGuess = this.inputGuessToTry();
            checkResult = this.checkGuess(playerGuess);
            this.reactToCheckResult(checkResult, playerGuess);
        }

        System.out.println("Number of guesses: " + this.player.getScore());
        if (this.lastPlayerScore > this.player.getScore() || this.lastPlayerScore.equals(0)) {
            player.save();
        }
    }

    private void choseGameMode() {
        Scanner scan = new Scanner(System.in);
        while (this.selectedMode == null) {
            System.out.println("\nGame modes");
            System.out.println("1. You are guess generated number");
            System.out.println("2. Computer guess what you type");
            System.out.println("3. Compete against computer who guess first");

            System.out.print("Select: ");
            String selectedMode = scan.nextLine();
            switch (selectedMode) {
                case "1":
                    this.selectedMode = PLAYER_GUESS_MODE;
                    break;
                case "2":
                    this.selectedMode = COMPUTER_GUESS_MODE;
                    break;
                case "3":
                    this.selectedMode = COMPETE_GUESS_MODE;
                    break;
            }

            System.out.println();
        }
    }

    private void prepareEnvironment() throws Exception {
        if (this.selectedMode.equals(PLAYER_GUESS_MODE)) {
            this.preparePlayer();
        } else {
            this.prepareComputer();
        }

        this.player.adjustMinPossibleGuess(0);
        this.player.adjustMaxPossibleGuess(this.maxTarget);
    }

    private void preparePlayer() throws FileNotFoundException {
        Scanner scan = new Scanner(System.in);
        System.out.print("What is your name: ");
        String name = scan.nextLine();
        System.out.println("\nPreparing player");
        this.player = new Player(name);
        System.out.println("Best score: " + this.player.getScore());
        this.lastPlayerScore = this.player.getScore();
        this.player.resetScore();
    }

    private void prepareComputer() throws FileNotFoundException {
        this.player = new Player("");
    }

    private void prepareTarget() {
        if (this.selectedMode.equals(PLAYER_GUESS_MODE)) {
            this.target = PrepareTargetService.generateRandomTarget(this.maxTarget);
        } else {
            this.target = PrepareTargetService.softSpecifyTargetByUser(this.maxTarget);
        }
    }

    private Integer inputGuessToTry() throws Exception {
        if (this.selectedMode.equals(PLAYER_GUESS_MODE)) {
            return InputGuessService.inputPlayerGuess(this.maxTarget);
        } else {
            return InputGuessService.inputComputerGuess(this.player.getMaxPossibleGuess(), this.player.getMinPossibleGuess());
        }
    }

    private int checkGuess(Integer guess) {
        if (this.selectedMode.equals(PLAYER_GUESS_MODE)) {
            return CheckGuessService.letComputerDecide(this.target, guess);
        } else {
            return CheckGuessService.letPlayerDecide(guess);
        }
    }

    private void reactToCheckResult(Integer checkResult, Integer playerGuess) throws Exception {
        if (!checkResult.equals(CheckGuessService.GUESS_CORRECT)) {
            this.player.addScorePoint();
        }

        if (this.selectedMode.equals(COMPUTER_GUESS_MODE)) {
            switch (checkResult) {
                case CheckGuessService.GUESS_TOO_LOW:
                    this.player.adjustMinPossibleGuess(playerGuess + 1);
                    break;
                case CheckGuessService.GUESS_TOO_HIGH:
                    this.player.adjustMaxPossibleGuess(playerGuess - 1);
                    break;
            }
        }
    }
}
