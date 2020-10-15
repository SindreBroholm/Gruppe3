create schema fastis;

create table fastis.user(
    Id int auto_increment unique,
    Email varchar(320) not null unique,
    Firstname varchar(100) not null,
    Lastname varchar(100) not null,
    Password varchar(300) not null,
    Phone_Number varchar(15),
    primary key (Id)
);


create table fastis.board(
    Id int unique auto_increment,
    name varchar(100) not null unique,
    primary key (Id),
    contact_Name varchar(100) not null,
    contact_Number varchar(15),
    contact_Email varchar(320) not null ,
    homepage varchar(100)
);

create table  fastis.membership_Type(
    name varchar(56) not null,
    primary key (name)
);

create table fastis.user_role(
    User_Id int not null,
    Board_Id int not null,
    membership_Type varchar(56) not null,
    Numb_Of_Members int not null,
    primary key (User_Id, Board_Id),
    FOREIGN KEY (User_Id) references user(Id) ON DELETE CASCADE,
    foreign key (Board_Id) references board(Id) ON DELETE CASCADE,
    foreign key (membership_Type) references membership_Type(name)
);

INSERT INTO fastis.membership_Type (name)
VALUES ('FOLLOWER'), ('MEMBER'), ('LEADER'), ('ADMIN');

create table fastis.events(
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

create table fastis.notification
(
    Id int auto_increment unique not null,
    Board_Id int not null,
    Message varchar(5000) not null,
    Datetime_Created datetime not null,
    notification_Type varchar(56) not null,
    primary key (Id),
    foreign key (Board_Id) references board(Id) ON DELETE CASCADE,
    foreign key (notification_Type) references membership_Type(name)
);