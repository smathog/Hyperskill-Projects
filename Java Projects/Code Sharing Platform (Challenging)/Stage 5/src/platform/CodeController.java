package platform;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Map;
import java.util.UUID;

@RestController
@Component
public class CodeController {
    private static HttpHeaders apiHeaders;
    private static HttpHeaders htmlHeaders;
    private static Configuration config;

    static {
        apiHeaders = new HttpHeaders();
        apiHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        htmlHeaders = new HttpHeaders();
        htmlHeaders.add(HttpHeaders.CONTENT_TYPE, "text/html");
        config = new Configuration(Configuration.VERSION_2_3_29);
        try {
            config.setDirectoryForTemplateLoading(new File("C:\\Users\\Scott\\Desktop\\Code Sharing Platform\\Code Sharing Platform\\task\\src\\resources"));
        } catch (IOException e) {
            System.out.println("Error setting up Configuration for FreeMarker!");
            System.out.println(e.getMessage());
        }
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        config.setLogTemplateExceptions(false);
        config.setWrapUncheckedExceptions(true);
        config.setFallbackOnNullLoopVariable(false);
    }

    private CodeRepository repo;

    @Autowired
    public CodeController(CodeRepository repo) {
        this.repo = repo;
    }

    @Transactional
    @GetMapping(path = "/api/code/{N}")
    public ResponseEntity<Code> codeAsJSON(@PathVariable String N) {
        System.out.println("GET /api/code/N");
        var ce = repo.findByUUID(N);
        if (ce.isEmpty() || exceedsTime(ce.get())) {
            if (ce.isPresent())
                updateRepo(ce.get()); //need to delete snippet with exceeded time
            return ResponseEntity.notFound().build();
        }
        updateRepo(ce.get());
        return ResponseEntity.ok()
                .headers(apiHeaders)
                .body(new Code(ce.get()));
    }

    @Transactional
    @GetMapping(path = "/code/{N}")
    public ResponseEntity<String> codeAsHTML(@PathVariable String N) {
        System.out.println("GET /code/N");
        var ce = repo.findByUUID(N);
        if (ce.isEmpty() || exceedsTime(ce.get())) {
            if (ce.isPresent())
                updateRepo(ce.get());
            return ResponseEntity.notFound().build();
        }
        int numViewsBeforeUpdate = ce.get().getNumViewsRemaining();
        updateRepo(ce.get());
        Map<String, Object> root = new HashMap<>();
        Code temp = new Code(ce.get());
        root.put("source", temp);
        if (temp.getViews() == numViewsBeforeUpdate - 1) {
            root.put("enableViews", true);
            System.out.println("Enabled views");
        } else
            root.put("enableViews", false);
        try {
            Template template = config.getTemplate("GetN.html");
            StringWriter sw = new StringWriter();
            template.process(root, sw);
            System.out.println("Template: \n" + sw.toString());
            return ResponseEntity.ok()
                    .headers(htmlHeaders)
                    .body(sw.toString());
        } catch (Exception e) {
            System.out.println("Exception in creating template!");
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    @PostMapping(path = "/api/code/new", consumes = "application/json")
    public ResponseEntity<returnID> getCodeAsJSON(@RequestBody RawCode rc) {
        System.out.println("POST /api/code/new");
        CodeEntity temp = new CodeEntity();
        temp.setCode(rc.getCode());
        temp.setDate(LocalDateTime.now());
        temp.setUUID(generateUUID());
        temp.setNumViewsRemaining(rc.getViews());
        temp.setSecondsToView(rc.getTime());
        System.out.println("Generated UUID: " + temp.getUUID());
        repo.save(temp);
        return ResponseEntity.ok()
                .headers(apiHeaders)
                .body(new returnID(temp.getUUID()));
    }

    @Transactional
    @GetMapping(path = "/code/new")
    public ResponseEntity<String> sendHTMLForm() {
        try {
            System.out.println("Returning HTML form");
            Path fileName = Path.of("C:\\Users\\Scott\\Desktop\\Code Sharing Platform\\Code Sharing Platform\\task\\src\\resources\\FormResponse.html");
            return ResponseEntity.ok().
                    headers(htmlHeaders)
                    .body(Files.readString(fileName));
        } catch (IOException e) {
            System.out.println("Error reading from FormResponse.html");
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Failed to read from HTML file!");
        }
    }

    @Transactional
    @GetMapping(path = "/api/code/latest")
    public ResponseEntity<List<Code>> getLatestAsJSON() {
        System.out.println("GET /api/code/latest");
        List<Code> responses = getRecentResponses();
        return ResponseEntity.ok()
                .headers(apiHeaders)
                .body(responses);
    }

    @Transactional
    @GetMapping(path = "/code/latest")
    public ResponseEntity<String> getLatestAsHTML() {
        System.out.println("GET /code/latest");
        try {
            Map<String, Object> root = new HashMap<>();
            root.put("list", getRecentResponses());
            Template template = config.getTemplate("Latest.html");
            StringWriter sw = new StringWriter();
            template.process(root, sw);
            return ResponseEntity.ok()
                    .headers(htmlHeaders)
                    .body(sw.toString());
        } catch (Exception e) {
            System.out.println("Exception in creating template!");
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    private List<Code> getRecentResponses() {
        var listAll = repo.findAllByOrderByDateDescCodeDesc();
        var list = listAll.stream().
                filter(ce -> ce.getSecondsToView() <= 0 && ce.getNumViewsRemaining() <= 0)
                .limit(10)
                .map(Code::new)
                .collect(Collectors.toList());
        return list;
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    @Transactional
    private void updateRepo(CodeEntity ce) {
        if (ce.getNumViewsRemaining() == 1 || exceedsTime(ce)) {
            repo.deleteByUUID(ce.getUUID());
            if (!exceedsTime(ce))
                ce.setNumViewsRemaining(ce.getNumViewsRemaining() - 1);
        } else if (ce.getNumViewsRemaining() > 1) {
            ce.setNumViewsRemaining(ce.getNumViewsRemaining() - 1);
            repo.save(ce);
        }
    }

    private boolean exceedsTime(CodeEntity ce) {
        if (ce.getSecondsToView() > 0 && ChronoUnit.SECONDS.between(ce.getDate(), LocalDateTime.now()) > ce.getSecondsToView())
            return true;
        else
            return false;
    }
}
