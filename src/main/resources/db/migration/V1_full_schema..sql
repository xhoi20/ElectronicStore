-- Create main user table
CREATE TABLE [user] (
                        id BIGINT IDENTITY PRIMARY KEY,
                        emri VARCHAR(255),
                        mbiemri VARCHAR(255),
                        email VARCHAR(255),
                        password VARCHAR(255),
                        role VARCHAR(50)
);

-- Create user_permissions (ElementCollection)
CREATE TABLE user_permissions (
                                  user_id BIGINT NOT NULL,
                                  permission VARCHAR(255),
                                  CONSTRAINT FK_user_permissions_user FOREIGN KEY (user_id) REFERENCES [user](id) ON DELETE CASCADE
);

-- Create sectors table (if not already defined)
-- NOTE: Replace this with your actual Sector table definition if needed
CREATE TABLE sector (
                        id BIGINT IDENTITY PRIMARY KEY,
                        name VARCHAR(255) NOT NULL
);

-- Create user_sectors join table with role constraint
CREATE TABLE user_sectors (
                              user_id BIGINT NOT NULL,
                              sector_id BIGINT NOT NULL,
                              role VARCHAR(50) NOT NULL,
                              CONSTRAINT PK_user_sectors PRIMARY KEY (user_id, sector_id, role),
                              CONSTRAINT FK_user_sector_user FOREIGN KEY (user_id) REFERENCES [user](id) ON DELETE CASCADE,
                              CONSTRAINT FK_user_sector_sector FOREIGN KEY (sector_id) REFERENCES sector(id) ON DELETE CASCADE
);
-- Create supplier table
CREATE TABLE supplier (
                          id BIGINT IDENTITY PRIMARY KEY,
                          emri_furnitorit VARCHAR(255),
                          kontakti VARCHAR(255),
                          adresa VARCHAR(255)
);


-- Create purchase_items table
CREATE TABLE purchase_items (
                                id BIGINT IDENTITY PRIMARY KEY,
                                purchase_id BIGINT NOT NULL,
                                item_id BIGINT NOT NULL,
                                invoice_id BIGINT,
                                quantity INT,
                                CONSTRAINT FK_purchase_items_purchase FOREIGN KEY (purchase_id) REFERENCES purchase(id) ON DELETE CASCADE,
                                CONSTRAINT FK_purchase_items_item FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE,
                                CONSTRAINT FK_purchase_items_invoice FOREIGN KEY (invoice_id) REFERENCES invoice(id) ON DELETE SET NULL
);
-- Create Purchase table
CREATE TABLE purchase (
                          id BIGINT IDENTITY PRIMARY KEY,
                          data_blerjes DATETIME,
                          furnitor_id BIGINT,
                          menaxher_id BIGINT,
                          totali_kostos FLOAT,
                          sasia INT,
                          CONSTRAINT FK_purchase_supplier FOREIGN KEY (furnitor_id) REFERENCES supplier(id) ON DELETE SET NULL,
                          CONSTRAINT FK_purchase_user FOREIGN KEY (menaxher_id) REFERENCES [user](id) ON DELETE SET NULL
);
-- Create item table
CREATE TABLE item (
                      id BIGINT IDENTITY PRIMARY KEY,
                      emri VARCHAR(255),
                      category_id BIGINT,
                      cmimi DECIMAL(10, 2),
                      sasia INT,
                      sector_id BIGINT,
                      CONSTRAINT FK_item_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL,
                      CONSTRAINT FK_item_sector FOREIGN KEY (sector_id) REFERENCES sector(id) ON DELETE SET NULL
);
-- Create ItemFurnitori join table for item and supplier
CREATE TABLE ItemFurnitori (
                               item_id BIGINT NOT NULL,
                               supplier_id BIGINT NOT NULL,
                               CONSTRAINT PK_item_supplier PRIMARY KEY (item_id, supplier_id),
                               CONSTRAINT FK_itemfurnitori_item FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE,
                               CONSTRAINT FK_itemfurnitori_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(id) ON DELETE CASCADE
);
-- Create invoice table
CREATE TABLE invoice (
                         id BIGINT IDENTITY PRIMARY KEY,
                         arketar_id BIGINT,
                         totali DECIMAL(10, 2),
                         status VARCHAR(50) NOT NULL DEFAULT 'PAPAGUAR',
                         CONSTRAINT FK_invoice_user FOREIGN KEY (arketar_id) REFERENCES [user](id) ON DELETE SET NULL
);
-- Create category table
CREATE TABLE category (
                          id BIGINT IDENTITY PRIMARY KEY,
                          emmrikategorise VARCHAR(255),
                          pershkrimi TEXT
);
