DROP TABLE IF EXISTS users, categories, locations, events, compilations, compilation_events, requests, comments;

CREATE TABLE IF NOT EXISTS users (
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        VARCHAR(250) NOT NULL,
    email       VARCHAR(254) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories (
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS locations (
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat REAL NOT NULL,
    lon REAL NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation         VARCHAR(2000)                              NOT NULL,
    category_id        BIGINT,
    created_on         TIMESTAMP WITHOUT TIME ZONE                NOT NULL,
    description        VARCHAR(7000),
    event_date         TIMESTAMP WITHOUT TIME ZONE                NOT NULL,
    initiator_id       BIGINT                                     NOT NULL,
    location_id        BIGINT,
    paid               BOOLEAN,
    participant_limit  INT         DEFAULT 0,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN     DEFAULT TRUE,
    event_state        VARCHAR(20),
    confirmed_requests BIGINT,
    title              VARCHAR(500)                               NOT NULL,
    views              BIGINT,
    CONSTRAINT fk_events_categories FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE,
    CONSTRAINT fk_events_users FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_events_location FOREIGN KEY (location_id) REFERENCES locations (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations (
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN                                    NOT NULL,
    title  VARCHAR(255)                               NOT NULL
);

CREATE TABLE IF NOT EXISTS compilation_events (
    compilation_id BIGINT NOT NULL,
    event_id       BIGINT NOT NULL,
    CONSTRAINT pk_comp_event PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT fk_event_compilation FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_compilation_event FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests (
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id     BIGINT                      NOT NULL,
    requester_id BIGINT                      NOT NULL,
    status       VARCHAR(20)                 NOT NULL,
    CONSTRAINT fk_user_req FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_event_req FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created_on   TIMESTAMP WITHOUT TIME ZONE,
    event_id     BIGINT,
    author_id    BIGINT,
    comment_text VARCHAR(2000) NOT NULL,
    CONSTRAINT fk_event_comment FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_comment FOREIGN KEY (author_id) REFERENCES users (user_id) ON DELETE CASCADE
);