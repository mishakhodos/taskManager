insert into users (id, full_name, email, password) values (99991, 'user1', 'test@test.com', '$2a$10$NLslXNLJHNA0HfC4llDuRebIMhvXJP5pl8IzI9sqID/RC3UYf.LjK');
insert into users (id, full_name, email, password) values (99992, 'user2', 'tet2@test.com', '$2a$10$NLslXNLJHNA0HfC4llDuRebIMhvXJP5pl8IzI9sqID/RC3UYf.LjK');

insert into task (id, title, description, status, priority, owner_id, updated_by_id) values ('0e543417-02b0-4681-8516-5955b2150543', 'task1', 'task1desc', 'PENDING', 'LOW', 99991, 99991);
insert into task (id, title, description, status, priority, owner_id, updated_by_id) values ('0e543417-02b0-4681-8516-4955b2150542', 'task2', 'task2desc', 'PENDING', 'LOW', 99991, 99991);
insert into task (id, title, description, status, priority, owner_id, updated_by_id) values ('0e543417-02b0-4681-8516-2955b2150541', 'task3', 'task3desc', 'PENDING', 'LOW', 99992, 99992);