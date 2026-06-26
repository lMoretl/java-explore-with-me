CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events (
    id BIGSERIAL PRIMARY KEY,

    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    title VARCHAR(120) NOT NULL,

    event_date TIMESTAMP NOT NULL,
    created_on TIMESTAMP NOT NULL,
    published_on TIMESTAMP,

    paid BOOLEAN NOT NULL DEFAULT FALSE,
    participant_limit INTEGER NOT NULL DEFAULT 0,
    request_moderation BOOLEAN NOT NULL DEFAULT TRUE,
    views BIGINT NOT NULL DEFAULT 0,

    state VARCHAR(20) NOT NULL,

    lat DOUBLE PRECISION,
    lon DOUBLE PRECISION,

    initiator_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,

    FOREIGN KEY (initiator_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS participation_requests (
    id BIGSERIAL PRIMARY KEY,
    created TIMESTAMP NOT NULL,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,

    CONSTRAINT fk_participation_requests_event
        FOREIGN KEY (event_id) REFERENCES events(id),

    CONSTRAINT fk_participation_requests_requester
        FOREIGN KEY (requester_id) REFERENCES users(id),

    CONSTRAINT uq_participation_requests_requester_event
        UNIQUE (requester_id, event_id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    pinned BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS compilation_events (
    compilation_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    PRIMARY KEY (compilation_id, event_id),
    FOREIGN KEY (compilation_id) REFERENCES compilations(id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL PRIMARY KEY,
    text VARCHAR(2000) NOT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP,
    event_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,

    CONSTRAINT fk_comments_event
        FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,

    CONSTRAINT fk_comments_author
        FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);