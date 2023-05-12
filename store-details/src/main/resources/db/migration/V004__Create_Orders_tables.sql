create table orders (
    id uuid primary key ,
    user_email text not null,
    time_placed timestamp with time zone not null,
    total_price int
);
create index if not exists orders_user on orders(user_email);



create table order_line_items(
    order_id uuid not null,
    position int not null,
    sku varchar(256) not null,
    price_per int not null,
    serial_numbers text[],

    constraint fk_line_order
        foreign key (order_id)
            references orders(id),

    constraint fk_line_sku
        foreign key (sku)
            references  variants(sku)

);