package engine;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
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
    public ResponseEntity<IDQuiz> getQuizByID(@PathVariable int id) {
        if (quizMap.containsKey(id))
            return ResponseEntity.ok()
                .headers(jsonSpecifier)
                .body(new IDQuiz(quizMap.get(id), id));
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/api/quizzes")
    public ResponseEntity<List<IDQuiz>> getAllQuizzes() {
        var allQuizzes = quizMap.entrySet().stream()
                .map(e -> new IDQuiz(e.getValue(), e.getKey()))
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .headers(jsonSpecifier)
                .body(allQuizzes);
    }

    @PostMapping(path = "api/quizzes", consumes = "application/json")
    public ResponseEntity<IDQuiz> getQuiz(@RequestBody AnswerQuiz aq) {
        int id = currentID;
        ++currentID;
        quizMap.put(id, aq);
        return ResponseEntity.ok()
                .headers(jsonSpecifier)
                .body(new IDQuiz(aq, id));
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    public ResponseEntity<SimpleFeedback> sendFeedback(@PathVariable int id, @RequestParam int answer) {
        if (quizMap.containsKey(id)) {
            SimpleFeedback sf = new SimpleFeedback();
            if (answer == quizMap.get(id).getAnswer()) {
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
}
