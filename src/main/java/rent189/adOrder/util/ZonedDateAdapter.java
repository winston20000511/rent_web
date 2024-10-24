package rent189.adOrder.util;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ZonedDateAdapter implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime>{

	private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
	
	@Override
	// JsonElement: a class represents an element of JSON (JsonObject, JsonArray, JsonPrimitive, JsonNull)
	public JsonElement serialize(ZonedDateTime zonedDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
		return new JsonPrimitive(zonedDateTime.format(formatter));
	}
	
	@Override
	public ZonedDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
			throws JsonParseException {
		return ZonedDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString(), formatter);
	}



}
