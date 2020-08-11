delete from pcr_axis;
delete from pcr_princess;
delete from pcr_activity;
delete from pcr_boss;

insert into pcr_princess(name, priority, type, img)
values ('布丁', 2, 0, 'base64'),
       ('asd', 1, 0, 'base64'),
       ('asd', 1, 0, 'base64');

insert into pcr_activity(name, begin_date, end_date)
values ('巨蟹座', '2020-08-02 00:00:00', '2020-08-12 23:30:00'),
       ('金牛座', '2020-07-13 00:00:00', '2020-07-22 23:30:00');

insert into pcr_boss(activity_id, name, is_furious, round)
values (1, '', 1, 1),
       (1, '', 0, 1);

insert into pcr_axis(boss_id, damage_amount, remark, img_path, create_time)
values (1, 70, 'asd', '', now()),
       (1, 70, 'asd', '', now());