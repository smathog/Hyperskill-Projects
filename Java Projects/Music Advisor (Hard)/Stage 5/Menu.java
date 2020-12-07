package advisor;

import java.util.*;
import java.util.regex.*;

public class Menu {
    private static Pattern playlistPattern = Pattern.compile("(?<=playlists ).*");

    public void MenuLoop() {
       Scanner scanner = new Scanner(System.in);
       int currentPage = -1;
       List<String> currentList = null;
       while (true) {
           String command = scanner.nextLine();

           if (!Settings.getAccess() && !(command.equals("auth") || command.equals("exit"))) {
               System.out.println("Please, provide access for application.");
               continue;
           }

           if ("auth".equals(command)) {
               if (Settings.authorizeAccess()) {
                   System.out.println("making http request for access_token...");
                   if (Settings.getAccessToken()) {
                       System.out.println("Success!");
                       Settings.setAccess();
                   } else
                       System.out.println("try again");
               } else
                   System.out.println("try again");
           } else if ("exit".equals(command))
               break;
           else { //Main logic body
               if (command.contains("playlists ")) {
                   Matcher m = playlistPattern.matcher(command);
                   m.find();
                   currentList = MusicLoader.playlistsCategory(m.group());
                   currentPage = 1;
               } else {
                   switch (command) {
                       case "next":
                           if (currentPage < currentList.size() / Settings.getNumPages()
                                   + ((currentList.size() % Settings.getNumPages()) == 0 ? 0 : 1)) {
                               ++currentPage;
                               break;
                           } else {
                               System.out.println("No more pages.");
                               continue;
                           }
                       case "prev":
                           if (currentPage > 1) {
                               --currentPage;
                               break;
                           } else {
                               System.out.println("No more pages.");
                               continue;
                           }
                       case "featured":
                           currentList = MusicLoader.featuredList();
                           currentPage = 1;
                           break;
                       case "new":
                           currentList = MusicLoader.newSongs();
                           currentPage = 1;
                           break;
                       case "categories":
                           currentList = MusicLoader.listCategories();
                           currentPage = 1;
                           break;
                       default:
                           continue;
                   }
               }
               displayPage(currentList, currentPage);
           }
       }
   }

    private void displayPage(List<String> source, int currentPage) {
        if (source == null)
            System.out.println("---PAGE 1 OF 1---");
        else {
            for (int i = (currentPage - 1) * Settings.getNumPages();
                 i < source.size() && i < (currentPage - 1) * Settings.getNumPages() + Settings.getNumPages();
                 ++i)
                System.out.println(source.get(i));
            System.out.println(String.format("---PAGE %d OF %d---", currentPage,
                    source.size() / Settings.getNumPages() + ((source.size() % Settings.getNumPages()) == 0 ? 0 : 1)));
        }
    }
}
