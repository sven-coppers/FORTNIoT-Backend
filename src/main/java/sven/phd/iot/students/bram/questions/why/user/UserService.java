package sven.phd.iot.students.bram.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private Map<String, User> users;
    private static UserService instance;

    public static UserService getInstance() {
        if(instance == null) {
            instance = new UserService();
        }
        return instance;
    }
    public UserService() {
        users = new HashMap<>();
        this.fetchUsers();
    }

    private void fetchUsers() {
        try {
            String url = "ws://hassio.local:8123/api/websocket";
            WebsocketClientEndpoint clientEndpoint = new WebsocketClientEndpoint(new URI(url));

            UserService that = this;

            // add listener
            clientEndpoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {

                    JSONObject obj = new JSONObject(message);
                    if(obj.has("result")) {
                        String arrStr = obj.get("result").toString();
                        JSONArray arr = new JSONArray(arrStr);

                        for (int i = 0; i < arr.length(); i++) {

                            JSONObject userJson = (JSONObject) arr.get(i);
                            User user = new User();
                            user.id = userJson.get("id").toString();
                            user.name = userJson.get("name").toString();
                            user.is_active = (boolean) userJson.get("is_active");
                            user.is_owner = (boolean) userJson.get("is_owner");
                            user.system_generated = (boolean) userJson.get("system_generated");

                            that.addUser(user.id, user);
                        }

                    }
                }
            });

            clientEndpoint.sendMessage("{\"type\":\"config/auth/list\",\"id\":21}");
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        }

    }
    private void addUser(String id, User user) {
        this.users.put(id, user);
    }
    public String getUsername(String userId) {
        System.out.println(userId);
        System.out.println(users);
        return this.users.get(userId).name;
    }
    public User getUser(String userId) {
        return this.users.get(userId);
    }
}
