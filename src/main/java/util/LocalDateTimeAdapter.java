package util;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter implements JsonSerializer<Object>, JsonDeserializer<Object> {
    
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        if (src instanceof LocalDate) {
            return new JsonPrimitive(((LocalDate) src).format(dateFormatter));
        } else if (src instanceof LocalTime) {
            return new JsonPrimitive(((LocalTime) src).format(timeFormatter));
        } else if (src instanceof LocalDateTime) {
            return new JsonPrimitive(((LocalDateTime) src).format(dateTimeFormatter));
        }
        return null; // should never reach here
    }

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String value = json.getAsString();
        
        if (typeOfT == LocalDate.class) {
            return LocalDate.parse(value, dateFormatter);
        } else if (typeOfT == LocalTime.class) {
            return LocalTime.parse(value, timeFormatter);
        } else if (typeOfT == LocalDateTime.class) {
            return LocalDateTime.parse(value, dateTimeFormatter);
        }
        return null; // should never reach here
    }
}
