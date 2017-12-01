package id1212.werlinder.marcus.homework3.common.dtoInfo;

import java.io.Serializable;

public class FileStruct implements Serializable {
    private final long owner;
    private final String filename;
    private final long size;
    private final boolean publicAll;
    private final boolean readable;
    private final boolean writable;

    public FileStruct(long owner, String filename, long size, boolean publicAll, boolean readable, boolean writable) {
        this.owner = owner;
        this.filename = filename;
        this.size = size;
        this.publicAll = publicAll;
        this.readable = readable;
        this.writable = writable;
    }

    public long getOwner() {
        return owner;
    }

    public String getFilename() {
        return filename;
    }

    public long getSize() {
        return size;
    }

    public boolean isPublicAll() {
        return publicAll;
    }

    public boolean isReadable() {
        return readable;
    }

    public boolean isWritable() {
        return writable;
    }

}
