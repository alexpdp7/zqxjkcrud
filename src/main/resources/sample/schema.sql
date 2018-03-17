create schema sample_app;

create table sample_app.users (
	id                       serial primary key,
	username                 text not null unique,
	password                 text not null
);

insert into sample_app.users(username, password) values ('admin', 'admin');

create schema zqxjk;

create view zqxjk._users as
	select username, password
	from sample_app.users;

create view zqxjk.users as
	select id as _id,
	       username as _friendly,
	       username,
	       password
	from   sample_app.users;

set search_path to zqxjk;
