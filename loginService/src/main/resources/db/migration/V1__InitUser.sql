drop table if exists "user_role";
drop table if exists "user";
create table "user" (
   username varchar(255) not null,
   password varchar(255),
   is_enabled boolean not null,
   constraint pk_user primary key (username)
);

create table "user_role" (
  username varchar(255) not null,
   roles varchar(255)
);

alter table "user_role" add CONSTRAINT fk_user_role_on_user FOREIGN KEY (username) REFERENCES "user" (username);