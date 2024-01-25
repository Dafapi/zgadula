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

    private String name;
    private Integer score = 0, lastBestScore = 0, losesNo = 0, maxPossibleGuess, minPossibleGuess;
    private String badge = "";

    public Player(String name, String gameMetadata) {
        this.pathname = Paths.get("./score---" + name + "---" + gameMetadata + ".txt");
        this.name = name;
        this.load();
    }

    private void load() {
        String saveData = this.getSaveData().trim();
        if (saveData.isEmpty()) return;

        try {
            try {
                this.lastBestScore = Integer.parseInt(saveData.split(";")[0]);
            } catch (ArrayIndexOutOfBoundsException e) {
                this.lastBestScore = 0;
            }

            try {
                this.losesNo = Integer.parseInt(saveData.split(";")[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                this.losesNo = 0;
            }

            try {
                this.badge = saveData.split(";")[2];
            } catch (ArrayIndexOutOfBoundsException e) {
                this.badge = "";
            }
        } catch (NumberFormatException e) {
            this.lastBestScore = 0;
            this.losesNo = 0;
            this.badge = "";
        }

        this.score = 0;
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

        Integer scoreToSave;
        if (this.getLastBestScore() > this.getScore() || this.getLastBestScore().equals(0)) {
            scoreToSave = this.getScore();
        } else {
            scoreToSave = this.getLastBestScore();
        }

        String writeln = scoreToSave.toString() + ";" + this.getLosesNo() + ";" + this.badge;

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

    public String getName() {
        return (this.badge.isEmpty() ? "" : "<<" + this.badge + ">> ") + this.name;
    }

    public Integer getScore() {
        return this.score;
    }

    public Integer getLastBestScore() {
        return this.lastBestScore;
    }

    public Integer getLosesNo() {
        return this.losesNo;
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

    public void increaseLoseStreak() {
        this.losesNo++;
    }

    public void addBadge(String badge) throws Exception {
        if (badge.contains(";")) {
            throw new Exception("Invalid badge name");
        }
        this.badge = badge;
    }

    public void removeBadge() {
        this.badge = "";
    }
}
