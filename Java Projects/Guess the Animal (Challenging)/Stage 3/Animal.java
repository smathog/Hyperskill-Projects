package animals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Animal {
    private final String name;
    private final String article;
    private String ability;

    public Animal(String name) {
        Pattern p = Pattern.compile("((a )|(an ))?([\\w\\-]+)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(name);
        m.matches();
        this.name = name.substring(m.start(4)).toLowerCase();
        if (m.start(1) >= 0)
            this.article = name.substring(m.start(1), m.end(1)).trim().toLowerCase();
        else {
            if (Main.getAnSet().contains(this.name.charAt(0)))
                this.article = "an";
            else
                this.article = "a";
        }
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public String getAbility() {
        return ability;
    }

    public String getArticle() {
        return article;
    }

    @Override
    public String toString() {
        return name;
    }
}

