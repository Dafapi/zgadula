package dawid.pionk.lesson_1;

import java.io.FileNotFoundException;
import java.util.*;

public class Game {

    private static final int PLAYER_GUESS_MODE = 0;
    private static final int COMPUTER_GUESS_MODE = 1;
    private static final int COMPETE_GUESS_MODE = 2;
    private static final int CUSTOM_MODE = 3;
    private static final int RANKED_MODE = 4;

    Integer target, minTarget, maxTarget, selectedMode;
    List<Player> players;

    public void start() throws Exception {
        this.choseGameMode();
        this.prepareEnvironment();
        this.prepareTarget();

        Player winner = null;
        Integer playerGuess, checkResult = CheckGuessService.GUESS_TOO_LOW;
        while (winner == null) {
            for (Player player : this.players) {
                playerGuess = this.inputGuessToTry(player);
                checkResult = this.checkGuess(playerGuess);
                this.reactToCheckResult(checkResult, playerGuess, player);

                if (checkResult.equals(CheckGuessService.GUESS_CORRECT)) {
                    winner = player;
                    break;
                }
            }
        }

        this.showResultsAndSave(winner);
    }

    private void choseGameMode() {
        Scanner scan = new Scanner(System.in);
        while (this.selectedMode == null) {
            System.out.println("\nGame modes");
            System.out.println("1. You guess generated number");
            System.out.println("2. Computer guess what you think");
            System.out.println("3. Compete against computer who will guess first");
            System.out.println("4. Custom settings");
            System.out.println("5. Ranked games");

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
                case "4":
                    this.selectedMode = CUSTOM_MODE;
                    break;
                case "5":
                    this.selectedMode = RANKED_MODE;
                    break;
            }

            System.out.println();
        }

