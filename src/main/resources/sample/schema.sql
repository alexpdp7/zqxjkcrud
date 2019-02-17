create schema sample_app;

create table sample_app.users (
	id                       serial primary key,
	username                 text not null unique,
	password                 text not null
);

insert into sample_app.users(username, password) values ('admin', '{noop}admin');

create schema test;

create view test._users as
	select username, password
	from sample_app.users;

create view test.users as
	select id as _id,
	       username as _display,
	       username,
	       password
	from   sample_app.users;
