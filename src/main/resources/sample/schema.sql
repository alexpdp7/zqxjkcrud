create schema sample_app;

create table sample_app.users (
	id                       serial primary key,
	username                 text not null unique,
	password                 text not null
);

insert into sample_app.users(username, password) values ('admin', 'admin');

create schema test;

create view test._users as
	select username, password
	from sample_app.users;

create view test.users as
	select id as _id,
	       username as _friendly,
	       username,
	       password
	from   sample_app.users;
