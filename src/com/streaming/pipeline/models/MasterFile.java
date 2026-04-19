package com.streaming.pipeline.models;

public class MasterFile {
    private final String filename;
    private final long sizeInBytes;
    private final String format;

    public MasterFile(String filename, long sizeInBytes, String format) {
        this.filename = filename;
        this.sizeInBytes = sizeInBytes;
        this.format = format;
    }

    public String getFilename() {
        return filename;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    public String getFormat() {
        return format;
    }

    @Override
    public String toString() {
        return "MasterFile{" +
                "filename='" + filename + '\'' +
                ", size=" + (sizeInBytes / (1024 * 1024 * 1024.0)) + " GB" +
                ", format='" + format + '\'' +
                '}';
    }
}
