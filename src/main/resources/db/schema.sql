DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS customer_order;
DROP TABLE IF EXISTS cart_product;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS volunteer_event_organizer;
DROP TABLE IF EXISTS tour_cleaning_volunteer_availability;
DROP TABLE IF EXISTS availability;
DROP TABLE IF EXISTS tour_cleaning_volunteer;
DROP TABLE IF EXISTS voluntary;
DROP TABLE IF EXISTS donation;
DROP TABLE IF EXISTS adoption;
DROP TABLE IF EXISTS animal;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS contact;
DROP TABLE IF EXISTS content_items;

CREATE TABLE account (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cpf VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    type VARCHAR(20) CHECK (type IN ('ADMMASTER', 'ADMCOMMON', 'USERCOMMON')) NOT NULL,
    gender VARCHAR(20) CHECK (gender IN ('MALE', 'FEMALE', 'OTHER', 'UNSPECIFIED')) NOT NULL
);

CREATE TABLE animal (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(20) CHECK (type IN ('DOG', 'CAT', 'OTHER')) NOT NULL,
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
    photo_url TEXT,
    adopted BOOLEAN DEFAULT FALSE
);

CREATE TABLE adoption (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT CHECK (age >= 0) NOT NULL,
    profession VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    phone_whatsapp VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    cep VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    house_number INT NOT NULL,
    neighborhood VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(20) NOT NULL,
    type_of_residence VARCHAR(20) CHECK (type_of_residence IN ('HOUSE', 'APARTMENT')),
    place_screened_street_access VARCHAR(255) NOT NULL,
    someone_lives_with_you VARCHAR(255) NOT NULL,
    someone_is_allergic VARCHAR(255) NOT NULL,
    adopter_has_animals VARCHAR(255) NOT NULL,
    adopter_animals_castrated BOOLEAN NOT NULL DEFAULT FALSE,
    adopter_animals_vaccinated BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE donation (
    id BIGSERIAL PRIMARY KEY,
    active_qr_code BOOLEAN DEFAULT FALSE,
    description TEXT,
    account VARCHAR(255),
    agency VARCHAR(255),
    bank_name VARCHAR(255),
    pix_key VARCHAR(255)
);

CREATE TABLE voluntary (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT CHECK (age >= 0) NOT NULL,
    phone_whatsapp VARCHAR(255) NOT NULL,
    voluntary_work TEXT NOT NULL,
    tell_us_about_yourself TEXT NOT NULL
);

CREATE TABLE tour_cleaning_volunteer (
    id BIGSERIAL PRIMARY KEY,
    availability_for_three_hours BOOLEAN DEFAULT FALSE,
    availability_during_week BOOLEAN NOT NULL DEFAULT FALSE,
    another_availability VARCHAR(255),
    FOREIGN KEY (id) REFERENCES voluntary(id) ON DELETE CASCADE
);

CREATE TABLE availability (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE tour_cleaning_volunteer_availability (
    id_voluntary BIGSERIAL REFERENCES tour_cleaning_volunteer(id) ON DELETE CASCADE,
    id_availability BIGSERIAL REFERENCES availability(id) ON DELETE CASCADE,
    PRIMARY KEY (id_voluntary, id_availability)
);

CREATE TABLE volunteer_event_organizer (
    id BIGSERIAL PRIMARY KEY,
    availability_to_attend_events BOOLEAN NOT NULL DEFAULT FALSE,
    experience_organing_events TEXT NOT NULL,
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
    account_id BIGSERIAL UNIQUE,
    FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE TABLE cart_product (
    cart_id BIGSERIAL REFERENCES cart(id) ON DELETE CASCADE,
    product_id BIGSERIAL REFERENCES product(id) ON DELETE CASCADE,
    quantity INT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (cart_id, product_id)
);

CREATE TABLE customer_order (
    id BIGSERIAL PRIMARY KEY,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount NUMERIC(10,2) NOT NULL,
    cart_id BIGSERIAL UNIQUE,
    FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE
);

CREATE TABLE payment (
    id BIGSERIAL PRIMARY KEY,
    status VARCHAR(20) CHECK (status IN ('PENDING', 'PAID', 'CANCELED')) NOT NULL,
    payment_method VARCHAR(20) CHECK (payment_method IN ('PIX', 'CARD')) NOT NULL,
    order_id INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES customer_order(id) ON DELETE CASCADE
);

CREATE TABLE contact (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    subject TEXT NOT NULL,
    message TEXT NOT NULL
);

CREATE TABLE content_items (
    id BIGSERIAL PRIMARY KEY,
    namespace VARCHAR(255) NOT NULL,
    key VARCHAR(255) NOT NULL,
    data TEXT NOT NULL,
    version INT DEFAULT 1,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(namespace, key)
);