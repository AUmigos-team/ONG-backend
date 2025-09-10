DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS customerOrder;
DROP TABLE IF EXISTS cart_product;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS volunteerEventOrganizer;
DROP TABLE IF EXISTS tourCleaningVolunteer_availability;
DROP TABLE IF EXISTS availability;
DROP TABLE IF EXISTS tourCleaningVolunteer;
DROP TABLE IF EXISTS voluntary;
DROP TABLE IF EXISTS donation;
DROP TABLE IF EXISTS adoption;
DROP TABLE IF EXISTS animal;
DROP TABLE IF EXISTS userCommon;
DROP TABLE IF EXISTS admCommon;
DROP TABLE IF EXISTS admMaster;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS contact;

CREATE TABLE account (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	cpf VARCHAR(255) UNIQUE NOT NULL,
	email VARCHAR(255) UNIQUE NOT NULL,
	phone VARCHAR(255), 
	password VARCHAR(255) NOT NULL,
	typeUser VARCHAR(20) CHECK (typeUser IN ('ADMMASTER', 'ADMCOMMON', 'USERCOMMON')) NOT NULL,
	gender VARCHAR(20) CHECK (gender IN ('MALE', 'FEMALE', 'OTHER', 'UNSPECIFIED')) NOT NULL
);

CREATE TABLE admMaster (
	id BIGINT PRIMARY KEY,
	FOREIGN KEY (id) REFERENCES account ON DELETE CASCADE 
);

CREATE TABLE admCommon (
	id BIGINT PRIMARY KEY,
	FOREIGN KEY (id) REFERENCES account ON DELETE CASCADE 
);

CREATE TABLE userCommon (
	id BIGINT PRIMARY KEY,
	dateOfBirth DATE NOT NULL,
	FOREIGN KEY (id) REFERENCES account ON DELETE CASCADE
);

CREATE TABLE animal (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	typeAnimal VARCHAR(20) CHECK (typeAnimal IN ('DOG', 'CAT', 'OTHER')) NOT NULL,
	gender VARCHAR(20) CHECK (gender IN ('MALE', 'FEMALE')) NOT NULL,
	color VARCHAR(255) NOT NULL,
	age INT CHECK (age >= 0) NOT NULL,
	size VARCHAR(20) CHECK (size IN ('SMALL', 'MEDIUM', 'LARGE')) NOT NULL,
	story TEXT NOT NULL,
	castrated BOOLEAN DEFAULT FALSE,
	veterinary_care BOOLEAN DEFAULT FALSE,
    neutered_spayed BOOLEAN DEFAULT FALSE,
    dewormed BOOLEAN DEFAULT FALSE,
    microchipped BOOLEAN DEFAULT FALSE,
    special_needs BOOLEAN DEFAULT FALSE,
	docile BOOLEAN DEFAULT FALSE,
    aggressive BOOLEAN DEFAULT FALSE,
    playful BOOLEAN DEFAULT FALSE,
    calm BOOLEAN DEFAULT FALSE,
    sociable BOOLEAN DEFAULT FALSE,
    shy BOOLEAN DEFAULT FALSE,
    independent BOOLEAN DEFAULT FALSE,
    needy BOOLEAN DEFAULT FALSE,
	good_with_children BOOLEAN DEFAULT FALSE,
    good_with_cats BOOLEAN DEFAULT FALSE,
    good_with_dogs BOOLEAN DEFAULT FALSE,
    good_with_strangers BOOLEAN DEFAULT FALSE,
    photoUrl TEXT NOT NULL,
    adopted BOOLEAN DEFAULT FALSE
);

