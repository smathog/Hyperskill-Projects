package platform;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Map;

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

    private ArrayList<Code> codeList;

    private CodeRepository repo;

    @Autowired
    public CodeController(CodeRepository repo) {
        codeList = new ArrayList<>();
        this.repo = repo;
        //Get all entries out of repo, oldest update first:
        var dbEntries = repo.findAllByOrderByDateAsc();
        dbEntries.ifPresent(codeEntities -> codeList.addAll(codeEntities.stream()
                .map(Code::new)
                .collect(Collectors.toList())));
    }

    @GetMapping(path = "/api/code/{N}")
    public ResponseEntity<Code> codeAsJSON(@PathVariable int N) {
        System.out.println("GET /api/code/N");
        return ResponseEntity.ok()
                .headers(apiHeaders)
                .body(codeList.get(N - 1));
    }

    @GetMapping(path = "/code/{N}")
    public ResponseEntity<String> codeAsHTML(@PathVariable int N) {
        System.out.println("GET /code/N");
        Map<String, Object> root = new HashMap<>();
        root.put("list", codeList);
        root.put("index", N - 1);
        try {
            Template template = config.getTemplate("GetN.html");
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

    @PostMapping(path = "/api/code/new", consumes = "application/json")
    public ResponseEntity<returnID> getCodeAsJSON(@RequestBody RawCode rc) {
        System.out.println("POST /api/code/new");
        Code temp = new Code(rc.getCode());
        codeList.add(temp);
        repo.save(new CodeEntity(temp));
        return ResponseEntity.ok()
                .headers(apiHeaders)
                .body(new returnID(Integer.toString(codeList.size())));
    }

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

    @GetMapping(path = "/api/code/latest")
    public ResponseEntity<List<Code>> getLatestAsJSON() {
        System.out.println("GET /api/code/latest");
        List<Code> responses = getRecentResponses();
        return ResponseEntity.ok()
                .headers(apiHeaders)
                .body(responses);
    }

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

    private List<Code> getRecentResponses() {
        return IntStream.range(0, codeList.size())
                .mapToObj(i -> codeList.get(codeList.size() - 1 - i))
                .limit(10)
                .collect(Collectors.toList());
    }
}
