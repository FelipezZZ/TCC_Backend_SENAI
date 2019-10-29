

create database projeto;

use projeto;

create table pessoa(
cod_pessoa integer primary key not null auto_increment,
login varchar(50)not null unique,
nickname varchar(50) not null,
senha varchar(50)not null,
tipoperfil integer(3) not null ,
verificado boolean not null,
ident varchar(20),
anonimo boolean not null,
registro date not null,
primeiroAcesso boolean
);

create table admins(
cod_admin integer primary key auto_increment,
login varchar(50) not null unique,
senha varchar(50) not null

);

create table perfps(
 cod_pessoa integer not null,
 constraint fkCod_pessoa foreign key (cod_pessoa) references pessoa(cod_pessoa)
 

);

insert into pessoa values(0,"jooj","kok","123","1",true,"xxxxx.12333",false,false);
SELECT nickname FROM pessoa WHERE login = "jooj" AND senha = 123 ;