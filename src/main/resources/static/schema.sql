create schema fastis;

create table user(
    Id int auto_increment unique,
    Email varchar(320) not null unique,
    Firstname varchar(100) not null,
    Lastname varchar(100) not null,
    Password varchar(300) not null,
    primary key (Id)
);

drop table board;
drop table user_role;
drop table events;
drop table notification;



create table board(
    Id int unique auto_increment,
    name varchar(100) not null unique,
    primary key (Id)
);

create table user_role(
    User_Id int not null,
    Board_Id int not null,
    membership_Type varchar(56) not null,
    Numb_Of_Members int not null,
    primary key (User_Id, Board_Id),
    FOREIGN KEY (User_Id) references user(Id) ON DELETE CASCADE,
    foreign key (Board_Id) references board(Id) ON DELETE CASCADE,
    foreign key (membership_Type) references membership_Type(name)
);

create table events(
    Id int unique auto_increment,
    Board_Id int not null,
    Message varchar(5000) not null,
    Datetime_From datetime not null,
    Datetime_To datetime not null,
    Datetime_Created datetime not null,
    Event_Type varchar(56) not null,
    Location varchar(150),
    Name varchar(150) not null,
    foreign key (Event_Type) references membership_Type(name),
    foreign key (Board_Id) references board(Id) ON DELETE CASCADE
);

create table notification
(
    Id int auto_increment unique not null,
    Board_Id int unique not null,
    Message varchar(5000) not null,
    Datetime_Created datetime not null,
    notification_Type varchar(56) not null,
    primary key (Id),
    foreign key (Board_Id) references board(Id) ON DELETE CASCADE,
    foreign key (notification_Type) references membership_Type(name)
);

create table  membership_Type(
    name varchar(56) not null,
    primary key (name)
);
