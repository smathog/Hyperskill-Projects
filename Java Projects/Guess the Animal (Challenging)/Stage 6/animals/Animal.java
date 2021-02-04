package animals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Animal {
    private String name;

    public Animal() {}

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getNameWithoutArticle() {
        Main.Wrapper w = Main.Wrapper.getInstance();
        return name.replaceAll(w.appResource.getString("animalName.1.pattern"), w.appResource.getString("animalName.1.replace"));
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

