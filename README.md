# Welcome to the Spring Shoe Store!

> A demonstration app built for Spring I/O 2023 to present Clean Architecture principles in Spring

## Overview 

As mentioned, this repository is meant to show off a key techniques to attempt to achieve Clean Architecture:

### Individual Components

The application is broken into three individually tested and built Gradle modules; something the [Book](https://www.oreilly.com/library/view/clean-architecture-a/9780134494272/) would call 'Components'.

* __store-core__: contains all the Entities, Use Cases, DTOs, Interfaces, and other classes necessary to fulfill the business logic. Has no direct dependencies on the other 
    components, nor any external systems (e.g. a database). It is ignorant and agnostic of anything that is not Core Business Rules.
* __store-details__: contains the implementation "Details"; the interface implementations responsible for actually communicating with external systems, performing I/O, etc... as well as supporting classes necessary to make that work.
* __store-app__: contains the Spring components, configuration, application lifecycle concerns, and is currently the location of HTTP (e.g. controllers.). 
    This component is the place of actual 'integration'; where the app comes together. It is also the location of Integration tests.

The three are meant to be illustrative or a starting point; in a real application you may have more than three. For example,
the current `details` component could be broken up into multiple ones, each for redis, postgres, etc. The http concerns could be extracted.


### Dependency directionality

All dependencies should point 'inward', that is, everything depends on `Core`, but `Core` has no knowledge of the other components. 
Practically, this is largely achieved by liberal use of Interfaces and small data classes contained within the Core package. Other components are thus
dependent on core by adhering to the interface.

The [Repository pattern](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/infrastructure-persistence-layer-design) is also heavily used.

> Be advised that this code is truly, truly horrendous. It should be used for architecturalreference - or entertainment - purposes only

### Encapsulation and Isolation

Concerns of the Component should be contained within the Component. 

* the details component marks any non-interface-implementation classes with the `internal` keyword. In Kotlin, this 
  ensures that the class can not be used by jars / artifacts outside the current module. This pairs _nicely_ with the notion of using multiple components like we do here. 
* The http layer in `store-app` contains its own API objects, and translates the internal DTOs to an API-consumer-dedicated object. This also
  reduces the cross-boundary dependencies; without these API objects the api consumers would be directly dependent on classes contained in core.

Yes this results in more mapping code, but this is a small price to pay for the loose coupling we achieve with this
architectural style.

### Ignorance of Environment

The system has no notion of traditional environments (e.g. DEV, TEST, PROD, etc). Instead, it utilizes environment variables
to adjust configuration. This means that the system can use Integration Tests with real external dependencies just as 
easily as if it were running locally, or in a CI pipeline, or in a k8s cluster.

Speaking of testing, this application makes heavy use of [TestContainers](https://www.testcontainers.org/), creating a real
Postgres, Redis, and simulates AWS using the wonderful [Localstack](https://localstack.cloud/).
 

To run the tests, try `./gradlew clean test`. To demonstrate the advantages of the repository pattern, this repo is set up to 
swap out the storage of Orders from using Postgres to using DynamoDb.
To do so:

1. Verify the current tests work (`./gradlew test`)
2. Find the `getOrderRepository()` Bean function located in `CoreConfig` within the `store-app` component.
3. Replace the `return PostgresOrderRepository(jdbcTemplate)` line with `return DynamoDbOrderRepository(dynamoDbClient)`.
4. Run the tests once again (`./gradlew clean test`)
5. The tests should still pass!