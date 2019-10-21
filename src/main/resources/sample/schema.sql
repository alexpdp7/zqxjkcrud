drop schema if exists sample_app cascade;

create schema sample_app;

create table sample_app.users (
	id                       serial primary key,
	username                 text not null unique,
	password                 text not null
);

insert into sample_app.users(username, password) values ('admin', '{noop}admin');

create table sample_app.types (
	id                       serial primary key,
	timestamp_value          timestamp with time zone,
	date_value               date,
	decimal_value            numeric(6, 3),
	boolean_value            boolean
);

insert into sample_app.types(timestamp_value, date_value, decimal_value, boolean_value) values (now(), now(), 33.4, true);

create table sample_app.custom_widgets (
	id                       serial primary key,
	textarea                 text
);

insert into sample_app.custom_widgets(textarea) values ('foo
bar');

create table sample_app.sorting (
    id                       serial primary key,
	value                    varchar
);

insert into sample_app.sorting(value) select random() * 1000 as value from generate_series(1, 1000);

drop schema if exists test cascade;

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
	       decimal_value,
	       boolean_value
	from   sample_app.types;

create view test.custom_widgets as
	select id as _id,
	       id as _display,
	       textarea
	from   sample_app.custom_widgets;

create view test.sorting as
	select id as _id,
	       value as _display,
	       value as value
	from   sample_app.sorting;

create table _tables (
	name                     text primary key,
	default_sort             text[]
);

insert into _tables(name, default_sort) values ('sorting', '{"value", "desc"}');

create table _columns (
	table_name               text,
	name                     text,
	widget                   text,
	primary key(table_name, name)
);

insert into _columns(table_name, name, widget) values ('custom_widgets', 'textarea', 'textarea');