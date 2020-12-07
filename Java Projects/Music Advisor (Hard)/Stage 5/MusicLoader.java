package advisor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.ArrayList;

public class MusicLoader {
    private static HashMap<String, String> categoryNameToID = null;

    public static ArrayList<String> newSongs() {
        JsonObject jo = Settings.getRequest("/v1/browse/new-releases");
        if (hasError(jo))
            return null;
        JsonObject album = jo.getAsJsonObject("albums");
        ArrayList<String> list = new ArrayList<>();
        for (JsonElement entry : album.getAsJsonArray("items")) {
            StringBuilder sb = new StringBuilder();
            sb.append(entry.getAsJsonObject().get("name").getAsString() + '\n');
            ArrayList<String> artists = new ArrayList<>();
            for (JsonElement artist : entry.getAsJsonObject().getAsJsonArray("artists"))
                artists.add(artist.getAsJsonObject().get("name").getAsString());
            sb.append(artists.toString() + '\n');
            sb.append(entry.getAsJsonObject().getAsJsonObject("external_urls").get("spotify").getAsString() + '\n');
            list.add(sb.toString());
        }
        return list;
    }

    public static ArrayList<String> featuredList() {
        JsonObject jo = Settings.getRequest("/v1/browse/featured-playlists");
        if (hasError(jo))
            return null;
        return extractPlaylists(jo);
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

    public static ArrayList<String> listCategories() {
        JsonObject jo = Settings.getRequest("/v1/browse/categories");
        if (hasError(jo))
            return null;
        JsonObject categories = jo.getAsJsonObject("categories");
        ArrayList<String> temp = new ArrayList<>();
        for (JsonElement entry : categories.getAsJsonArray("items")) {
            String categoryName = entry.getAsJsonObject().get("name").getAsString();
            temp.add(categoryName);
        }
        return temp;
    }

    public static ArrayList<String> playlistsCategory(String category) {
        loadMap();
        if (!categoryNameToID.containsKey(category)) {
            System.out.println("Unknown category name");
            return null;
        }
        String categoryID = categoryNameToID.get(category);
        JsonObject jo = Settings.getRequest("/v1/browse/categories/" + categoryID + "/playlists");
        if (hasError(jo))
            return null;
        return extractPlaylists(jo);
    }

    private static boolean hasError(JsonObject jo) {
        if (jo.has("error")) {
            System.out.println(jo.getAsJsonObject("error").get("message").getAsString());
            return true;
        } else
            return false;
    }

    private static ArrayList<String> extractPlaylists(JsonObject jo) {
        JsonObject playLists = jo.getAsJsonObject("playlists");
        ArrayList<String> temp = new ArrayList<>();
        for (JsonElement entry : playLists.getAsJsonArray("items")) {
            StringBuilder sb = new StringBuilder();
            sb.append(entry.getAsJsonObject().get("name").getAsString() + '\n');
            sb.append(entry.getAsJsonObject().getAsJsonObject("external_urls").get("spotify").getAsString() + '\n');
            temp.add(sb.toString());
        }
        return temp;
    }
}
