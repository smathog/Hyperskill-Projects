package advisor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.ArrayList;

public class MusicLoader {
    private static HashMap<String, String> categoryNameToID = null;

    public static void newSongs() {
        JsonObject jo = Settings.getRequest("/v1/browse/new-releases");
        if (hasError(jo))
            return;
        JsonObject album = jo.getAsJsonObject("albums");
        for (JsonElement entry : album.getAsJsonArray("items")) {
            System.out.println(entry.getAsJsonObject().get("name").getAsString());
            ArrayList<String> artists = new ArrayList<>();
            for (JsonElement artist : entry.getAsJsonObject().getAsJsonArray("artists")) {
                artists.add(artist.getAsJsonObject().get("name").getAsString());
            }
            System.out.println(artists);
            System.out.println(entry.getAsJsonObject().getAsJsonObject("external_urls").get("spotify").getAsString());
            System.out.println();
        }
    }

    public static void featuredList() {
        JsonObject jo = Settings.getRequest("/v1/browse/featured-playlists");
        if (hasError(jo))
            return;
        extractPlaylists(jo);
    }

    public static void loadMap() {
        if (categoryNameToID == null) {
            categoryNameToID = new HashMap<>();
            JsonObject jo = Settings.getRequest("/v1/browse/categories");
            JsonObject categories = jo.getAsJsonObject("categories");
            for (JsonElement entry : categories.getAsJsonArray("items")) {
                String categoryName = entry.getAsJsonObject().get("name").getAsString();
                categoryNameToID.putIfAbsent(categoryName, entry.getAsJsonObject().get("id").getAsString());
            }
        }
    }

    public static void listCategories() {
        JsonObject jo = Settings.getRequest("/v1/browse/categories");
        if (hasError(jo))
            return;
        JsonObject categories = jo.getAsJsonObject("categories");
        for (JsonElement entry : categories.getAsJsonArray("items")) {
            String categoryName = entry.getAsJsonObject().get("name").getAsString();
            System.out.println(categoryName);
        }
    }

    public static void playlistsCategory(String category) {
        loadMap();
        if (!categoryNameToID.containsKey(category)) {
            System.out.println("Unknown category name");
            return;
        }
        String categoryID = categoryNameToID.get(category);
        JsonObject jo = Settings.getRequest("/v1/browse/categories/" + categoryID + "/playlists");
        if (hasError(jo))
            return;
        extractPlaylists(jo);
    }

    private static boolean hasError(JsonObject jo) {
        if (jo.has("error")) {
            System.out.println(jo.getAsJsonObject("error").get("message").getAsString());
            return true;
        } else
            return false;
    }

    private static void extractPlaylists(JsonObject jo) {
        JsonObject playLists = jo.getAsJsonObject("playlists");
        for (JsonElement entry : playLists.getAsJsonArray("items")) {
            System.out.println(entry.getAsJsonObject().get("name").getAsString());
            System.out.println(entry.getAsJsonObject().getAsJsonObject("external_urls").get("spotify").getAsString());
            System.out.println();
        }
    }
}
