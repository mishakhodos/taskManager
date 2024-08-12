package com.example.task_manager.entity;

import com.example.task_manager.enums.Priority;
import com.example.task_manager.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"createdAt", "updatedBy", "updatedAt"})
@EqualsAndHashCode(exclude = {"createdAt", "updatedBy", "updatedAt"})
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String title;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_id", referencedColumnName = "id", nullable = false)
    private User updatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor_id", referencedColumnName = "id")
    private User executor;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @CreationTimestamp(source = SourceType.DB)
    private Date createdAt;

    @UpdateTimestamp(source = SourceType.DB)
    private Date updatedAt;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private List<Comment> comments;
}
