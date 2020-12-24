package platform;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Code {
    private String code;
    private String date;

    public Code(String code) {
        this.code = code;
        setDate(LocalDateTime.now());
    }

    public String getCode() {
        return this.code;
    }

    public String getDate() {
        return this.date;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(LocalDateTime lastUpdated) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = lastUpdated.format(format);
    }
}
