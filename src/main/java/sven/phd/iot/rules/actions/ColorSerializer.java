package sven.phd.iot.rules.actions;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.awt.*;
import java.io.IOException;

public class ColorSerializer extends JsonSerializer<Color> {
    @Override
    public void serialize(Color color, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        double[] array = {color.getRed()/ 255.0f, color.getGreen()/ 255.0f, color.getBlue()/ 255.0f};
        jsonGenerator.writeArray(array, 0, array.length);
    }
}
