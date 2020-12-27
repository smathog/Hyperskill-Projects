package engine;

public class BaseQuiz {
    private String title;
    private String text;
    private String[] options;

    public BaseQuiz() {}

    public BaseQuiz(BaseQuiz other) {
        this.title = other.title;
        this.text = other.text;
        this.options = other.options;
    }

    @Override
    public String toString() {
        return "title: " + title + " text: " + text + " options: " + String.join(",", options);
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
}
