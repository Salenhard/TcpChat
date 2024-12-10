CREATE SEQUENCE  IF NOT EXISTS chat_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE  IF NOT EXISTS message_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE chat (
   id BIGINT NOT NULL,
   name VARCHAR(255),
   is_private BOOLEAN DEFAULT TRUE,
   creator_username VARCHAR(255),
   CONSTRAINT pk_chat PRIMARY KEY (id)
);

CREATE TABLE chat_users (
  chats_id BIGINT NOT NULL,
   users_username VARCHAR(255) NOT NULL
);

CREATE TABLE message (
   id BIGINT NOT NULL,
   author_username VARCHAR(255),
   text VARCHAR(255),
   chat_id BIGINT,
   created_at TIMESTAMP WITHOUT TIME ZONE,
   edited_at TIMESTAMP WITHOUT TIME ZONE,
   is_edited BOOLEAN,
   is_read BOOLEAN,
   CONSTRAINT pk_message PRIMARY KEY (id)
);

CREATE TABLE "user" (
  username VARCHAR(255) NOT NULL,
   CONSTRAINT pk_user PRIMARY KEY (username)
);

ALTER TABLE chat ADD CONSTRAINT FK_CHAT_ON_CREATOR_USERNAME FOREIGN KEY (creator_username) REFERENCES "user" (username);

ALTER TABLE message ADD CONSTRAINT FK_MESSAGE_ON_AUTHOR_USERNAME FOREIGN KEY (author_username) REFERENCES "user" (username);

ALTER TABLE message ADD CONSTRAINT FK_MESSAGE_ON_CHAT FOREIGN KEY (chat_id) REFERENCES chat (id) ON DELETE CASCADE;

ALTER TABLE chat_users ADD CONSTRAINT fk_chause_on_chat FOREIGN KEY (chats_id) REFERENCES chat (id);

ALTER TABLE chat_users ADD CONSTRAINT fk_chause_on_user FOREIGN KEY (users_username) REFERENCES "user" (username);