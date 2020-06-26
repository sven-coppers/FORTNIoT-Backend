package sven.phd.iot.rules.actions;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

public class ColorDeserializer extends JsonDeserializer<Color> {
    @Override
    public Color deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        float[] array = new ObjectMapper().readValue(jsonParser, float[].class);
        System.out.println("COLORARRAY = " + Arrays.toString(array));

        return new Color(array[0], array[1], array[2]);
    }
}
