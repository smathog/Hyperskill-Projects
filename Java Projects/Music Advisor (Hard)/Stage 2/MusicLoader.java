package advisor;

import java.util.List;

public class MusicLoader {
    public List<String> load(Categories c) {
        if (c == Categories.TOP_LISTS) {
            return List.of("Walk like A Badass");
        } else if (c == Categories.LATIN) {
            return List.of("Rage Beats");
        } else if (c == Categories.POP) {
            return List.of("Arab Mood Booster");
        } else if (c == Categories.MOOD) {
            return List.of("Walk Like A Badass",
                    "Rage Beats",
                    "Arab Mood Booster",
                    "Sunday Stroll");
        } else {
            return null;
        }
    }
}
