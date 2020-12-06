package advisor;

import java.util.*;
import java.util.regex.*;

public class Menu {
    private static Pattern playlistPattern = Pattern.compile("(?<=playlists ).*");

   public void MenuLoop() {
       Scanner scanner = new Scanner(System.in);
       menuLoop:
       while (true) {
           String command = scanner.nextLine();

           if (!Settings.getAccess() && !(command.equals("auth") || command.equals("exit"))) {
               System.out.println("Please, provide access for application.");
               continue;
           }

           if (command.contains("playlists ")) {
               Matcher m = playlistPattern.matcher(command);
               m.find();
               MusicLoader.playlistsCategory(m.group());
           } else {
               switch (command) {
                   case "exit":
                       break menuLoop;
                   case "featured":
                       MusicLoader.featuredList();
                       break;
                   case "new":
                       MusicLoader.newSongs();
                       break;
                   case "categories":
                       MusicLoader.listCategories();
                       break;
                   case "auth":
                       if (Settings.authorizeAccess()) {
                           System.out.println("making http request for access_token...");
                           if (Settings.getAccessToken()) {
                               System.out.println("Success!");
                               Settings.setAccess();
                           } else {
                               System.out.println("try again");
                           }
                       } else {
                           System.out.println("try again");
                       }
                       break;
                   default:
                       break;
               }
           }
       }
   }
}
