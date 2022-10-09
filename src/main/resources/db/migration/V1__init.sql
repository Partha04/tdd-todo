CREATE TABLE todo (
  id UUID NOT NULL,
   task VARCHAR(255),
   completed BOOLEAN NOT NULL,
   CONSTRAINT pk_todo PRIMARY KEY (id)
);