create table credentials(
    id serial primary key,
    user_id int,
    password varchar(255)
);

insert into credentials(user_id, password) values(1, 'test@localhost');
