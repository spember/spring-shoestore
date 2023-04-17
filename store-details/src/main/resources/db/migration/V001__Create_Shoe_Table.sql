create table shoes (
    id uuid primary key,
    name varchar(256) not null,
    description text,
    price_in_cents integer not null,
    price_currency varchar(3) not null, -- e.g. EUR, USD
    constraint price_non_neg check (price_in_cents >= 0)

);