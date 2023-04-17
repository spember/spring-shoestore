create table variants(
    sku varchar(256) primary key,
    label text not null,
    size varchar(128) not null,
    color varchar(128) not null
     -- for now just size and color, but we could add additional things like materials
);

create index if not exists v_size on variants(size);
create index if not exists v_color on variants(color);