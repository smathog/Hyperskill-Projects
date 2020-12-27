package engine;

public class AnswerQuiz extends BaseQuiz {
    private int answer;

    public AnswerQuiz() {}

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return super.toString() + " answer: " + answer;
    }
}
