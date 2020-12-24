package platform;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class CodeController {
    private static HttpHeaders apiHeaders;
    private static HttpHeaders htmlHeaders;

    static {
        apiHeaders = new HttpHeaders();
        apiHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        htmlHeaders = new HttpHeaders();
        htmlHeaders.add(HttpHeaders.CONTENT_TYPE, "text/html");
    }

    private Code code = new Code("public static void main(String[] args) {\n" +
            "   SpringApplication.run(CodeSharingPlatform.class, args);\n}");

    @GetMapping(path = "/api/code")
    public ResponseEntity<Code> codeAsJSON() {
        System.out.println("Returning json");
        return ResponseEntity.ok()
                .headers(apiHeaders)
                .body(code);
    }

    @GetMapping(path = "/code")
    public ResponseEntity<String> codeAsHTML() {
        System.out.println("Returning HTML");
        return ResponseEntity.ok()
                .headers(htmlHeaders)
                .body(codeToHTML(code));
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

    @PostMapping(path = "/api/code/new", consumes = "application/json")
    public ResponseEntity<String> getCodeAsJSON(@RequestBody RawCode rc) {
        System.out.println("Received code as JSON");
        code = new Code(rc.getCode());
        return ResponseEntity.ok()
                .headers(apiHeaders)
                .body("{}");
    }

    private String codeToHTML(Code code) {
        return
                "<html>" +
                        "\n<head>" +
                        "\n<title>Code</title>"+
                        "\n<style> " +
                        "\n#load_date{color:green;}" +
                        "\n#code_snippet{background-size: contain;background-color:LightGrey;}"+
                        "\n</style>" +
                        "\n</head>" +
                        "\n<body>" +
                        "\n<span id=\"load_date\">" + code.getDate() + "</span>" +
                        "\n<pre id=\"code_snippet\">" +
                        "\n" + code.getCode() +
                        "\n</pre>" +
                        "\n</body>" +
                        "\n</html>";
    }
}
