alter table  if exists et_transactions drop constraint pfk;
alter table  if exists et_transactions drop constraint ptk;
alter table  if exists et_categories drop constraint pfk;

drop table if exists et_users;
drop table if exists et_categories;
drop table if exists et_transactions;

drop database if exists expensetrackerdb;
drop user if exists expensetracker;

create user expensetracker with password 'password';
create database expensetrackerdb with template=template0 owner=expensetracker;
\connect expensetrackerdb;

alter default privileges grant all on tables to expensetracker;
alter default privileges grant all on sequences to expensetracker;

create table et_users(
user_id varchar(30) primary key not null,
first_name varchar(30) not null,
last_name varchar(30) not null,
email varchar(30) not null,
password varchar(100) not null
);

create table et_categories(
category_id varchar(30) primary key not null,
user_id varchar(30) not null,
title varchar(30),
description varchar(30)
);

alter table et_categories add constraint fk
foreign key (user_id) references et_users(user_id);

create table et_transactions(
transaction_id integer primary key not null,
category_id varchar(30) not null,
user_id varchar(30) not null,
amount numeric(10,2) not null,
note varchar(50) not null,
transaction_date bigint not null);

alter table et_transactions add constraint pfk
foreign key (user_id) references et_users(user_id);
alter table et_transactions add constraint ptk
foreign key (category_id) references et_categories(category_id);

create sequence et_transactions_seq increment 1 start 1000;