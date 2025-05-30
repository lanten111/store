const { faker}  = require('@faker-js/faker');

const N = 100; // Number of customers
const M = 10_000; // Number of orders

// Generate customers
for (let i = 1; i <= N; i++) {
    console.log(`INSERT INTO customer (name) VALUES ('${faker.name.fullName()}');`);
}

// Generate orders
for (let i = 1; i <= M; i++) {
    const customerId = Math.ceil(Math.random() * N);
    console.log(`INSERT INTO "order" (description, customer_id) VALUES ('${faker.commerce.productName()}', ${customerId});`);
}
