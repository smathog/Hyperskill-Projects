package platform;

import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "code")
public class CodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String code;
    private LocalDateTime date;
    private int numViewsRemaining;
    private int secondsToView;
    private String UUID;

    public CodeEntity() {
    }

    public CodeEntity(Code code) {
        this.code = code.getCode();
        this.date = LocalDateTime.parse(code.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getNumViewsRemaining() {
        return this.numViewsRemaining;
    }

    public void setNumViewsRemaining(int numViewsRemaining) {
        this.numViewsRemaining = numViewsRemaining;
    }

    public int getSecondsToView() {
        return this.secondsToView;
    }

    public void setSecondsToView(int secondsToView) {
        this.secondsToView = secondsToView;
    }

    public String getUUID() {
        return this.UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}
