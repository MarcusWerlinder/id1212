package id1212.werlinder.marcus.homework3.common.dtoInfo;

import id1212.werlinder.marcus.homework3.server.model.FileDB;

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

    public FileStruct(FileDB file) {
        this.owner = file.getOwner().getId();
        this.filename = file.getName();
        this.size = file.getSize();
        this.publicAll = file.isPublicAccess();
        this.readable = file.isReadable();
        this.writable = file.isWritable();
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
