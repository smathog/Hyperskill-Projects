package engine;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuizController {
    private static HttpHeaders jsonSpecifier;

    static {
        jsonSpecifier = new HttpHeaders();
        jsonSpecifier.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    private SimpleQuiz sq;

    public QuizController() {
        sq = new SimpleQuiz();
        sq.setTitle("The Java Logo");
        sq.setText("What is depicted on the Java logo?");
        sq.setOptions(new String[]{"Robot", "Tea leaf", "Cup of coffee", "Bug"});
    }

    @GetMapping(path = "/api/quiz")
    public ResponseEntity<SimpleQuiz> sendSimpleQuiz() {
        return ResponseEntity.ok()
                .headers(jsonSpecifier)
                .body(sq);
    }

    @PostMapping(path = "/api/quiz")
    public ResponseEntity<SimpleFeedback> sendFeedback(@RequestParam int answer) {
        SimpleFeedback sf = new SimpleFeedback();
        if (answer == 2) {
            sf.setSuccess(true);
            sf.setFeedback("Congratulations, you're right!");
        } else {
            sf.setSuccess(false);
            sf.setFeedback("Wrong answer! Please, try again.");
        }
        return ResponseEntity.ok()
                .headers(jsonSpecifier)
                .body(sf);
    }
}
