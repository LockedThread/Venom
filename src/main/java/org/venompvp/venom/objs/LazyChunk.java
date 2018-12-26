package org.venompvp.venom.objs;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

/*
 * Wouldn't serialize org.bukkit.Chunk properly
 */

public class LazyChunk {

    private String uid;
    private int x, z;

    public LazyChunk(String uid, int x, int z) {
        this.uid = uid;
        this.x = x;
        this.z = z;
    }

    public LazyChunk(Chunk bukkitChunk) {
        this(bukkitChunk.getWorld().getUID().toString(), bukkitChunk.getX(), bukkitChunk.getZ());
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public World getBukkitWorld() {
        return Bukkit.getWorld(uid);
    }

    public Chunk getBukkitChunk() {
        return getBukkitWorld().getChunkAt(x, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LazyChunk lazyChunk = (LazyChunk) o;
        return x == lazyChunk.x &&
                z == lazyChunk.z &&
                uid.equals(lazyChunk.uid);
    }
}
