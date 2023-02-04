alter table auto_post
add column if not exists status boolean not null default false;