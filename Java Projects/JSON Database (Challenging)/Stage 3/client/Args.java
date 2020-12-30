package client;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names =  "-t", description = "type of request")
    private String type;

    @Parameter(names = "-i", description =  "index of the cell")
    private int index;

    @Parameter(names = "-m", description = "value to be saved into database")
    private String message;

    public String getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public boolean hasMessage() {
        return message != null;
    }

    public String getMessage() {
        return message;
    }
}
