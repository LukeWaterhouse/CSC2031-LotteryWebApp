CREATE TABLE userAccounts (
Firstname varchar(50) NOT NULL,
Lastname varchar(50) NOT NULL, 
Email varchar(50) NOT NULL,
Phone varchar(50) NOT NULL,
Username varchar(50) NOT NULL,
Pwd varchar(200) NOT NULL,
Userrole varchar(10) NOT NULL,
PRIMARY KEY (Username, Pwd)
);

CREATE TABLE winningDraws (
Draw varchar(50) NOT NULL,
PRIMARY KEY (Draw)
);

INSERT INTO userAccounts 
('Luke', 'Waterhouse', 'Luke@hotmail.co.uk', '11-1111-1111111', 'LukeW', 'Password','user');


INSERT INTO winningDraws VALUES
('01-23-45-67-89-10');



