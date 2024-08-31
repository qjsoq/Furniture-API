ALTER TABLE warehouse_storage
    DROP FOREIGN KEY FKqa5vjpwt80ahdnyj1nhxs1qbm;

ALTER TABLE warehouse_storage
    ADD CONSTRAINT FKqa5vjpwt80ahdnyj1nhxs1qbm
        FOREIGN KEY (warehouse_id)
            REFERENCES warehouse (id)
            ON DELETE CASCADE;
ALTER TABLE warehouse_storage
    DROP FOREIGN KEY FK2xxglyarsipgflg3l6g234a5t;

ALTER TABLE warehouse_storage
    ADD CONSTRAINT FK2xxglyarsipgflg3l6g234a5t
        FOREIGN KEY (furniture_id)
            REFERENCES furniture (id)
            ON DELETE CASCADE;

insert into warehouse_storage(warehouse_id, quantity, furniture_id)
values (2, 10, 1),
       (2, 0, 2),
       (2, 1, 3),
       (2, 4, 4);