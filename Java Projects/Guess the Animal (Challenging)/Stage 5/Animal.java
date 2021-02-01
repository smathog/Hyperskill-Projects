package animals;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JsonSerialize
public class Animal {
    private String name;
    private String article;
    private String ability;

    public Animal() {}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Animal))
            return false;
        Animal rhs = (Animal) other;
        if (rhs == this)
            return true;
        return rhs.name.equals(this.name);
    }
}

