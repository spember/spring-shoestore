insert into shoes(id, name, description, price_in_cents, price_currency) values
     (gen_random_uuid(), 'Classic sandal', '', 2500, 'USD'),
     (gen_random_uuid(), 'Spring Sneaker', '', 9900, 'EUR')
;



--     id uuid primary key,
--     name varchar(256) not null,
--     description text,
--     price_in_cents integer not null,
--     price_currency varchar(3) not null -- e.g. EUR, USD