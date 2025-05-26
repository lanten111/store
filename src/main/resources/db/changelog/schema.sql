-- Create customer table
CREATE TABLE customer (
                          customer_id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);

-- Create order table
CREATE TABLE "order" (
                         order_id BIGSERIAL PRIMARY KEY,
                         customer_id BIGINT NOT NULL,
                         CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
);

-- Create product table
CREATE TABLE "product" (
                         product_id BIGSERIAL PRIMARY KEY,
                         description VARCHAR(255) NOT NULL,
                         name VARCHAR(255) NOT NULL
);

-- Create order product link table
CREATE TABLE "order_product" (
                          product_id BIGSERIAL NOT NULL,
                          order_id BIGSERIAL NOT NULL,
                          PRIMARY KEY (order_id, product_id),
                          FOREIGN KEY (order_id) REFERENCES "order"(order_id),
                          FOREIGN KEY (product_id) REFERENCES product(product_id)

);