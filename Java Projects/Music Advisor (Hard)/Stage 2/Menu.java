package advisor;

import java.util.*;
import java.util.stream.Collectors;

public class Menu {
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
               Categories category = Categories.getCategory(command.split(" ")[1]);
               System.out.println("---" + category.getLabel() + " PLAYLISTS---");
               for (String s : new MusicLoader().load(category))
                   System.out.println(s);
           } else {
               switch (command) {
                   case "exit":
                       System.out.println("---GOODBYE!---");
                       break menuLoop;
                   case "featured":
                       System.out.println("---FEATURED---");
                       for (String s : featured())
                           System.out.println(s);
                       break;
                   case "new":
                       System.out.println("---NEW RELEASES---");
                       for (String s : newMusic())
                           System.out.println(s);
                       break;
                   case "categories":
                       System.out.println("---CATEGORIES---");
                       for (String s : categoryList())
                           System.out.println(s);
                       break;
                   case "auth":
                       System.out.println("https://accounts.spotify.com/authorize?client_id="
                               + Settings.getClientID()
                               + "&redirect_uri=http://localhost:8080&response_type=code");
                       System.out.println("---SUCCESS---");
                       Settings.setAccess();
                       break;
                   default:
                       break;
               }
           }
       }
   }

   private List<String> featured() {
       return List.of("Mellow Morning",
               "Wake Up and Smell the Coffee",
               "Monday Motivation",
               "Songs to Sing in the Shower");
   }

   private List<String> newMusic() {
       return List.of("Mountains [Sia, Diplo, Labrinth]",
                "Runaway [Lil Peep]",
                "The Greatest Show [Panic! At The Disco]",
                "All Out Life [Slipknot]");
    }

    private List<String> categoryList() {
       return Arrays.stream(Categories.values())
               .map(Categories::getLabel)
               .collect(Collectors.toList());
    }
}
