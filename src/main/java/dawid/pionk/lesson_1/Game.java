package dawid.pionk.lesson_1;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Game {

    private static final int PLAYER_GUESS_MODE = 0;
    private static final int COMPUTER_GUESS_MODE = 1;
    private static final int COMPETE_GUESS_MODE = 1;
    Integer target, maxTarget, selectedMode;
    List<Player> players;

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
            for (Player player : this.players) {
                playerGuess = this.inputGuessToTry(player);
                checkResult = this.checkGuess(playerGuess);
                this.reactToCheckResult(checkResult, playerGuess, player);
            }
        }

        this.showResultsAndSave();
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
    }

    private void preparePlayer() throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.print("What is your name: ");
        String name = scan.nextLine();
        System.out.println("\nPreparing player");
        Player player = new Player(name);
        System.out.println("Best score: " + player.getScore());
        player.resetScore();
        player.adjustMinPossibleGuess(0);
        player.adjustMaxPossibleGuess(this.maxTarget);
        this.players.add(player);
    }

    private void prepareComputer() throws Exception {
        Player player = new Player("");
        player.adjustMinPossibleGuess(0);
        player.adjustMaxPossibleGuess(this.maxTarget);
        this.players.add(player);

    }

    private void prepareTarget() {
        if (this.selectedMode.equals(PLAYER_GUESS_MODE)) {
            this.target = PrepareTargetService.generateRandomTarget(this.maxTarget);
        } else {
            this.target = PrepareTargetService.softSpecifyTargetByUser(this.maxTarget);
        }
    }

    private Integer inputGuessToTry(Player player) throws Exception {
        if (this.selectedMode.equals(PLAYER_GUESS_MODE)) {
            return InputGuessService.inputPlayerGuess(this.maxTarget);
        } else {
            return InputGuessService.inputComputerGuess(player.getMaxPossibleGuess(), player.getMinPossibleGuess());
        }
    }

    private int checkGuess(Integer guess) {
        if (this.selectedMode.equals(PLAYER_GUESS_MODE)) {
            return CheckGuessService.letComputerDecide(this.target, guess);
        } else {
            return CheckGuessService.letPlayerDecide(guess);
        }
    }

    private void reactToCheckResult(Integer checkResult, Integer playerGuess, Player player) throws Exception {
        if (!checkResult.equals(CheckGuessService.GUESS_CORRECT)) {
            player.addScorePoint();
        }

        if (this.selectedMode.equals(COMPUTER_GUESS_MODE)) {
            switch (checkResult) {
                case CheckGuessService.GUESS_TOO_LOW:
                    player.adjustMinPossibleGuess(playerGuess + 1);
                    break;
                case CheckGuessService.GUESS_TOO_HIGH:
                    player.adjustMaxPossibleGuess(playerGuess - 1);
                    break;
            }
        }
    }

    private void showResultsAndSave() throws FileNotFoundException {
        for (Player player : this.players) {
            System.out.println("Player " + player.getName() + " of guesses: " + player.getScore());
            if (player.getLastScore() > player.getScore() || player.getLastScore().equals(0)) {
                player.save();
            }
        }
    }
}
