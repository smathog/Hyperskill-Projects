package engine;

public class IDQuiz extends BaseQuiz {
    private int id;

    public IDQuiz() {}

    public IDQuiz(AnswerQuiz aq, int id) {
        super(aq);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
