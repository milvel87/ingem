create database ingem;
\c ingem
create user ingem with encrypted password 'test1234';
grant all privileges on database ingem to ingem;
create table public.product
(
  id serial,
  code character varying(10) constraint code_size_check check (char_length(code) = 10),
  name character varying(255),
  price_hrk decimal(12,2),
  price_eur decimal(12,2),
  description character varying(255),
  is_available boolean,
  constraint id_pk primary key(id),
  constraint product_code_key unique (code)
)
with (
  oids=false
);
alter table public.product
  owner to ingem;