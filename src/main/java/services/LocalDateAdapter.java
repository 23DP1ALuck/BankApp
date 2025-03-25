package services;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public JsonElement serialize(LocalDateTime dateTime, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(dateTime.format(formatter));
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
        return LocalDateTime.parse(json.getAsString(), formatter);
    }
}
