package sven.phd.iot.hassio.states;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HassioDateDeserializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String timezone = jsonParser.getText().substring(26);
        String datetime = jsonParser.getText().substring(0, 23);
        String merged = datetime + timezone;

        try {
            if(jsonParser.getText().length() == 29) {
                return df.parse(jsonParser.getText());
            } else {
                return df.parse(merged);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}