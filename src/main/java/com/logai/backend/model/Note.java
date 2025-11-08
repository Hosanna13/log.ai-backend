package com.logai.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "notes")
public class Note {
    @Id
    private String id;
    private String userId;
    private String title;
    private String description;
    private Date createdAt = new Date();
    private Date updatedAt = new Date();

    public void touch(){
        this.updatedAt = new Date();
    }
}