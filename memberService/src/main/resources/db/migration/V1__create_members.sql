create table members(
    id serial primary key,
    name varchar(255),
    email varchar(255)
);

insert into members(name, email) values('test', 'test@localhost');
