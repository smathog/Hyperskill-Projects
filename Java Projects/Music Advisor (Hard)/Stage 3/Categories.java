package advisor;

public enum Categories {
    TOP_LISTS("Top Lists"),
    POP("Pop"),
    MOOD("Mood"),
    LATIN("Latin");

    private final String label;

    public String getLabel() {
        return this.label;
    }

    public static Categories getCategory(String label) {
        for (Categories c : Categories.values())
            if (c.label.equals(label))
                return c;
        return null;
    }

    Categories(String label) {
        this.label = label;
    }
}
