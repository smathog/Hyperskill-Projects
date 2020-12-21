package platform;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@RestController
public class CodeController {
    private static Code code = new Code("public static void main(String[] args) {\n" +
            "   SpringApplication.run(CodeSharingPlatform.class, args);\n}");

    @GetMapping(path = "/api/code")
    public ResponseEntity<Code> codeAsJSON() {
        System.out.println("Returning json");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return ResponseEntity.ok()
                .headers(headers)
                .body(code);
    }

    @GetMapping(path = "/code")
    public ResponseEntity<String> codeAsHTML() {
        System.out.println("Returning HTML");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/html");
        return ResponseEntity.ok()
                .headers(headers)
                .body(codeToHTML(code));
    }

    private String codeToHTML(Code code) {
        return
                "<html>" +
                        "\n<head>" +
                        "\n<title>Code</title>"+
                        "\n</head>" +
                        "\n<body>" +
                        "\n<pre>" +
                        "\n" + code.getCode() +
                        "\n</pre>" +
                        "\n</body>" +
                        "\n</html>";
    }
}
