package sven.phd.iot.students.bram.questions.why.user;

import org.json.JSONArray;
import org.json.JSONObject;
import sven.phd.iot.BearerToken;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private Map<String, HassioUser> users;
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
            String url = BearerToken.getInstance().getUrl().replace("http", "ws") + "/api/websocket";
            WebsocketClientEndpoint clientEndpoint = new WebsocketClientEndpoint(new URI(url));

            UserService that = this;

            // add listener
            clientEndpoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    //System.out.println(message);

                    JSONObject obj = new JSONObject(message);
                    if(obj.has("result")) {
                        String arrStr = obj.get("result").toString();
                        JSONArray arr = new JSONArray(arrStr);

                        for (int i = 0; i < arr.length(); i++) {

                            JSONObject userJson = (JSONObject) arr.get(i);
                            HassioUser user = new HassioUser();
                            user.id = userJson.get("id").toString();
                            user.name = userJson.get("name").toString();
                            user.is_active = (boolean) userJson.get("is_active");
                            user.is_owner = (boolean) userJson.get("is_owner");
                            user.system_generated = (boolean) userJson.get("system_generated");

//                            System.out.println("UserService - New user found: " + user.name + " (" + user.id + ")");
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
    private void addUser(String id, HassioUser user) {
        this.users.put(id, user);
    }
    public String getUsername(String userId) {
        System.out.println(userId);
        System.out.println(users);
        return this.users.get(userId).name;
    }
    public HassioUser getUser(String userId) {
        return this.users.get(userId);
    }
}
