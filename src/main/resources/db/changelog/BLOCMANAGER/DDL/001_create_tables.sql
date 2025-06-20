--liquibase formatted sql

--changeset dev:init_schema

-- Activează extensia pentru UUID în PostgreSQL
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =============================
-- TABLE: associations
-- Reprezintă o asociație de proprietari (poate avea unul sau mai multe blocuri)
-- =============================
CREATE TABLE associations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),         -- ID unic
    name VARCHAR(255) NOT NULL,                             -- Numele asociației
    cif VARCHAR(20),                                        -- Cod fiscal (opțional)
    address TEXT,                                           -- Adresa generală
    phone_number VARCHAR(20),
    email VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP          -- Data creării
);

-- =============================
-- TABLE: blocks
-- Reprezintă un bloc (sau scară) aparținând unei asociații
-- =============================
CREATE TABLE blocks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),         -- ID unic bloc
    association_id UUID NOT NULL REFERENCES associations(id) ON DELETE CASCADE, -- Legătură cu asociația
    name VARCHAR(100) NOT NULL,                             -- Numele blocului (ex: Bl. A)
    address TEXT,                                           -- Adresa exactă a blocului
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================
-- TABLE: apartments
-- Reprezintă un apartament dintr-un bloc
-- =============================
CREATE TABLE apartments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    block_id UUID NOT NULL REFERENCES blocks(id) ON DELETE CASCADE, -- Legătură cu blocul
    number VARCHAR(10) NOT NULL,                          -- Numărul apartamentului (ex: Ap. 7)
    floor INT,                                             -- Etajul
    surface NUMERIC(6,2),                                  -- Suprafața în metri pătrați
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- =============================
-- TABLE: apartment_users
-- Leagă utilizatorii suplimentari de un apartament (ex: chiriași, co-proprietari)
-- Nu înlocuiește proprietarul principal (care rămâne în apartments.owner_id)
-- =============================
CREATE TABLE apartment_users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),                -- ID unic pentru fiecare legătură apartament-utilizator
    apartment_id UUID NOT NULL REFERENCES apartments(id) ON DELETE CASCADE,  -- Referință la apartamentul în care utilizatorul locuiește
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,            -- Referință la utilizatorul asociat (chiriaș, co-proprietar etc.)
    role VARCHAR(50) DEFAULT 'TENANT' CHECK (role IN ('TENANT', 'CO_OWNER')), -- Rolul utilizatorului în apartament: chiriaș (TENANT) sau co-proprietar (CO_OWNER)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                -- Data când a fost creată asocierea
    UNIQUE (apartment_id, user_id)                                 -- Asigură că un utilizator nu este adăugat de două ori la același apartament
);

-- =============================
-- TABLE: users
-- Reprezintă utilizatorii platformei (admini, locatari etc.)
-- =============================
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,                    -- Emailul utilizatorului (unic)
    first_name VARCHAR(255) NOT NULL,                       -- Nume
    last_name VARCHAR(255) NOT NULL,                   -- Prenume
    phone VARCHAR(20),                                     -- Telefon (opțional)
    stripe_account_id VARCHAR(255),                     -- ID cont Stripe (opțional, pentru plăți)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================
-- TABLE: user_roles
-- Definește rolurile utilizatorilor într-o asociație sau bloc
-- =============================
CREATE TABLE user_roles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE, -- Utilizatorul
    association_id UUID REFERENCES associations(id) ON DELETE CASCADE, -- Asociația (opțional)
    block_id UUID REFERENCES blocks(id) ON DELETE CASCADE,             -- Blocul (opțional)
    role VARCHAR(50) NOT NULL CHECK (role IN ('ADMIN_ASSOCIATION', 'BLOCK_ADMIN', 'APARTMENT_OWNER', 'TENANT')), -- Rol: ADMIN_ASSOCIATION, BLOCK_ADMIN, LOCATAR etc.
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, role, association_id, block_id)
);

-- =============================
-- TABLE: meter_readings
-- Citiri lunare de contoare pentru apartamente (ex: apă, gaz)
-- =============================
CREATE TABLE meter_readings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    apartment_id UUID NOT NULL REFERENCES apartments(id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL,                               -- Tip: "water", "gas"
    value NUMERIC(10, 3) NOT NULL,                           -- Valoarea citită
    reading_date DATE NOT NULL,                              -- Data citirii
    photo_url TEXT,                                          -- Poză justificativă (opțional)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================
-- TABLE: expenses
-- Cheltuieli comune ale blocului (lunare, reparații etc.)
-- =============================
CREATE TABLE expenses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    block_id UUID NOT NULL REFERENCES blocks(id) ON DELETE CASCADE,
    description TEXT,                                        -- Descriere opțională
    amount NUMERIC(10,2) NOT NULL,                     -- Suma totală
    category VARCHAR(50) NOT NULL,
    due_date DATE NOT NULL,                                     -- Luna la care se referă
    status VARCHAR(50) NOT NULL, -- Status: "pending", "paid", "overdue"
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================
-- TABLE: apartment_expenses
-- Alocarea fiecărei cheltuieli la apartamente
-- =============================
CREATE TABLE apartment_expenses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    apartment_id UUID NOT NULL REFERENCES apartments(id) ON DELETE CASCADE,
    expense_id UUID NOT NULL REFERENCES expenses(id) ON DELETE CASCADE,
    allocated_amount NUMERIC(10,2) NOT NULL                  -- Suma repartizată apartamentului
);

-- =============================
-- TABLE: payments
-- Plățile făcute de locatari (cash, card etc.)
-- =============================
CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    apartment_id UUID NOT NULL REFERENCES apartments(id) ON DELETE CASCADE,
    amount NUMERIC(10,2) NOT NULL,                           -- Suma plătită
    paid_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,             -- Data plății
    method VARCHAR(50),                                      -- Metodă: "cash", "card", "stripe"
    note TEXT                                                -- Observații (ex: pentru lunile ian-feb)
);

-- =============================
-- TABLE: announcements
-- Anunțuri publice postate pentru locatari
-- =============================
CREATE TABLE announcements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    block_id UUID NOT NULL REFERENCES blocks(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,                             -- Titlul anunțului
    message TEXT NOT NULL,                                   -- Conținutul
    posted_by UUID REFERENCES users(id),                     -- Cine a postat (admin)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================
-- TABLE: repair_requests
-- Sesizări trimise de locatari (avarii, reparații etc.)
-- =============================
CREATE TABLE repair_requests (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    apartment_id UUID REFERENCES apartments(id) ON DELETE SET NULL, -- Cine a semnalat (opțional)
    block_id UUID NOT NULL REFERENCES blocks(id) ON DELETE CASCADE,
    submitted_by UUID REFERENCES users(id),                 -- Utilizatorul care a trimis
    description TEXT NOT NULL,                              -- Ce problemă există
    status VARCHAR(50) DEFAULT 'open' CHECK (status IN ('open', 'in_progress', 'closed')),  -- Status: open / in_progress / closed
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================
-- INDEXURI UTILE
-- =============================
CREATE INDEX idx_user_roles_user ON user_roles(user_id);
CREATE INDEX idx_apartment_block ON apartments(block_id);
CREATE INDEX idx_block_association ON blocks(association_id);
