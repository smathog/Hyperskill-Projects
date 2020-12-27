package platform;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Code {
    private String code;
    private String date;
    private int time;
    private int views;

    public Code() {}

    public Code(String code) {
        this.code = code;
        setDate(LocalDateTime.now());
    }

    public Code(CodeEntity ce) {
        this.code = ce.getCode();
        setDate(ce.getDate());
        this.views = ce.getNumViewsRemaining();
        this.time = ce.getSecondsToView() <= 0 ? ce.getSecondsToView() : (int) (ce.getSecondsToView() - ChronoUnit.SECONDS.between(ce.getDate(), LocalDateTime.now()));
    }

    @Override
    public String toString() {
        return "code: " + code
                +"\ndate: " + date
                +"\ntime: " + time
                +"\nviews: " + views;
    }

    public String getCode() {
        return this.code;
    }

    public String getDate() {
        return this.date;
    }

    public int getTime() {
        return this.time;
    }

    public int getViews() {
        return this.views;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(LocalDateTime lastUpdated) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");
        this.date = lastUpdated.format(format);
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
