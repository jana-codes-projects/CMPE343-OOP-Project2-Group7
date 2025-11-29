CREATE DATABASE Project2Database
USE Project2Database

CREATE TABLE users (
	user_id INT NOT NULL auto_increment,
	username VARCHAR(30) NOT NULL UNIQUE,
	password_hash VARCHAR(255) NOT NULL,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	user_role VARCHAR(25) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (user_id)
);

CREATE TABLE contacts (
	contact_id INT NOT NULL AUTO_INCREMENT,
	first_name VARCHAR(50) NOT NULL,
	middle_name VARCHAR(50),
	last_name VARCHAR(50) NOT NULL,
	nickname VARCHAR(40) NOT NULL,
	phone_primary VARCHAR(20) NOT NULL UNIQUE,
	phone_secondary VARCHAR(20),
	email VARCHAR(100) NOT NULL UNIQUE,
	linkedin_url VARCHAR(150),
	birth_date DATE NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (contact_id)
);

INSERT INTO users (username, password_hash, first_name, last_name, user_role)
VALUES	('tt', SHA2('tt', 256), 'Kaan', 'Yılmaz', 'TESTER'),
		    ('jd', SHA2('jd', 256), 'Nur', 'Kumbasar', 'JUNIOR_DEVELOPER'),
		    ('sd', SHA2('sd', 256), 'Emir', 'Gökdemir', 'SENIOR_DEVELOPER'),
		    ('man', SHA2('man', 256), 'Jana', 'Albarazi', 'MANAGER');

