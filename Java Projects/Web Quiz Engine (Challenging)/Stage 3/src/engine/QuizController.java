package engine;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class QuizController {
    private static HttpHeaders jsonSpecifier;

    static {
        jsonSpecifier = new HttpHeaders();
        jsonSpecifier.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    private HashMap<Integer, AnswerQuiz> quizMap;
    private int currentID;

    public QuizController() {
        quizMap = new HashMap<>();
        currentID = 1;
    }

    @GetMapping(path = "/api/quizzes/{id}")
    public synchronized ResponseEntity<IDQuiz> getQuizByID(@PathVariable int id) {
        if (quizMap.containsKey(id))
            return ResponseEntity.ok()
                .headers(jsonSpecifier)
                .body(new IDQuiz(quizMap.get(id), id));
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/api/quizzes")
    public synchronized ResponseEntity<List<IDQuiz>> getAllQuizzes() {
        var allQuizzes = quizMap.entrySet().stream()
                .map(e -> new IDQuiz(e.getValue(), e.getKey()))
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .headers(jsonSpecifier)
                .body(allQuizzes);
    }

    @PostMapping(path = "api/quizzes", consumes = "application/json")
    public synchronized ResponseEntity<IDQuiz> getQuiz(@Valid @RequestBody AnswerQuiz aq) {
        int id = currentID;
        ++currentID;
        if (aq.getAnswer() == null)
            aq.setAnswer(new int[]{});
        quizMap.put(id, aq);
        return ResponseEntity.ok()
                .headers(jsonSpecifier)
                .body(new IDQuiz(aq, id));
    }

    @PostMapping(path = "/api/quizzes/{id}/solve", consumes = "application/json")
    public synchronized ResponseEntity<SimpleFeedback> sendFeedback(@PathVariable int id, @RequestBody Answer a) {
        if (quizMap.containsKey(id)) {
            if (a.getAnswer() == null)
                a.setAnswer(new int[]{});
            var set1 = Arrays.stream(quizMap.get(id).getAnswer()).boxed().collect(Collectors.toSet());
            var set2 = Arrays.stream(a.getAnswer()).boxed().collect(Collectors.toSet());
            SimpleFeedback sf = new SimpleFeedback();
            if (set1.equals(set2) || (quizMap.get(id).getAnswer().length == 0 && a.getAnswer().length == 0)) {
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
