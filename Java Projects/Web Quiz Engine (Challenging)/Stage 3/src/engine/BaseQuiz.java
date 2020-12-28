package engine;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BaseQuiz {
    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "text is required")
    private String text;

    @NotNull(message = "options cannot be null")
    @Size(min = 2, message = "must be at least two options")
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
