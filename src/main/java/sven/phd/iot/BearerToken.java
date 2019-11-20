package sven.phd.iot;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BearerToken {
        /**
         * Checks whether to use bearer or legacy api
         * @return
         */
        public static Boolean useBearer() {
            //JSON parser object to parse read file
            JSONParser jsonParser = new JSONParser();

            File file = new File(
                    ContextManager.class.getClassLoader().getResource("token.json").getFile()
            );
            String path = file.getPath().replace("%20", " ");
            try (FileReader reader = new FileReader(path))
            {
                //Read JSON file
                Object obj = jsonParser.parse(reader);

                JSONObject json = (JSONObject) obj;

                //Iterate over employee array
                return (Boolean) json.get("use_token");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }

            return false;
        }
        /**
         * Get the bearer token from the json file
         */
        public static String getBearer() {
            //JSON parser object to parse read file
            JSONParser jsonParser = new JSONParser();

            File file = new File(
                    ContextManager.class.getClassLoader().getResource("token.json").getFile()
            );
            String path = file.getPath().replace("%20", " ");
            try (FileReader reader = new FileReader(path))
            {
                //Read JSON file
                Object obj = jsonParser.parse(reader);

                JSONObject json = (JSONObject) obj;

                //Iterate over employee array
                String tokenId = (String) json.get("token_id");

                return (String) json.get(tokenId);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }

            return "";
        }

    }

