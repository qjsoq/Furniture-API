insert into furniture (id, title, price, availability, category, created_at, domain,
                       vendor_code, creator_id, number_of_reviews)
values (1, 'Taburetka', 7800, 'INSTOCK', 'CHAIR', now(), 'KITCHEN', '1235673', 7, 0),
       (2, 'Stol1', 5450, 'INSTOCK', 'TABLE', DATE_SUB(now(), INTERVAL 4 DAY), 'KITCHEN', '4238748', 7, 0),
       (3, 'Divan', 90000, 'INSTOCK', 'RECLINER', now(), 'LIVINGROOM', '4326746', 7, 0),
       (4, 'Shafa', 15000, 'INSTOCK', 'ARMOIRE', now(), 'BEDROOM', '9543422', 7, 0),
       (5, 'Krislo', 219787, 'OUTSTOCK', 'ARMCHAIR', now(), 'SITTINGROOM', '8563276', 7, 0),
       (6, 'Stol2', 60000, 'INSTOCK', 'TABLE', DATE_SUB(now(), INTERVAL 3 DAY), 'KITCHEN', '7891187', 7, 0),
       (7, 'Stol3', 7899, 'OUTSTOCK', 'TABLE', DATE_SUB(now(), INTERVAL 2 DAY), 'KITCHEN', '4329789', 7, 0),
       (8, 'Stol4', 12000, 'INSTOCK', 'TABLE', DATE_SUB(now(), INTERVAL 1 DAY), 'KITCHEN', '8983744', 7, 0);
