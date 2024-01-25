package dawid.pionk.lesson_1;

import java.io.FileNotFoundException;

public class ComputerPlayer extends Player {

    public ComputerPlayer(String gameMetadata) throws FileNotFoundException {
        super("", gameMetadata);
    }

    @Override
    public String getName() {
        return "Computer";
    }
}
