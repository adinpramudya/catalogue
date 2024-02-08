-- Delete ms_product table if exist
DROP TABLE EXISTS ms_product

-- Create ms_product table

CREATE TABLE ms_product (
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(50) NOT NULL,
    quantity INT4 NULL,
    category_id VARCHAR(50) NULL,
    created_date TIMESTAMP NOT NULL,
    creator_id INT4 NOT NULL,
    updated_date TIMESTAMP NOT NULL,
    updater_id INT4 NULL,
    deleted_date TIMESTAMP NOT NULL,
    deleter_id INT4 NULL,
    rec_status VARCHAR(1) NULL DEFAULT 'N'::VARCHAR
)
WITH (
    OIDS=false
);

SELECT * FROM ms_product