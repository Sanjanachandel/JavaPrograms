

CREATE TABLE CUSTOMER1 (
  CUSTOMER_ID VARCHAR2(10) PRIMARY KEY,
  FIRST_NAME VARCHAR2(15),
  LAST_NAME VARCHAR2(15),
  MOBILE_NUMBER VARCHAR2(15),
  EMAIL VARCHAR2(20)
);

  
INSERT INTO CUSTOMER1(CUSTOMER_ID, FIRST_NAME, LAST_NAME, MOBILE_NUMBER, EMAIL)
VALUES ('CUST001', 'Ram', 'Kumar', '9876543210', 'ram.kumar@gmail.com');

INSERT INTO CUSTOMER1 (CUSTOMER_ID, FIRST_NAME, LAST_NAME, MOBILE_NUMBER, EMAIL)
VALUES ('CUST002', 'Sita', 'Devi', '9123456789', 'sita.devi@gmail.com');

INSERT INTO CUSTOMER1 (CUSTOMER_ID, FIRST_NAME, LAST_NAME, MOBILE_NUMBER, EMAIL)
VALUES ('CUST003', 'Arun', 'Sharma', '9988776655', 'arun.sharma@gmail.com');

INSERT INTO CUSTOMER1 (CUSTOMER_ID, FIRST_NAME, LAST_NAME, MOBILE_NUMBER, EMAIL)
VALUES ('CUST004', 'Priya', 'Singh', '9012345678', 'priya.singh@gmail.com');

INSERT INTO CUSTOMER1 (CUSTOMER_ID, FIRST_NAME, LAST_NAME, MOBILE_NUMBER, EMAIL)
VALUES ('CUST005', 'Vijay', 'Rao', '9090909090', 'vijay.rao@gmail.com');

commit;


>mvn clean package -DskipTests

docker build --no-cache -t custservice .

// D1-CustomerService1>docker run -p 9094:9090 custservice


docker run -p 9094:9090 custservice


 
 
 //docker run -d -p 9094:9090 --name customer-container custservice:latest
 
 
 http://localhost:9094/customers
 
 
 
 
 
 