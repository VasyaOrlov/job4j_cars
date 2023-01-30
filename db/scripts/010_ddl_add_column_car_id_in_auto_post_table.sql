alter table auto_post
add column if not exists car_id int unique references car(id);