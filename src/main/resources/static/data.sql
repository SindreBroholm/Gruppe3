insert into user(Email, Firstname, Lastname, Password) values
('Sindreset@gmail.com', 'Sindre Broholm', 'Sæther', 'Sindre123'),
('marit_jp@hotmail.com', 'Marit', 'Pedersen', 'Marit123'),
('fredrsan@gmail.com', 'Fredrik', 'Sandbakk', 'Fredrik123'),
('helgerannem@gmail.com', 'Helge', 'Rannem', 'Helge123');

insert into board(name) values
('Fastis test Board'),
('Fastis Subtest Board');

insert into membership_type(name) values
('admin'),
('leader'),
('member'),
('follower');

insert into user_role(user_id, board_id, membership_type, numb_of_members) values
(1, 1, 'leader', 1),
(1, 2, 'follower', 1),
(2, 1, 'member', 1),
(2, 2, 'follower', 1),
(3, 1, 'follower', 1),
(3, 2, 'leader', 1),
(4, 1, 'member', 1),
(4, 2, 'follower', 1);

insert into events(Board_Id, Message, Datetime_From, Datetime_To, Datetime_Created, Event_Type, Location, Name) values
(1, 'Hælg', '2020-10-16 16:00:00', '2020-10-16 17:00:00', '2020-10-12 12:00:00', 'follower', 'Bratørkaia 17A', 'Skål');