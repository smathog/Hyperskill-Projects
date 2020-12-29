package engine;

public class IDQuiz extends BaseQuiz {
    private int id;

    public IDQuiz() {}

    public IDQuiz(QuizEntity qe, int id) {
        super(qe);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