CREATE TABLE adoption (
    id BIGSERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	age INT CHECK (age >= 0) NOT NULL,
	profession VARCHAR(255) NOT NULL,
	phone VARCHAR(255),
	phoneWhatsapp VARCHAR(255),
	email VARCHAR(255) UNIQUE NOT NULL,
    cep VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
	houseNumber INT NOT NULL,
	neighborhood VARCHAR(255) NOT NULL,
	city VARCHAR(255) NOT NULL,
	state VARCHAR(20) NOT NULL,
	typeOfResidence VARCHAR(20) CHECK (typeOfResidence IN ('HOUSE', 'APARTMENT')),
	placeScreenedStreetAccess VARCHAR(255) NOT NULL,
	someoneLivesWithYou VARCHAR(255) NOT NULL,
	someoneIsAllergic VARCHAR(255) NOT NULL,
	adopterHasAnimals VARCHAR(255) NOT NULL,
	adopterAnimalsCastrated BOOLEAN NOT NULL DEFAULT FALSE,
	adopterAnimalsVaccinated BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE donation (
    id BIGSERIAL PRIMARY KEY,
    activeQrCode BOOLEAN DEFAULT FALSE,
    description TEXT,
    account VARCHAR(255),
    agency VARCHAR(255),
    bankName VARCHAR(255),
    pixKey VARCHAR(255)
);

CREATE TABLE voluntary (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT CHECK (age >= 0) NOT NULL,
    phoneWhatsapp VARCHAR(255) NOT NULL,
    voluntaryWork TEXT NOT NULL,
    tellUsAboutYourself TEXT NOT NULL
);

CREATE TABLE tourCleaningVolunteer (
    id BIGSERIAL PRIMARY KEY,
	availabilityForThreeHours BOOLEAN DEFAULT FALSE,
    availabilityDuringWeek BOOLEAN NOT NULL DEFAULT FALSE,
	anotherAvailability VARCHAR(255),
    FOREIGN KEY (id) REFERENCES voluntary(id) ON DELETE CASCADE
);

CREATE TABLE availability (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE tourCleaningVolunteer_availability (
    id_voluntary BIGSERIAL REFERENCES tourCleaningVolunteer(id) ON DELETE CASCADE,
    id_availability BIGSERIAL REFERENCES availability(id) ON DELETE CASCADE,
    PRIMARY KEY (id_voluntary, id_availability)
);

CREATE TABLE volunteerEventOrganizer (
    id BIGSERIAL PRIMARY KEY,
    availabilityToAttendEvents BOOLEAN NOT NULL DEFAULT FALSE,
	experienceOrganingEvents TEXT NOT NULL,
	FOREIGN KEY (id) REFERENCES voluntary(id) ON DELETE CASCADE
);

CREATE TABLE product(
	id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10,2) NOT NULL,
    quantity INT NOT NULL,
	stock INT NOT NULL
);

CREATE TABLE cart (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_value NUMERIC(10,2) DEFAULT 0,
	userCommon_id BIGSERIAL UNIQUE,
	FOREIGN KEY (userCommon_id) REFERENCES userCommon(id)
);

CREATE TABLE cart_product (
    cart_id BIGSERIAL REFERENCES cart(id) ON DELETE CASCADE,
    product_id BIGSERIAL REFERENCES product(id) ON DELETE CASCADE,
    quantity INT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (cart_id, product_id)
);

CREATE TABLE customerOrder (
    id BIGSERIAL PRIMARY KEY,
    orderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    totalAmount NUMERIC(10,2) NOT NULL,
    cart_id BIGSERIAL UNIQUE,
	FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE
);

CREATE TABLE payment (
    id BIGSERIAL PRIMARY KEY,
    status VARCHAR(20) CHECK (status IN ('PENDING', 'PAID', 'CANCELED')) NOT NULL,
    payment_method VARCHAR(20) CHECK (payment_method IN ('PIX', 'CARD')) NOT NULL,
    order_id INT NOT NULL,
	FOREIGN KEY (order_id) REFERENCES customerOrder(id) ON DELETE CASCADE
);

CREATE TABLE contact (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    subject TEXT NOT NULL,
    message TEXT NOT NULL
);