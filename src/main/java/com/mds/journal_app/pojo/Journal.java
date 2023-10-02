package com.mds.journal_app.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document("journal")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Journal {
    @JsonIgnore
    @Id
    String id;
    @Indexed(unique = true)
    String title;
    String description;
    @CreatedDate
    @Field("createdAt")
    Instant createdAt;
    @LastModifiedDate
    @Field("updatedAt")
    Instant updatedAt;
}
