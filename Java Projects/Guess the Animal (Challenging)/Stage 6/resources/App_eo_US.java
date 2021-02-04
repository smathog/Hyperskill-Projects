import animals.Animal;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App_eo_US extends ListResourceBundle {
    private static List<Object[]> messagesList() {
        ResourceBundle messagesBundle = ResourceBundle.getBundle("messages_eo");
        return Arrays.asList(
                new Object[][]{
                        {"fileName", "animals_eo."},
                        {"welcome", messagesBundle.getString("welcome")},
                        {"greeting.morning", messagesBundle.getString("greeting.morning")},
                        {"greeting.afternoon", messagesBundle.getString("greeting.afternoon")},
                        {"greeting.evening", messagesBundle.getString("greeting.evening")},
                        {"farewell", messagesBundle.getString("farewell").split("\\f")},
                        {"ask.again", messagesBundle.getString("ask.again").split("\\f")},
                        {"animal.wantLearn", messagesBundle.getString("animal.wantLearn")},
                        {"animal.askFavorite", messagesBundle.getString("animal.askFavorite")},
                        {"animal.nice", messagesBundle.getString("animal.nice").split("\\f")},
                        {"animal.prompt", messagesBundle.getString("animal.prompt")},
                        {"animal.error", messagesBundle.getString("animal.error")},
                        {"statement.prompt", (BiFunction<Animal, Animal, String>) (a1, a2) -> MessageFormat.format(messagesBundle.getString("statement.prompt"),
                                a1.getName(),
                                a2.getName())},
                        {"statement.error", messagesBundle.getString("statement.error")},
                        {"game.entry", (Runnable) () -> {
                            System.out.println(messagesBundle.getString("game.letsPlay"));
                            System.out.println(messagesBundle.getString("game.think"));
                            System.out.println(messagesBundle.getString("game.enter"));
                            System.out.println();
                        }},
                        {"game.win", messagesBundle.getString("game.win")},
                        {"game.giveUp", messagesBundle.getString("game.giveUp")},
                        {"game.isCorrect", (Function<Animal, String>) a -> MessageFormat.format(messagesBundle.getString("game.isCorrect"), a.getName())},
                        {"game.learned", messagesBundle.getString("game.learned")},
                        {"game.distinguish", messagesBundle.getString("game.distinguish")},
                        {"game.again", messagesBundle.getString("game.again").split("\\f")},
                        {"menu.property.title", messagesBundle.getString("menu.property.title")},
                        {"menu.property.exit", messagesBundle.getString("menu.property.exit")},
                        {"menu.property.error", messagesBundle.getString("menu.property.error")},
                        {"menu.entry.play", messagesBundle.getString("menu.entry.play")},
                        {"menu.entry.list", messagesBundle.getString("menu.entry.list")},
                        {"menu.entry.search", messagesBundle.getString("menu.entry.search")},
                        {"menu.entry.statistics", messagesBundle.getString("menu.entry.statistics")},
                        {"menu.entry.print", messagesBundle.getString("menu.entry.print")},
                        {"tree.list.animals", messagesBundle.getString("tree.list.animals")},
                        {"tree.search.facts", (Function<Animal, String>) a -> MessageFormat.format(messagesBundle.getString("tree.search.facts"), a.getName())},
                        {"tree.search.noFacts", (Function<Animal, String>) a -> MessageFormat.format(messagesBundle.getString("tree.search.noFacts"), a.getName())},
                        {"tree.stats.title", messagesBundle.getString("tree.stats.title")},
                        {"tree.stats.root", (Function<String, String>) s -> MessageFormat.format(messagesBundle.getString("tree.stats.root"), s)},
                        {"tree.stats.nodes", (Function<Integer, String>) i -> MessageFormat.format(messagesBundle.getString("tree.stats.nodes"), i)},
                        {"tree.stats.animals", (Function<Integer, String>) i -> MessageFormat.format(messagesBundle.getString("tree.stats.animals"), i)},
                        {"tree.stats.statements", (Function<Integer, String>) i -> MessageFormat.format(messagesBundle.getString("tree.stats.statements"), i)},
                        {"tree.stats.height", (Function<Integer, String>) i -> MessageFormat.format(messagesBundle.getString("tree.stats.height"), i)},
                        {"tree.stats.minimum", (Function<Integer, String>) i -> MessageFormat.format(messagesBundle.getString("tree.stats.minimum"), i)},
                        {"tree.stats.average", (Function<Double, String>) d -> MessageFormat.format(messagesBundle.getString("tree.stats.average"), d)}
                }
        );
    }

    private static List<Object[]> patternList() {
        ResourceBundle patternsBundle = ResourceBundle.getBundle("patterns_eo");
        return patternsBundle.keySet().stream()
                .map(s -> new Object[]{s, patternsBundle.getString(s)})
                .collect(Collectors.toList());
    }

    @Override
    protected Object[][] getContents() {
        return Stream.of(messagesList(), patternList())
                .flatMap(l -> l.stream())
                .collect(Collectors.toList())
                .toArray(new Object[][]{});
    }

}
