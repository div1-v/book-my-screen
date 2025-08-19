-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Genres table
CREATE TABLE genres (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Movies table
CREATE TABLE movies (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    poster VARCHAR(255),
    duration INTEGER NOT NULL CHECK (duration > 0), -- in seconds
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Movie_Genres junction table
CREATE TABLE movie_genres (
    movie_id INTEGER REFERENCES movies(id) ON DELETE CASCADE,
    genre_id INTEGER REFERENCES genres(id) ON DELETE CASCADE,
    PRIMARY KEY (movie_id, genre_id)
);

-- Auditoriums table
CREATE TABLE auditoriums (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    capacity INTEGER NOT NULL
);

-- Seats table
CREATE TABLE seats (
    id SERIAL PRIMARY KEY,
    seat_number INTEGER NOT NULL,
    auditorium_id INTEGER NOT NULL REFERENCES auditoriums(id) ON DELETE CASCADE,
    UNIQUE (auditorium_id, seat_number)
);

-- Showtimes table
CREATE TABLE showtimes (
    id SERIAL PRIMARY KEY,
    movie_id INTEGER NOT NULL REFERENCES movies(id) ON DELETE CASCADE,
    auditorium_id INTEGER NOT NULL REFERENCES auditoriums(id) ON DELETE CASCADE,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    price INTEGER NOT NULL CHECK (price >= 0),
    CONSTRAINT valid_times CHECK (end_time > start_time)
);

-- Reservations table
CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    showtime_id INTEGER NOT NULL REFERENCES showtimes(id) ON DELETE CASCADE,
    seat_id INTEGER NOT NULL REFERENCES seats(id),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (showtime_id, seat_id) -- Prevents double booking
);

-- Insert basic genres
INSERT INTO genres (name) VALUES 
    ('ACTION'),
    ('COMEDY'),
    ('DRAMA'),
    ('HORROR'),
    ('SCIENCE_FICTION'),
    ('THRILLER'),
    ('ROMANCE'),
    ('ADVENTURE'),
    ('FANTASY'),
    ('ANIMATION');

-- Create indexes for better query performance
CREATE INDEX idx_movies_name ON movies(name);
CREATE INDEX idx_showtimes_start_time ON showtimes(start_time);
CREATE INDEX idx_reservations_showtime ON reservations(showtime_id);
CREATE INDEX idx_reservations_user ON reservations(user_id);
CREATE INDEX idx_seats_auditorium ON seats(auditorium_id);
