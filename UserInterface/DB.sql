/* Sqript for creating db and insert test data*/

create database if not exists cinemabd;

use cinemabd;

CREATE TABLE if not exists CINEMA(id INT AUTO_INCREMENT NOT NULL, name VARCHAR(20) NOT NULL,
									address VARCHAR(45) NOT NULL, PRIMARY KEY(id) );
							
                            
CREATE TABLE if not exists FILM(id INT AUTO_INCREMENT NOT NULL, name VARCHAR(20) NOT NULL,
								rating INT, description VARCHAR(45) NOT NULL, genre VARCHAR(20) NOT NULL,
                                date DATE NOT NULL,PRIMARY KEY(id) );
                                
CREATE TABLE if not exists CINEMA_FILM(cinema_film_id INT AUTO_INCREMENT NOT NULL, cinema_id INT NOT NULL,
										film_id INT NOT NULL, PRIMARY KEY(cinema_film_id),
                                        FOREIGN KEY(cinema_id) REFERENCES CINEMA(id),
                                        fOREIGN KEY(film_id) REFERENCES FILM(id) );
                                        
CREATE TABLE if not exists ACTOR(id INT AUTO_INCREMENT NOT NULL, name VARCHAR(40) NOT NULL, PRIMARY KEY (id) );

CREATE TABLE if not exists FILM_ACTOR(film_actor_id INT AUTO_INCREMENT NOT NULL, actor_id INT NOT NULL,
										film_id INT NOT NULL, PRIMARY KEY(film_actor_id),
                                        FOREIGN KEY(actor_id) REFERENCES ACTOR(id),
                                        fOREIGN KEY(film_id) REFERENCES FILM(id) );
                                        
CREATE TABLE if not exists USER(id INT AUTO_INCREMENT NOT NULL, login VARCHAR(20) NOT NULL,
								password VARCHAR(20) NOT NULL, PRIMARY KEY(id) );
                                
CREATE TABLE if not exists FILM_COMMENT(film_comment_id INT AUTO_INCREMENT NOT NULL,
										comment VARCHAR(140) NOT NULL, user_id INT NOT NULL,film_id INT NOT NULL,
                                        rating INT NOT NULL, likes INT NOT NULL, dislikes INT NOT NULL,
                                        PRIMARY KEY(film_comment_id), 
                                        fOREIGN KEY(film_id) REFERENCES FILM(id) );
                                        
create Table if not exists comment_like (id int auto_increment not null, liked int, user_id int not null,
										comment_id int not null, FOREIGN key(user_id) references user(id),
                                        foreign key(comment_id) references film_comment(film_comment_id),
                                        primary key(id));



/* Creating testing data */

insert into user (login, password) values ("admin", "admin");
insert into user (login, password) values ("user", "user");

insert into cinema (name, address) values ("Imax", "SPB");
insert into film (name, rating, description, genre, date ) values ("Interstellar", 0, "Film about space", "fantastic", "2014/11/19");
insert into cinema_film(cinema_id, film_id) values (1,1);
insert into actor(name) values ("Kenny");
insert into film_actor(actor_id, film_id) values (1,1);
insert into actor(name) values ("Kyle");
insert into film_actor(actor_id, film_id) values (2,1);
insert into film (name,rating,description,genre,date) values ("Pinguins", 0, "film about Pingiuns", "cartoon", "2014-11-27");
insert into cinema_film(cinema_id, film_id) values (1,2);
insert into actor(name) values ("Capral");
insert into film_actor(actor_id, film_id) values (3,2);
insert into film (name,rating,description,genre,date) values ("Matrix", 0, "virtual reality", "fantasy", "2001-06-15");
insert into cinema_film(cinema_id, film_id) values (1,3);
insert into film_actor(actor_id, film_id) values (3,3);
insert into actor(name) values ("Stan");
insert into film_actor(actor_id, film_id) values (4,3);
insert into actor(name) values ("Kenny West");
insert into film_actor(actor_id, film_id) values (5,3);
insert into cinema (name, address) values ("Plaza", "SPB");
insert into cinema_film(cinema_id, film_id) values (2,1);
insert into cinema_film(cinema_id, film_id) values (2,2);
insert into cinema_film(cinema_id, film_id) values (2,3);
insert into cinema (name, address) values ("CinemaMAX", "Moscow");
insert into cinema_film(cinema_id, film_id) values (3,1);
insert into film (name,rating,description,genre,date) values ("Hobbit", 0, " Brave Hobbit finds adventure", "fantastic", "2014.12.11");
insert into cinema_film(cinema_id, film_id) values (3,4);
insert into actor(name) values ("Martin Freeman");
insert into film_actor(actor_id, film_id) values (6,4);
insert into film_actor(actor_id, film_id) values (1,4);
insert into cinema_film(cinema_id, film_id) values (3,2);
insert into cinema_film(cinema_id, film_id) values (3,3);

insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values ("Very Interesting film", 1, 10, 0, 0, 1);
insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values ("Very cool. I like it", 1, 10, 0, 0, 1);
insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values ("Oh my good. This idea is beautiful", 1, 9, 0, 0, 1);
insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values ("Boring film.", 1, 6, 0, 0, 1);
update film set rating = 8 where id = 1;

insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values ("Hahahaha. Its funny", 2, 8, 0, 0, 1);
insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values ("My favourite cartoon", 2, 9, 0, 0, 1);
insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values ("I like it", 2, 8, 0, 0, 1);
insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values (":)", 2, 7, 0, 0, 1);
update film set rating = 8 where id = 2;

insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values ("Actors is very nice", 3, 9, 0, 0, 1);
insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values ("Best part of this trilogy", 3, 9, 0, 0, 1);
insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values ("I hate matrix", 3, 1, 0, 0, 1);
insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values ("Never seen so good movie", 3, 10, 0, 0, 1);
update film set rating = 7 where id = 3;

insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values ("Peter Jackson is very cool", 4, 10, 0, 0, 1);
insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values ("I like Hobbit and Lord of the Rings", 4, 10, 0, 0, 1);
insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values ("More films about hobbits", 4, 9, 0, 0, 1);
insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values ("Hmm...i waited more...", 4, 6, 0, 0, 1);
update film set rating = 8 where id = 4;



