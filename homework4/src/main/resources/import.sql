INSERT INTO Currceny (id, name) VALUES (1, 'Swedish crowns');
INSERT INTO Currceny (id, name) VALUES (2, 'Euro');
INSERT INTO Currceny (id, name) VALUES (3, 'United States dollar');
INSERT INTO Currceny (id, name) VALUES (4, 'British pounds');

--Swedish to Swedish
INSERT INTO Rates (from_id, to_id, rate) VALUES (1, 1, 1);
--Swedish to EUR
INSERT INTO Rates (from_id, to_id, rate) VALUES (1, 2, 0.1);
--Swedish to Dolla
INSERT INTO Rates (from_id, to_id, rate) VALUES (1, 3, 0.12);
--Swedish to Pounds
INSERT INTO Rates (from_id, to_id, rate) VALUES (1, 4, 0.9);

--EUR to Swedish
INSERT INTO Rates (from_id, to_id, rate) VALUES (2, 1, 9.93);
--EUR to EUR
INSERT INTO Rates (from_id, to_id, rate) VALUES (2, 2, 1);
--EUR to DoLLA
INSERT INTO Rates (from_id, to_id, rate) VALUES (2, 3, 1.18);
--EUR to Pounds
INSERT INTO Rates (from_id, to_id, rate) VALUES (2, 4, 0.88);

--Dolla to Swedish
INSERT INTO Rates (from_id, to_id, rate) VALUES (3, 1, 8.43);
--Dolla to EUR
INSERT INTO Rates (from_id, to_id, rate) VALUES (3, 2, 0.85);
--Dolla to Dolla
INSERT INTO Rates (from_id, to_id, rate) VALUES (3, 3, 1);
--Dolla to Pounds
INSERT INTO Rates (from_id, to_id, rate) VALUES (3, 4, 0.744);

--Pounds to Swedish
INSERT INTO Rates (from_id, to_id, rate) VALUES (4, 1, 11.34);
--Pounds to EUR
INSERT INTO Rates (from_id, to_id, rate) VALUES (4, 2, 1.14);
--Pounds to Dollar
INSERT INTO Rates (from_id, to_id, rate) VALUES (4, 3, 1.34);
--Pounds to Pounds
INSERT INTO Rates (from_id, to_id, rate) VALUES (4, 4, 1);