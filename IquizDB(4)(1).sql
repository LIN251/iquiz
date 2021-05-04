USE iquizdb;
DROP TABLE IF EXISTS Answer, Attempt, AttemptAnswer, Question, Quiz, Taker, User, UserPhoto;

CREATE TABLE User
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR(32) NOT NULL,
    password VARCHAR(256) NOT NULL,          /* To store Salted and Hashed Password Parts */
    first_name VARCHAR(32) NOT NULL,
    middle_name VARCHAR(32),
    last_name VARCHAR(32) NOT NULL,
    security_question_number INT NOT NULL,   /* Refers to the number of the selected security question */
    security_answer VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL,      
    authenticated BOOLEAN, 
    PRIMARY KEY (id)
);

CREATE TABLE UserPhoto
(
       id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
       extension ENUM('jpeg', 'jpg', 'png', 'gif') NOT NULL,
       user_id INT UNSIGNED,
       FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Quiz(
quiz_id INT PRIMARY KEY AUTO_INCREMENT,
user_id_fk INT UNSIGNED,
title VARCHAR(1024) NOT NULL,
publish boolean NOT NULL,
publish_at DATETIME NOT NULL,
time_limit INT,
access_code VARCHAR(1024) NOT NULL,
FOREIGN KEY (user_id_fk) REFERENCES User (id) 
);

CREATE TABLE Taker(
taker_id INT PRIMARY KEY AUTO_INCREMENT,
first_name VARCHAR(255) NOT NULL,
last_name VARCHAR(255) NOT NULL
);

CREATE TABLE Question(
question_id INT PRIMARY KEY AUTO_INCREMENT,
quiz_id_fk INT,
question_text VARCHAR(1024) NOT NULL,
question_point INT NOT NULL,
FOREIGN KEY (quiz_id_fk) REFERENCES Quiz (quiz_id) 
);

CREATE TABLE Answer(
answer_id INT PRIMARY KEY AUTO_INCREMENT,
question_id_fk INT,
answer_text VARCHAR(1024),
instructor_result boolean NOT NULL,
FOREIGN KEY (question_id_fk) REFERENCES Question (question_id) 
);

CREATE TABLE Attempt(
attempt_id INT PRIMARY KEY AUTO_INCREMENT,
taker_id_fk INT ,
quiz_id_fk INT ,
score INT NOT NULL,
FOREIGN KEY (taker_id_fk) REFERENCES Taker (taker_id),
FOREIGN KEY (quiz_id_fk) REFERENCES Quiz (quiz_id)
);

CREATE TABLE AttemptAnswer(
attemptanswer_id INT PRIMARY KEY AUTO_INCREMENT,
question_id_fk INT ,
answer_id_fk INT ,
attempt_id_fk INT ,
FOREIGN KEY (answer_id_fk) REFERENCES Answer (answer_id) ,
FOREIGN KEY (question_id_fk) REFERENCES Question (question_id) ,
FOREIGN KEY (attempt_id_fk) REFERENCES Attempt (attempt_id) 
);


INSERT INTO user (username, password, first_name,middle_name,last_name,security_question_number,security_answer,email) VALUES
( 'Administrator' , 'sha1:64000:18:TdMlKaRMng/tVxsyL8MwnJTwFxhvdBV8:L3iBt9XP4DuK+cfzqh4PIYcN', 'admin', 'admin', 'admin', 1, 'admin','admin@vt.edu');


INSERT INTO quiz (user_id_fk, title, publish, publish_at, time_limit, access_code) VALUES
( (SELECT id from user WHERE id=1) , 'sample test0', 0, '2021-04-01', 10, 'access_code0'),
( (SELECT id from user WHERE id=1) , 'sample test1', 0, '2021-04-02', 15, 'access_code1'),
( (SELECT id from user WHERE id=1) , 'sample test2', 1, '2021-04-03', 20, 'access_code2'),
( (SELECT id from user WHERE id=1) , 'sample test3', 1, '2021-04-04', 25, 'access_code3');


INSERT INTO question (quiz_id_fk, question_text, question_point) VALUES
( (SELECT quiz_id from quiz WHERE quiz_id=1) , 'What is the capital of America?', 5),
( (SELECT quiz_id from quiz WHERE quiz_id=1) , 'What is the capital of Singapore?', 5),
( (SELECT quiz_id from quiz WHERE quiz_id=1) , 'What is the capital of Korea?', 5),
( (SELECT quiz_id from quiz WHERE quiz_id=1) , 'What is the capital of India?', 5),
( (SELECT quiz_id from quiz WHERE quiz_id=1) , 'What is the capital of France?', 5),
( (SELECT quiz_id from quiz WHERE quiz_id=1) , 'What is the capital of England?', 5),
( (SELECT quiz_id from quiz WHERE quiz_id=2) , 'Name the slowest animal of the world.', 10),
( (SELECT quiz_id from quiz WHERE quiz_id=2) , 'A snail can sleep for how many years?', 10),
( (SELECT quiz_id from quiz WHERE quiz_id=2) , 'A group of lions is known as?', 10),
( (SELECT quiz_id from quiz WHERE quiz_id=2) , 'How many heart chambers a cockroach has?', 10),
( (SELECT quiz_id from quiz WHERE quiz_id=2) , 'Which bird is a universal symbol of peace?', 10),
( (SELECT quiz_id from quiz WHERE quiz_id=3) , 'Rabbits are born blind. Is that true or false?', 20),
( (SELECT quiz_id from quiz WHERE quiz_id=3) , 'An average ear of corn has odd number of rows.', 20),
( (SELECT quiz_id from quiz WHERE quiz_id=3) , 'Chocolate was once used as currency.', 20),
( (SELECT quiz_id from quiz WHERE quiz_id=3) , 'Carrots are only orange in color.', 20),
( (SELECT quiz_id from quiz WHERE quiz_id=4) , 'What is the best thing to drink to keep you hydrated?', 25),
( (SELECT quiz_id from quiz WHERE quiz_id=4) , 'How many teaspoons of sugar are in a can of Coke?', 25),
( (SELECT quiz_id from quiz WHERE quiz_id=4) , 'Citrus fruits are a great source of which vitamin?', 25),
( (SELECT quiz_id from quiz WHERE quiz_id=4) , 'What does the word yoga mean?', 25),
( (SELECT quiz_id from quiz WHERE quiz_id=4) , 'How many pounds of fat are needed to store 3500 calories?', 25),
( (SELECT quiz_id from quiz WHERE quiz_id=4) , 'Which area of the body has the largest bone?', 25);



INSERT INTO answer (question_id_fk,answer_text,instructor_result) VALUES
(1,'New York',0),
(1,'Chicago',0),
(1,'Boston',0),
(1,'WashingtonDC',1),
(2,'Daban',0),
(2,'Johor',0),
(2,'Singapore',1),
(2,'Budin',0),
(3,'Seoul',1),
(3,'Busan',0),
(3,'Daegu',0),
(3,'Incheon',0),
(4,'Mumbai',0),
(4,'New Delhi',1),
(4,'Chennai',0),
(4,'Pune',0),
(5,'Paris',1),
(5,'Lyon',0),
(5,'Nice',0),
(5,'Lille',0),
(6,'York',0),
(6,'Bristol',0),
(6,'Oxford',0),
(6,'London',1),
(7,'pig',0),
(7,'bear',0),
(7,'dear',0),
(7,'Three-toed Sloth',1),
(8,'1',0),
(8,'2',0),
(8,'3',1),
(8,'4',0),
(9,'orgnization',0),
(9,'Pride',1),
(9,'Friends',0),
(9,'Team',0),
(10,'7',0),
(10,'24',0),
(10,'12',1),
(10,'36',0),
(11,'Dove',1),
(11,'Crow',0),
(11,'Goose',0),
(11,'Turkey',0),
(12,'True',1),
(12,'False',0),
(13,'True',0),
(13,'False',1),
(14,'True',1),
(14,'False',0),
(15,'True',0),
(15,'False',1),
(16,'Coke',0),
(16,'Water',1),
(16,'Juice',0),
(16,'Diet Coke',0),
(17,'4',0),
(17,'6',0),
(17,'7',1),
(17,'8',0),
(18,'Vitamin A',0),
(18,'Vitamin B',0),
(18,'Vitamin C',1),
(18,'Vitamin D',0),
(19,'Anion',0),
(19,'Circle',0),
(19,'Turn',0),
(19,'Union',1),
(20,'One',1),
(20,'Two',0),
(20,'Three',0),
(20,'Four',0),
(21,'Arm',0),
(21,'Head',0),
(21,'Leg',1);