        while (this.maxTarget == null) {
            System.out.println("\nGame difficulty");
            System.out.println("1. Easy (1 - 100)");
            System.out.println("2. Hard (1 - 1 000 000)");
            System.out.println("3. Custom");

            System.out.print("Select: ");
            String selectedMode = scan.nextLine();
            switch (selectedMode) {
                case "1":
                    this.minTarget = 0;
                    this.maxTarget = 100;
                    break;
                case "2":
                    this.minTarget = 0;
                    this.maxTarget = 1000000;
                    break;
                case "3":
                    this.prepareCustomTargetRange();
                    break;
            }

            System.out.println();
        }
    }

    private void prepareCustomTargetRange() {
        Scanner scan = new Scanner(System.in);
        Integer proposition;

        while (this.minTarget == null) {
            System.out.print("What is your minimal target: ");
            try {
                this.minTarget = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format");
            }
        }

        while (this.maxTarget == null) {
            System.out.print("What is your maximal target: ");
            try {
                proposition = Integer.parseInt(scan.nextLine());
                if (proposition <= this.minTarget) {
                    System.out.println("Proposed number must be higher than minimal target: " + this.minTarget.toString());
                } else {
                    this.maxTarget = proposition;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format");
            }
        }
    }

    private void prepareEnvironment() throws Exception {
        this.players = new ArrayList<>();

        switch (this.selectedMode) {
            case PLAYER_GUESS_MODE:
                while (true) {
                    try {
                        this.preparePlayer();
                        break;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                break;

            case COMPUTER_GUESS_MODE:
                this.prepareComputer();
                break;

            case COMPETE_GUESS_MODE:
                while (true) {
                    try {
                        this.preparePlayer();
                        break;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                this.prepareComputer();
                this.tossCoinWhoStart();
                break;

            case CUSTOM_MODE:
                this.prepareCustomPlayers();

                Scanner scan = new Scanner(System.in);
                boolean invalidAnswer = true;
                while (invalidAnswer) {
                    System.out.print("Do you want to play with computer? [y]es/[n]o : ");
                    String answer = scan.nextLine();
                    switch (answer) {
                        case "y":
                            this.prepareComputer();
                            invalidAnswer = false;
                            break;
                        case "n":
                            invalidAnswer = false;
                            break;
                    }
                }

                this.tossCoinWhoStart();
                break;

            case RANKED_MODE:
                this.prepareCustomPlayers();
                this.tossCoinWhoStart();
                break;
        }

        System.out.println();
        System.out.println();
    }

    private void prepareCustomPlayers() throws Exception {
        Scanner scan = new Scanner(System.in);

        Integer humanPlayersNumber = null;
        while (humanPlayersNumber == null) {
            System.out.print("How many human players do you want? : ");
            try {
                humanPlayersNumber = Integer.parseInt(scan.nextLine());
                if (humanPlayersNumber < 0) {
                    System.out.print("Number must be positive");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format");
            }
        }

        while (this.players.size() < humanPlayersNumber) {
            try {
                this.preparePlayer();
            } catch (Exception e) {
                System.out.println(e.getClass());
                System.out.println(e.getMessage());
            }
        }
    }

    private void preparePlayer() throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.print("What is your name: ");
        String name = scan.nextLine();

        if (name.isEmpty() || name.contains("<") || name.contains(">1")) {
            throw new Exception("Invalid player name");
        }

        for (Player player : this.players) {
            if (player.getName().equals(name)) {
                throw new Exception("This player already exists");
            }
        }

        System.out.println("\nPreparing player");
        Player player = new Player(name, this.getGameMetadata());
        System.out.println("Best score: " + player.getLastBestScore());

        if (this.selectedMode == COMPETE_GUESS_MODE) {
            System.out.println("No of losses: " + player.getLosesNo());
        }

        this.players.add(player);
    }

    private void prepareComputer() throws Exception {
        Player player = new ComputerPlayer(this.getGameMetadata());

        System.out.println("\nPreparing computer");
        System.out.println("Best score: " + player.getLastBestScore());

        if (this.selectedMode == COMPETE_GUESS_MODE) {
            System.out.println("No of losses: " + player.getLosesNo());
        }

        player.adjustMinPossibleGuess(this.minTarget);
        player.adjustMaxPossibleGuess(this.maxTarget);
        this.players.add(player);
    }

    private String getGameMetadata() {
        return this.selectedMode.toString();
    }

    private void tossCoinWhoStart() {
        Collections.shuffle(this.players);
        System.out.println();
        System.out.println("First turn belongs to: " + this.players.get(0).getName());
    }

    private void prepareTarget() {
        if (this.selectedMode.equals(COMPUTER_GUESS_MODE)) {
            this.target = PrepareTargetService.softSpecifyTargetByUser(this.maxTarget);
        } else {
            this.target = PrepareTargetService.generateRandomTarget(this.minTarget, this.maxTarget);
        }
    }

    private Integer inputGuessToTry(Player player) throws Exception {
        if (player instanceof ComputerPlayer) {
            return InputGuessService.inputComputerGuess(player.getMaxPossibleGuess(), player.getMinPossibleGuess());
        } else {
            return InputGuessService.inputPlayerGuess(player.getName(), this.minTarget, this.maxTarget);
        }
    }

    private int checkGuess(Integer guess) {
        if (this.selectedMode.equals(COMPUTER_GUESS_MODE)) {
            return CheckGuessService.letPlayerDecide();
        } else {
            return CheckGuessService.letComputerDecide(this.target, guess);
        }
    }

    private void reactToCheckResult(Integer checkResult, Integer playerGuess, Player guessingPlayer) throws Exception {
        guessingPlayer.addScorePoint();

        if (!this.selectedMode.equals(PLAYER_GUESS_MODE)) {
            for (Player player : this.players) {
                try {
                    switch (checkResult) {
                        case CheckGuessService.GUESS_TOO_LOW:
                            player.adjustMinPossibleGuess(playerGuess + 1);
                            break;
                        case CheckGuessService.GUESS_TOO_HIGH:
                            player.adjustMaxPossibleGuess(playerGuess - 1);
                            break;
                    }
                } catch (Exception $e) {
                    // Player provide answer that dont have any logic sense, so computer cant adjust to it
                }
            }
        }
    }

    private void showResultsAndSave(Player winner) throws Exception {
        for (Player player : this.players) {
            System.out.println(player.getName() + " guesses: " + player.getScore());
            if (player != winner) {
                player.increaseLoseStreak();
                if (this.selectedMode.equals(RANKED_MODE)) {
                    player.removeBadge();
                }

            } else {
                if (this.selectedMode.equals(RANKED_MODE)) {
                    player.addBadge("Master");
                }
            }
            player.save();
        }
    }
}
