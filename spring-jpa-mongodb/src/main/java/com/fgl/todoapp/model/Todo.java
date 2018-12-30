package com.fgl.todoapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
 
@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Todo {

    @Id
    private String id;

    @NotBlank(message = "{todo.name.NotBlank}")
    @Size(max = 100, message = "{todo.name.Size}")
    private String name;

    @NotNull
    @Builder.Default
    private Boolean completed = Boolean.FALSE;

    @NotNull
    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();
}
