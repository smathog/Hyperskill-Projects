package platform;

import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "code")
public class CodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String code;
    private String date;

    public CodeEntity() {}

    public CodeEntity(Code code) {
        this.code = code.getCode();
        this.date = code.getDate();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
