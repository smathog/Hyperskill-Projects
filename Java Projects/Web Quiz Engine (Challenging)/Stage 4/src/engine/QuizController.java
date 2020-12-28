package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Component
public class QuizController {
    private static HttpHeaders jsonSpecifier;

    static {
        jsonSpecifier = new HttpHeaders();
        jsonSpecifier.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    private final QuizRepository repo;

    @Autowired
    public QuizController(QuizRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = "/api/quizzes/{id}")
    public synchronized ResponseEntity<IDQuiz> getQuizByID(@PathVariable int id) {
        var temp = repo.findById(id);
        if (temp.isPresent())
            return ResponseEntity.ok()
                .headers(jsonSpecifier)
                .body(new IDQuiz(temp.get(), temp.get().getId()));
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/api/quizzes")
    public synchronized ResponseEntity<List<IDQuiz>> getAllQuizzes() {
        var allQuizzes = repo.findAll();
        return ResponseEntity.ok()
                .headers(jsonSpecifier)
                .body(allQuizzes.stream()
                        .map(entity -> new IDQuiz(entity, entity.getId()))
                        .collect(Collectors.toList()));
    }

    @PostMapping(path = "api/quizzes", consumes = "application/json")
    public synchronized ResponseEntity<IDQuiz> getQuiz(@Valid @RequestBody AnswerQuiz aq) {
        if (aq.getAnswer() == null)
            aq.setAnswer(new int[]{});
        QuizEntity temp = new QuizEntity(aq);
        repo.save(temp);
        return ResponseEntity.ok()
                .headers(jsonSpecifier)
                .body(new IDQuiz(temp, temp.getId()));
    }

    @PostMapping(path = "/api/quizzes/{id}/solve", consumes = "application/json")
    public synchronized ResponseEntity<SimpleFeedback> sendFeedback(@PathVariable int id, @RequestBody Answer a) {
        var temp = repo.findById(id);
        if (temp.isPresent()) {
            if (a.getAnswer() == null)
                a.setAnswer(new int[]{});
            var set1 = Arrays.stream(temp.get().getAnswer()).boxed().collect(Collectors.toSet());
            var set2 = Arrays.stream(a.getAnswer()).boxed().collect(Collectors.toSet());
            SimpleFeedback sf = new SimpleFeedback();
            if (set1.equals(set2) || (temp.get().getAnswer().length == 0 && a.getAnswer().length == 0)) {
                sf.setSuccess(true);
                sf.setFeedback("Congratulations, you're right!");
            } else {
                sf.setSuccess(false);
                sf.setFeedback("Wrong answer! Please, try again.");
            }
            return ResponseEntity.ok()
                    .headers(jsonSpecifier)
                    .body(sf);
        } else
            return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public synchronized Map<String, String> handleValidationExceptions(MethodArgumentNotValidException e) {
        return e.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(error -> ((FieldError) error).getField(), ObjectError::getDefaultMessage));
    }
}
