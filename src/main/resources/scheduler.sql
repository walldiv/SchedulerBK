DROP TABLE IF EXISTS employees, clients, orgunits, appointments;

create table employees (
    empid SERIAL primary key,
    fname varchar(30) NOT NULL,
    lname varchar(30) NOT NULL,
    phone varchar(11) NOT NULL,
    address integer,
    email varchar(30),
    orgunit integer
);

create table address (
    addressid SERIAL primary key,
    street varchar(50) NOT NULL,
    street2 varchar(50) NOT NULL,
    city varchar(30) NOT NULL,
    STATE varchar(30) NOT NULL,
    country varchar (30) NOT NULL,
    zipcode varchar (20) NOT NULL
);

create table clients (
    clientid SERIAL PRIMARY KEY,
    fname varchar(30) NOT NULL,
    lname varchar(30) NOT NULL,
    phone varchar(11) NOT NULL,
    address integer,
    email varchar(30),
    dateofbirth timestamp NOT NULL,
    emergencycontact varchar(10) NOT NULL,
    allowsms boolean
);

create table orgunits (
    orgname varchar(30) PRIMARY KEY,
    description text,
    dephead integer NOT NULL
);

create table appointments (
    appointmentid SERIAL PRIMARY KEY,
    client integer NOT NULL,
    FOREIGN KEY (client) REFERENCES clients (clientid),
    creationdate timestamp NOT NULL,
    createdby integer NOT NULL,
    assignedto integer NOT NULL,
    scheduledate timestamp NOT NULL,
    patientnotes text,
    employeenotes text
);