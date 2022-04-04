drop database apa;
CREATE DATABASE	apa CHARACTER SET utf8 COLLATE utf8_unicode_ci;
use apa;

create table role (
	id int unsigned not null auto_increment,
    name nvarchar(50),
    primary key(id)
);

create table account_level (
	id int unsigned not null auto_increment,
    `name` nvarchar(50) unique,
    primary key(id)
);

create table `user`(
	id int unsigned not null auto_increment,
    phone_number varchar(20) unique,
    `password` varchar(200),
    full_name nvarchar(100),
    avatar varchar(500),
    `address` varchar(1000),
    `status` char(1),
    role_id int unsigned,
    account_level_id int unsigned,
    primary key(id),
    foreign key (role_id) references `role`(id) on delete restrict,
    foreign key (account_level_id) references `account_level`(id) on delete restrict
);

create table `parking_space` (
  	id int unsigned not null auto_increment,
    name nvarchar(100),
    `address` varchar(1000),
    description varchar(1000),
    image varchar(1000),
    `status` char(1),
    create_at datetime,
    updated_at datetime,
    owner_id int unsigned,
    primary key(id),
    foreign key (owner_id) references `user`(id) on delete restrict
);

create table `parking_space_attendant` (
    user_id int unsigned not null,
    parking_id int unsigned not null,
    `status` char(1),
    create_at datetime,
    updated_at datetime,
    primary key(user_id, parking_id),
    CONSTRAINT `Constr_parking_space_attendant_user_fk`
        FOREIGN KEY `user_fk` (`user_id`) REFERENCES `user` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `Constr_parking_space_attendant_parking_space_fk`
        FOREIGN KEY `parking_space_fk` (`parking_id`) REFERENCES `parking_space` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
);


create table `qr_code` (
  	id int unsigned not null auto_increment,
    code varchar(1000),
    `status` char(1),
    create_at datetime,
    updated_at datetime,
    primary key(id),
    parking_id int unsigned,
    foreign key (parking_id) references `parking_space`(id) on delete restrict
);

create table `vehicle` (
  	id int unsigned not null auto_increment,
    plate_number nvarchar(100),
    type nvarchar(100),
    create_at datetime,
    primary key(id)
);


create table `parking_reservation` (
  	id int unsigned not null auto_increment,
    name nvarchar(100),
    attachment varchar(1000),
    `status` char(1),
    create_at datetime,
    updated_at datetime,
    primary key(id),
    code_id int unsigned,
    parking_id int unsigned,
    vehicle_id int unsigned,
    foreign key (code_id) references `qr_code`(id) on delete restrict,
    foreign key (parking_id) references `parking_space`(id) on delete restrict,
    foreign key (vehicle_id) references `vehicle`(id) on delete restrict
);

create table `parking_reservation_activity` (
    user_id int unsigned not null,
    parking_reservation_id int unsigned not null,
    type char(1),
    create_at datetime,
    primary key(user_id, parking_reservation_id),
    CONSTRAINT `Constr_parking_reservation_activity_user_fk`
        FOREIGN KEY `user_fk` (`user_id`) REFERENCES `user` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `Constr_parking_reservation_activity_parking_reservation_fk`
        FOREIGN KEY `parking_reservation_fk` (`parking_reservation_id`) REFERENCES `parking_reservation` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
);




