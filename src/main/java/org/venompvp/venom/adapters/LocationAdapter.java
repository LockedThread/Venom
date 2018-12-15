package org.venompvp.venom.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.venompvp.venom.Venom;

import java.io.IOException;
import java.util.UUID;

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
        double x = 0, y = 0, z = 0;
        String uid = "";
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            switch (jsonReader.nextName()) {
                case "x":
                    x = jsonReader.nextDouble();
                    break;
                case "y":
                    y = jsonReader.nextDouble();
                    break;
                case "z":
                    z = jsonReader.nextDouble();
                    break;
                case "uid":
                    uid = jsonReader.nextString();
                    break;
            }
        }
        jsonReader.endObject();
        return new Location(Bukkit.getWorld(UUID.fromString(uid)), x, y, z);
    }
}
