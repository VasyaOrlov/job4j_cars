create table if not exists owner_history (
id serial primary key,
driver_id int not null references driver(id),
car_id int not null references car(id),
startAt timestamp,
endAt timestamp
);