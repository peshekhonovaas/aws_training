package com.awsbasics.simpleapp.clientmodel;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImageClientModel {

    private long id;

    private String name;

    private long size;

    private String fileExtension;

    private LocalDateTime updatedAt;

    private byte[] bitmap;

    private ObjectMetadata objectMetadata;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public byte[] getBitmap() {
        return bitmap;
    }

    public void setBitmap(byte[] bitmap) {
        this.bitmap = bitmap;
    }

    public ObjectMetadata getObjectMetadata() {
        return objectMetadata;
    }

    public void setObjectMetadata(ObjectMetadata objectMetadata) {
        this.objectMetadata = objectMetadata;
    }
}
