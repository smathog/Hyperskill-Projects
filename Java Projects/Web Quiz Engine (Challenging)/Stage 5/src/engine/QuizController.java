package engine;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
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
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public QuizController(QuizRepository repo, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path = "/api/register", consumes = "application/json")
    public synchronized ResponseEntity<UserEntity> registerUser(@RequestBody UserEntity user) {
        if (userRepo.findByEmail(user.getEmail()).isPresent()
        || user.getPassword().chars().distinct().count() < 5
        || !user.getEmail().matches(".*@.*\\..*"))
            return ResponseEntity.badRequest().build();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return ResponseEntity.ok()
                .headers(jsonSpecifier)
                .body(user);
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
    public synchronized ResponseEntity<IDQuiz> getQuiz(@Valid @RequestBody AnswerQuiz aq, Principal principal) {
        if (aq.getAnswer() == null)
            aq.setAnswer(new int[]{});
        QuizEntity temp = new QuizEntity(aq);
        temp.setCreator(principal.getName());
        repo.save(temp);
        return ResponseEntity.ok()
                .headers(jsonSpecifier)
                .body(new IDQuiz(temp, temp.getId()));
    }

    @DeleteMapping(path = "api/quizzes/{id}")
    public synchronized ResponseEntity<String> deleteQuiz(@PathVariable int id, Principal principal) {
        var temp = repo.findById(id);
        if (temp.isEmpty())
            return ResponseEntity.notFound().build();
        else if (!temp.get().getCreator().equals(principal.getName()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        else {
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
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
