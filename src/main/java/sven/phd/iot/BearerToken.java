package sven.phd.iot;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class BearerToken {
    private String bearerToken;
    private boolean isUsingBearer;
    private static BearerToken bearerTokenInstance;
    private String url;

    private BearerToken() {
        this.loadBearer();
    }

    /**
     * Singleton
     * @return
     */
    public static BearerToken getInstance() {
        if(bearerTokenInstance == null) {
            bearerTokenInstance = new BearerToken();
        }

        return bearerTokenInstance;
    }

    /**
     * Get the bearer token from the json file
     */
    private void loadBearer() {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        File file = new File(
                ContextManager.class.getClassLoader().getResource("token.json").getFile()
        );
        String path = file.getPath().replace("%20", " ");
        try (FileReader reader = new FileReader(path)) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONObject json = (JSONObject) obj;

            String tokenId = (String) json.get("token_id");
            this.isUsingBearer = (Boolean) json.get("use_token");
            this.bearerToken = (String) json.get(tokenId);
            this.url = (String) json.get("url");
            if(this.url == null) {
                this.url = "http://hassio.local:8123";
            }
            System.out.println("BearerToken - Used URL: " + this.url);

            System.out.println("BearerToken - Used bearer: " + this.bearerToken);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks whether to use bearer or legacy api
     *
     * @return
     */
    public Boolean isUsingBearer() {
        return this.isUsingBearer;
    }

    public String getBearerToken() {
        return this.bearerToken;
    }

    public String getUrl() {
        return this.url;
    }
}