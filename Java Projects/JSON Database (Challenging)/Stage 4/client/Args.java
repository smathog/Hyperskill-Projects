package client;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names =  "-t", description = "type of request")
    private String type;

    @Parameter(names = {"-i", "-k"}, description =  "index of the cell")
    private String index;

    @Parameter(names = {"-m", "-v"}, description = "value to be saved into database")
    private String message;

    public String getType() {
        return type;
    }

    public boolean hasIndex() {
        return index != null;
    }

    public String getIndex() {
        return index;
    }

    public boolean hasMessage() {
        return message != null;
    }

    public String getMessage() {
        return message;
    }
}
