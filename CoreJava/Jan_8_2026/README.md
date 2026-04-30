IS-A and HAS-A Relationship Description

This project is designed using core Object-Oriented Programming concepts.
An IS-A relationship is implemented using inheritance.
RegularOrder, PremiumOrder, and CorporateOrder IS-A type of Order.
All customer-specific order classes extend the abstract Order class.
This allows each order type to provide its own billing logic.
Method overriding is used to achieve runtime polymorphism.
The design supports flexibility and scalability.
A HAS-A relationship is also used in the project.
The Order class HAS-A base order amount.
The base amount is encapsulated as a private variable.
It is accessed using a public getter method.
HAS-A relationship improves modularity.
It supports better data management.
Both relationships follow OOP best practices.
This design ensures clean, maintainable, and reusable code.
