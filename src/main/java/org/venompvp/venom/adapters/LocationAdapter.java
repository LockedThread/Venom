package org.venompvp.venom.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Location;
import org.venompvp.venom.Venom;

import java.io.IOException;

public class LocationAdapter extends TypeAdapter<Location> {

    private Venom plugin;

    public LocationAdapter(Venom plugin) {
        this.plugin = plugin;
    }

    @Override
    public void write(JsonWriter jsonWriter, Location location) throws IOException {
        if (location == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.beginObject();
            jsonWriter.name("uid").value(location.getWorld().getUID().toString());
            jsonWriter.name("x").value(location.getX());
            jsonWriter.name("y").value(location.getY());
            jsonWriter.name("z").value(location.getZ());
            jsonWriter.endObject();
        }
    }

    @Override
    public Location read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return plugin.getGson().fromJson(jsonReader, Location.class);
    }
}
