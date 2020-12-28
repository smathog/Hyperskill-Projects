package engine;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "quizzes")
public class QuizEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    private String title;
    private String text;
    private String[] options;
    private int[] answer;
    private String creator;

    public QuizEntity() {}

    public QuizEntity(AnswerQuiz answerQuiz) {
        this.title = answerQuiz.getTitle();
        this.text = answerQuiz.getText();
        this.options = answerQuiz.getOptions();
        this.answer = answerQuiz.getAnswer();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public int[] getAnswer() {
        return answer;
    }

    public void setAnswer(int[] answer) {
        this.answer = answer;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return super.toString() + " id: " + id + " creator: " + creator;
    }
}
