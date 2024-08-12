package com.example.task_manager.specification;

import com.example.task_manager.entity.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<Task> hasTitle(String title) {
        return (root, query, cb) -> title == null ? cb.conjunction() : cb.like(cb.upper(root.get("title")), "%" + title.toUpperCase() + "%");
    }

    public static Specification<Task> hasOwner(Long ownerId) {
        return (root, query, cb) -> ownerId == null ? cb.conjunction() : cb.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<Task> hasExecutor(String executorId) {
        return (root, query, cb) -> executorId == null ? cb.conjunction() : cb.equal(root.get("executor").get("id"), executorId);
    }

    public static Specification<Task> hasStatus(String status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status"), status.toUpperCase());
    }

    public static Specification<Task> hasPriority(String priority) {
        return (root, query, cb) -> priority == null ? cb.conjunction() : cb.equal(root.get("priority"), priority.toUpperCase());
    }
}
