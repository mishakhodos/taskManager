package com.example.task_manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString(exclude = { "createdAt", "updatedAt"})
@EqualsAndHashCode(exclude = { "createdAt", "updatedAt"})
@JsonIgnoreProperties({ "task", "author"})
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    @NotBlank(message = "text is mandatory")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    private Task task;

    @Column(nullable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private Date updatedAt;
}
