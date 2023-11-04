-- For this software, you need a mySql database installed. 

-- In my system, I have its username password as 'root' & 'manager' respectively.
-- If you have any other, make sure you make changes in this code.

-- following are the queries for creating the database & its tables. Simply copy-paste them.

create database bill_DB;
use bill_DB;
create table products (Product_ID integer,Product_name varchar(20),varient_1 varchar(15),Price_1 integer,varient_2 varchar(15),Price_2 integer,varient_3 varchar(15),Price_3 integer);
insert into Products values
(1011,'Clinic+ Shampoo','sachet 6ml',1,'bottle 30ml',30,'bottle 80ml',52),
(1012,'Maggie','70gm',14,'140gm',28,'420gm',82),
(1013,'Kit-Kat','11gm',10,'18gm',20,'38gm',30),
(1014,'Navneet Notebook','140pages',60,'172pages',80,'null',0),
(1015,'Montex Pen','blue ballpen',10,'null',0,'null',10),
(1016,'Rice(Basmati)','kg',42,'null',0,'null',0),
(1017,'Wheat(Ajanta)','kg',40,'null',0,'null',0),
(1018,'Nandini Farsaan','1kg',60,'null',0,'null',0),
(1019,'Parle-G','100gm',10,'250gm',25,'800gm',50),
(1020,'Lays Potato','23gm',10,'40gm',20,'55gm',35),
(1021,'Calender','kalnirnay',35,'mahalakshmi',32,'null',0),
(1022,'Kisan Ketchup','100gm',15,'425gm',50,'1.1kg',150),
(1023,'Sugar','kg',35,'null',0,'null',0),
(1024,'Ruchi Gold','1 litre',106,'null',0,'null',0),
(1025,'Badshah Garam Masala','50gm',51,'100gm',98,'null',0),
(1026,'Lijjat Papad','200gm',72,'1kg',318,'null',0),
(1027,'Colgate','36gm',20,'100gm',70,'200gm',125),
(1028,'Chikki','100gm',40,'200gm',80,'null',0),
(1029,'Amul Butter','100gm',58,'200gm',114,'null',0),
(1030,'Godreg No.1','100gm',24,'null',0,'null',0),
(1031,'Washing Powder Nirma','500gm',30,'1kg',58,'null',0),
(1032,'Oreo','43gm',10,'113gm',35,'null',0),
(1033,'Satyam Wafers','30gm',10,'null',0,'null',0),
(1034,'Cadbury Dairy Milk','13gm',10,'24gm',20,'123gm',100);
create table bill (ID integer,product_name varchar(20),varient varchar(15),price integer,quantity integer,amount integer);
create table bill_book (bill_no integer primary key auto_increment,bill_date varchar(30),cus_name varchar(100),ph_no varchar(10),items integer,quantity integer,tot_amount integer,amount_paid integer,change_returned integer);