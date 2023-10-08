package dawid.pionk.lesson_1;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Player {

    private final Path pathname;
    private Integer score = 0, maxPossibleGuess, minPossibleGuess;

    public Player(String name) throws FileNotFoundException {
        this.pathname = Paths.get("./" + name + "-score.txt");
        this.load();
    }

    private void load() {
        String saveData = this.getSaveData();
        if (saveData.trim().isEmpty()) return;

        try {
            this.score = Integer.parseInt(saveData);
        } catch (NumberFormatException e) {
            this.score = 0;
        }
    }

    private String getSaveData() {
        File file = this.prepareFile(this.pathname);
        Scanner filereader = null;
        String data = "";
        try {
            filereader = new Scanner(file);
            data = filereader.nextLine();

        } catch (FileNotFoundException | NoSuchElementException e) {
            return "";
        }
        filereader.close();
        return data;

    }

    public void save() throws FileNotFoundException {
        File file = this.prepareFile(this.pathname);
        PrintWriter filewriter = new PrintWriter(file);
        String writeln = this.score.toString();
        filewriter.println(writeln);
        filewriter.close();
    }

    private File prepareFile(Path filepath) {

        File file = new File(filepath.toString());

        if (!file.exists()) {
            try {
                Files.createFile(filepath);
            } catch (FileAlreadyExistsException x) {
                System.err.format("file named %s" + " already exists%n", filepath);
            } catch (IOException x) {
                System.err.format("createFile error: %s%n", x);
            }
        }

        return file;
    }

    public Integer getScore() {
        return this.score;
    }

    public void resetScore() {
        this.score = 0;
    }

    public void addScorePoint() {
        this.score++;
    }

    public void adjustMaxPossibleGuess(int newMaxPossibleGuess) throws Exception {
        if (this.maxPossibleGuess != null && this.maxPossibleGuess < newMaxPossibleGuess) {
            throw new Exception("You can only adjust max possible guest by limiting it");
        }
        this.maxPossibleGuess = newMaxPossibleGuess;
    }

    public Integer getMaxPossibleGuess() {
        return this.maxPossibleGuess;
    }

    public void adjustMinPossibleGuess(int newMinPossibleGuess) throws Exception {
        if (this.minPossibleGuess != null && this.minPossibleGuess > newMinPossibleGuess) {
            throw new Exception("You can only adjust min possible guest by limiting it");
        }
        this.minPossibleGuess = newMinPossibleGuess;
    }

    public Integer getMinPossibleGuess() {
        return this.minPossibleGuess;
    }
}
