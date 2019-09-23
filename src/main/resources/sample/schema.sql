create schema sample_app;

create table sample_app.users (
	id                       serial primary key,
	username                 text not null unique,
	password                 text not null
);

create table sample_app.types (
	id                       serial primary key,
	timestamp_value          timestamp with time zone,
	date_value               date,
	decimal_value            numeric(6, 3)
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

create view test.types as
	select id as _id,
	       id as _display,
	       timestamp_value,
	       date_value,
	       decimal_value
	from   sample_app.types;
