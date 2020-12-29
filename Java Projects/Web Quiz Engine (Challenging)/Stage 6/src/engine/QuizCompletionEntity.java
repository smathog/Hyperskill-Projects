package engine;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "completionsTable")
public class QuizCompletionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer completionID;
    private String username;
    private int id;
    private LocalDateTime completedAt;

    public QuizCompletionEntity() {}

    public QuizCompletionEntity(String username, int id) {
        this.username = username;
        this.id = id;
        completedAt = LocalDateTime.now();
    }

    @JsonIgnore
    public Integer getCompletionID() {
        return completionID;
    }

    public void setCompletionID(Integer completionID) {
        this.completionID = completionID;
    }

    @JsonIgnore
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