INSERT INTO contacts (first_name, middle_name, last_name, nickname, phone_primary, phone_secondary, email, linkedin_url, birth_date, created_at, updated_at) 
VALUES	('Sophia', 'Marie', 'Johnson', 'sophie', '+1-555-264-9785', NULL, 'sophia.johnson@yahoo.com', 'https://linkedin.com/in/sophiajohnson', '1992-03-15', NOW(), NOW()),
		('Mohammed', 'Ahmed', 'Hassan', 'mo', '+20-100-123-4567', '+20-111-987-6543', 'mohammed.hassan@gmail.com', NULL, '1988-07-22', NOW(), NOW()),
	    ('Mustafa', 'Kemal', 'Öztürk', 'musti', '+90-534-567-89-01', NULL, 'mustafa.ozturk@hotmail.com', NULL, '1990-05-10', NOW(), NOW()),
	    ('Yuki', 'Satoshi', 'Tanaka', 'yukitan', '+81-80-1234-5678', '+81-90-9876-5432', 'yuki.tanaka@yahoo.com', NULL, '1990-12-30', NOW(), NOW()),
		('Alexander', 'James', 'Smith', 'alex', '+44-7700-123456', NULL, 'alex.smith@protonmail.com', 'https://linkedin.com/in/alexsmith', '1987-05-18', NOW(), NOW()),
	    ('Sude', 'Naz', 'Erdem', 'sudenaz', '+90-545-876-54-32', '+90-534-123-09-87', 'sude.erdem@outlook.com', 'https://linkedin.com/in/sudeerdem', '1999-06-07', NOW(), NOW()),
	    ('Lucas', 'André', 'Silva', 'luck', '+55-11-98765-4321', '+55-11-91234-5678', 'lucas.silva@gmail.com', 'https://linkedin.com/in/lucassilva', '1991-02-14', NOW(), NOW()),
		('Aisha', NULL, 'Patel', 'aish', '+91-98-7654-3210', NULL, 'aisha.patel@outlook.com', NULL, '1994-06-07', NOW(), NOW()),
	    ('Dmitri', 'Ivanovich', 'Petrov', 'dima', '+7-916-123-45-67', NULL, 'dmitri.petrov@protonmail.com', 'https://linkedin.com/in/dmitripetrov', '1989-10-12', NOW(), NOW()),
	    ('Isabella', 'Grace', 'Anderson', 'bella', '+1-555-876-5432', '+1-555-234-1098', 'isabella.anderson@gmail.com', NULL, '1996-04-03', NOW(), NOW()),

		('Ali', NULL, 'Koç', 'alikoç', '+90-556-234-56-78', NULL, 'ali.koc@gmail.com', NULL, '1991-10-30', NOW(), NOW()), 
	    ('Burak', 'Selim', 'Yıldız', 'burakyildiz', '+90-546-123-78-90', '+90-212-987-65-43', 'burak.yildiz@gmail.com', NULL, '1992-11-05', NOW(), NOW()),
	    ('Zeynep', 'Sude', 'Yılmaz', 'zeyno', '+90-532-123-45-67', NULL, 'zeynep.yilmaz@yahoo.com', 'https://linkedin.com/in/zeynepyilmaz', '1995-03-15', NOW(), NOW()),
    	('Wei', 'Ming', 'Chen', 'weichen', '+86-138-2345-6789', NULL, 'wei.chen@hotmail.com', 'https://linkedin.com/in/weichen', '1990-04-15', NOW(), NOW()),
	    ('Rajesh', 'Kumar', 'Sharma', 'raj', '+91-99-8765-4321', NULL, 'rajesh.sharma@protonmail.com', 'https://linkedin.com/in/rajeshsharma', '1990-03-21', NOW(), NOW()),
        ('Chloe', 'Elizabeth', 'Brown', 'chlo', '+1-555-432-1098', NULL, 'chloe.brown@gmail.com', NULL, '1995-12-09', NOW(), NOW()),
        ('Ahmed', 'Mohammed', 'Ibrahim', 'ahmi', '+20-122-987-6543', '+20-100-555-1234', 'ahmed.ibrahim@gmail.com', 'https://linkedin.com/in/ahmedibrahim', '1987-09-16', NOW(), NOW()),
        ('Sakura', NULL, 'Yamamoto', 'saku', '+81-70-9876-5432', NULL, 'sakura.yamamoto@outlook.com', NULL, '1994-05-28', NOW(), NOW()),
        ('Zehra', NULL, 'Polat', 'zehracığım', '+90-557-123-45-67', NULL, 'zehra.polat@gmail.com', 'https://linkedin.com/in/zehrapolat', '1996-08-12', NOW(), NOW()),
        ('Priya', 'Devi', 'Reddy', 'priyu', '+91-97-2345-6789', '+91-98-1111-2222', 'priya.reddy@hotmail.com', NULL, '1992-11-23', NOW(), NOW()),
        
        ('Noah', 'Benjamin', 'Davis', 'noey', '+1-555-789-0123', NULL, 'noah.davis@gmail.com', 'https://linkedin.com/in/noahdavis', '1989-02-05', NOW(), NOW()),
        ('Chinwe', 'Ngozi', 'Okafor', 'chinny', '+234-80-1624-7859', NULL, 'chinwe.okafor@gmail.com', 'https://linkedin.com/in/chinweokafor', '1993-05-17', NOW(), NOW()),
        ('Amara', 'Nneka', 'Okonkwo', 'ama', '+234-80-1234-5678', NULL, 'amara.okonkwo@outlook.com', 'https://linkedin.com/in/amaraokonkwo', '1993-10-17', NOW(), NOW()),
        ('Jian', 'Hua', 'Li', 'jianli', '+86-137-5555-6666', NULL, 'jian.li@outlook.com', 'https://linkedin.com/in/jianli', '1988-07-09', NOW(), NOW()),
        ('Anya', 'Mikhailovna', 'Ivanova', 'anyak', '+7-925-987-65-43', NULL, 'anya.ivanova@protonmail.com', 'https://linkedin.com/in/anyaivanova', '1995-07-24', NOW(), NOW()),
        ('Ethan', 'Michael', 'Taylor', 'eitan', '+1-555-345-6789', NULL, 'ethan.taylor@yahoo.com', NULL, '1988-04-12', NOW(), NOW()),
        ('Carlos', 'Eduardo', 'Ferreira', 'carlinhos', '+55-21-99876-5432', NULL, 'carlos.ferreira@gmail.com', 'https://linkedin.com/in/carlosferreira', '1992-09-03', NOW(), NOW()),
        ('Yusuf', 'Emir', 'Arslan', 'yusufemir', '+90-532-123-78-90', NULL, 'yusuf.arslan@gmail.com', NULL, '1994-02-18', NOW(), NOW()),
        ('William', NULL, 'Thompson', 'will', '+44-7831-234567', NULL, 'will.thompson@yahoo.com', 'https://linkedin.com/in/willthompson', '1987-03-14', NOW(), NOW()),
        ('Mei', 'Ling', 'Chen', 'meimei', '+86-138-1234-5678', NULL, 'mei.chen@gmail.com', NULL, '1991-08-26', NOW(), NOW()),
        
        ('James', 'Robert', 'Wilson', 'jimmy', '+1-555-678-9012', '+1-555-123-4567', 'james.wilson@hotmail.com', NULL, '1989-11-07', NOW(), NOW()),
        ('Nadia', 'Alexandrovna', 'Sokolova', 'nadya', '+7-903-111-22-33', NULL, 'nadia.sokolova@gmail.com', 'https://linkedin.com/in/nadiasokolova', '1993-05-31', NOW(), NOW()),
        ('Benjamin', 'Thomas', 'Lee', 'ben', '+1-555-234-5678', NULL, 'ben.lee@outlook.com', NULL, '1990-02-22', NOW(), NOW()),
        ('Aaliyah', 'Maya', 'Jackson', 'ali', '+1-555-987-6543', NULL, 'aaliyah.jackson@yahoo.com', 'https://linkedin.com/in/aaliyahjackson', '1996-06-15', NOW(), NOW()),
        ('Hao', NULL, 'Liu', 'haohao', '+86-136-9876-5432', '+86-188-3333-4444', 'hao.liu@outlook.com', 'https://linkedin.com/in/haoliu', '1991-09-03', NOW(), NOW()),
        ('Keiko', 'Haruko', 'Nakamura', 'kei', '+81-80-5555-6666', NULL, 'keiko.nakamura@gmail.com', NULL, '1992-01-11', NOW(), NOW()),
        ('İrem', NULL, 'Bulut', 'iremciğim', '+90-554-987-65-43', NULL, 'irem.bulut@hotmail.com', 'https://linkedin.com/in/irembulut', '1997-09-03', NOW(), NOW()),
        ('Gabriela', 'Isabel', 'Costa', 'gabi', '+55-11-99333-4444', NULL, 'gabriela.costa@outlook.com', NULL, '1994-09-18', NOW(), NOW()),
        ('Ravi', 'Shankar', 'Patel', 'ravip', '+91-98-3333-4444', '+91-98-5555-6666', 'ravi.patel@yahoo.com', 'https://linkedin.com/in/ravipatel', '1991-04-26', NOW(), NOW()),
        ('Charlotte', 'Anne', 'Miller', 'charlie', '+1-555-456-7890', NULL, 'charlotte.miller@gmail.com', NULL, '1993-12-05', NOW(), NOW()),
        
        ('Kerem', 'Arda', 'Sarı', 'keremarda', '+90-532-987-12-34', NULL, 'kerem.sari@gmail.com', NULL, '1993-03-28', NOW(), NOW()),
        ('Lily', 'Grace', 'White', 'lils', '+1-555-321-0987', NULL, 'lily.white@gmail.com', 'https://linkedin.com/in/lilywhite', '1995-03-19', NOW(), NOW()),
        ('Hüseyin', 'Burak', 'Kılıç', 'hüso', '+90-543-987-65-43', '+90-216-876-54-32', 'huseyin.kilic@yahoo.com', NULL, '1989-08-14', NOW(), NOW()),
        ('Ayşe', NULL, 'Kaya', 'ayşecik', '+90-555-123-98-76', NULL, 'ayse.kaya@yahoo.com', 'https://linkedin.com/in/aysekaya', '1998-11-30', NOW(), NOW()),
        ('Sienna', NULL, 'Roberts', 'sienna', '+44-7500-987654', NULL, 'sienna.roberts@yahoo.com', 'https://linkedin.com/in/siennaroberts', '1992-07-30', NOW(), NOW()),
        ('Hiroshi', 'Takeshi', 'Sato', 'hiro', '+81-90-1234-9876', NULL, 'hiroshi.sato@protonmail.com', NULL, '1986-05-14', NOW(), NOW()),
        ('Samuel', 'Joseph', 'Clark', 'sam', '+1-555-765-4321', '+1-555-234-5678', 'sam.clark@gmail.com', NULL, '1990-10-21', NOW(), NOW()),
        ('Nina', 'Petrovna', 'Voloshina', 'ninushka', '+7-916-777-88-99', NULL, 'nina.voloshina@gmail.com', 'https://linkedin.com/in/ninavoloshina', '1994-02-16', NOW(), NOW()),
        ('David', 'Alexander', 'Lewis', 'dave', '+1-555-890-1234', NULL, 'david.lewis@outlook.com', NULL, '1988-06-02', NOW(), NOW()),
        ('Ahmet', NULL, 'Çelik', 'ahmoc', '+90-555-123-45-01', NULL, 'ahmet.celik@yahoo.com', NULL, '1998-11-12', NOW(), NOW());
