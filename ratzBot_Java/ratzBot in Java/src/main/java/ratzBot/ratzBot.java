/* 
Copyright (c) Gavin R. Isgar 2020
ratzBot Research Project // ratzBot in Java
*/

package ratzBot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
// Maven library imports
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.DiscordApi;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonParseException;
// *********************

public class ratzBot
{
    public static String token_data;
    public static String bot_token;

    public static void main(final String[] args)
    {
        /*
        Checks to see if tokens.json file exists, then scans each line and appends it to the token_data variable for use by the client
        */
        try {
            File file = new File("./ratzBot_Java/ratzBot in Java/src/main/java/ratzBot/tokens.json");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                if (token_data == null) {
                    token_data = scanner.nextLine();
                }
                else {
                    token_data = token_data + "\n" + scanner.nextLine();
                }
            }
            scanner.close();
        }
        catch (FileNotFoundException ex) {
            System.out.println("An error has occurred.");
            ex.printStackTrace();
        }
        try {
            /*
            Assign token values
            */
            ObjectMapper mapper = new ObjectMapper();
            bot_token = mapper.readValue(token_data, Token.class).bot_token;
        }
        catch (JsonParseException e) { e.printStackTrace();}
        catch (JsonMappingException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }

        /*
        Bot login
        */
        DiscordApi api = new DiscordApiBuilder().setToken(bot_token).login().join();

        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().startsWith("/ratz discorduserinfo ")) {
                String result = event.getMessageContent().substring(22);
                Server server = event.getServer().get();
                try {
                    User user = server.getMemberByDiscriminatedName(result).get();
                    server.getMembers().forEach(member -> {System.out.println(member);});
                    event.getMessage().getChannel().sendMessage(user.getAvatar().getUrl().toString());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

class Token {
    public String bot_token;
}
