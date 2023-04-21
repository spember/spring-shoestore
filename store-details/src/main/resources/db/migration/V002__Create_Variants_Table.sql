create table variants(
    sku varchar(256) primary key,
    shoe_id uuid not null,
    label text not null,
    size varchar(128) not null,
    color varchar(128) not null,
     -- for now just size and color, but we could add additional things like materials
    CONSTRAINT fk_shoe
        FOREIGN KEY(shoe_id)
            REFERENCES shoes(id)
);

create index if not exists v_size on variants(size);
create index if not exists v_color on variants(color);