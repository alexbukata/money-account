create table if not exists ACCOUNT (
    id int PRIMARY KEY,
    customerName varchar(255),
    amount decimal
);

insert into ACCOUNT values (1, 'Aleksandr Bukata', 1000.23);
insert into ACCOUNT values (2, 'Anonymous person', 10000.88);
