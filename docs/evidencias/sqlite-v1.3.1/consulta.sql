SELECT _id, name, website, phone, email, services, categories FROM companies ORDER BY name COLLATE NOCASE, _id;

CREATE TABLE companies (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, website TEXT NOT NULL, phone TEXT NOT NULL, email TEXT NOT NULL, services TEXT NOT NULL, categories INTEGER NOT NULL CHECK(categories BETWEEN 1 AND 7));

-- Conteos por pertenencia (una empresa puede pertenecer a varias clasificaciones).
SELECT SUM((categories & 1) != 0) AS consultoria, SUM((categories & 2) != 0) AS desarrollo, SUM((categories & 4) != 0) AS fabrica FROM companies;
