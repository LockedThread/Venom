package org.venompvp.venom.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import java.io.IOException;
import java.util.UUID;

public class ChunkAdapter extends TypeAdapter<Chunk> {

    @Override
    public void write(JsonWriter jsonWriter, Chunk chunk) throws IOException {
        if (chunk == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.beginObject();
            jsonWriter.name("uid").value(chunk.getWorld().getUID().toString());
            jsonWriter.name("x").value(chunk.getX());
            jsonWriter.name("z").value(chunk.getZ());
            jsonWriter.endObject();
        }
    }

    @Override
    public Chunk read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        int x = 0, z = 0;
        String uid = "";
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            switch (jsonReader.nextName()) {
                case "x":
                    x = jsonReader.nextInt();
                    break;
                case "z":
                    z = jsonReader.nextInt();
                    break;
                case "uid":
                    uid = jsonReader.nextString();
                    break;
            }
        }
        jsonReader.endObject();
        return Bukkit.getWorld(UUID.fromString(uid)).getChunkAt(x, z);
    }
}
