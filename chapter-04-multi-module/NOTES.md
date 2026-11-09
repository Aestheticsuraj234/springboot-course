# Chapter 4 — Maven Multi-Module Projects

**Example:** `shopease-platform`, an order service split into **layers**, with one Maven module per layer. The compiler and Maven both enforce the architecture: a module can only use code from the modules it declares as dependencies.

```
mvn clean package                     # build all 4 modules (the "reactor")
java -jar shop-app/target/shop-app.jar
```
```
ORD-000001 | customer=CUST-ASHA | total=INR 949.00 | status=PAID | ref=PAY-…
ORD-000002 | customer=CUST-RAVI | total=INR 189990.00 | status=PAYMENT_FAILED
```

## 1. Layout

```
chapter-04-multi-module/
├── pom.xml                   ← parent + aggregator (packaging = pom)
├── shop-domain/              ← Order, OrderItem, ports (interfaces). ZERO dependencies.
├── shop-application/         ← PlaceOrderService use case.          depends on: domain
├── shop-infrastructure/      ← in-memory repo, payment gateway.     depends on: domain
└── shop-app/                 ← main() wiring + uber-jar.            depends on: application, infrastructure
```

```
            shop-app
           /        \
 shop-application  shop-infrastructure
           \        /
           shop-domain
```
- `shop-application` **cannot** import `InMemoryOrderRepository`, because it doesn't depend on infrastructure, and the build fails if you try. That is **Dependency Inversion enforced by the build**.
- `shop-domain` depends on nothing, so it stays free of frameworks and is easy to test and reuse.

## 2. Aggregation vs inheritance

These are two separate mechanisms. This parent POM happens to do both.

| | Aggregation | Inheritance |
|---|---|---|
| Declared in | the parent: `<modules>` | each child: `<parent>` |
| Gives you | "build these together with one command" | "share this configuration" |
| Example | the course's **root `pom.xml`** only aggregates the chapters | each `shop-*` inherits from `shopease-platform` |

**Inherited by children:** `groupId`, `version`, `properties`, `dependencies`, `dependencyManagement`, `build/pluginManagement`, `build/plugins`, `profiles`, `repositories`.
**Not inherited:** `artifactId`, `name`, `modules`.

## 3. Centralising versions

| Element in the parent | Effect on children |
|---|---|
| `<dependencies>` | **Added** to every child (here, JUnit for all modules) |
| `<dependencyManagement>` | Only **versions/scopes**. A child must still declare the dependency, but without a `<version>`. |
| `<pluginManagement>` | Version/config used *if* a child uses that plugin. Only `shop-app` uses shade. |
| `<build><plugins>` | Plugin runs in **every** child |

Sibling modules are managed with `${project.version}`, so the whole platform is released with a single version number.

## 4. The reactor

Maven reads every module, builds a dependency graph and **sorts** the modules. The order in `<modules>` doesn't matter. Sample output from `mvn package`:
```
Reactor Build Order:
ShopEase :: Platform (parent)   [pom]
ShopEase :: Domain              [jar]
ShopEase :: Application         [jar]
ShopEase :: Infrastructure      [jar]
ShopEase :: App                 [jar]
```
Inside a reactor build, modules resolve each other from `target/`. You don't need `mvn install` first. **Cycles are illegal** (A → B → A fails the build).

### Commands you'll use every day

```
mvn -pl shop-application test            # only that module (siblings must be in ~/.m2)
mvn -pl shop-app -am package             # module + everything it depends on   (--also-make)
mvn -pl shop-domain -amd test            # module + everything that depends on it (--also-make-dependents)
mvn -rf :shop-infrastructure install     # resume a failed build from this module
mvn -T 1C package                        # parallel build, 1 thread per CPU core
mvn install                              # publish all modules to ~/.m2 for other projects
mvn versions:set -DnewVersion=1.1.0      # bump the version in every POM at once
```

**Typical workflow:** after changing the domain, run `mvn -pl shop-domain -amd test`. That checks that nothing depending on the domain broke.

## 5. When to split into modules

✅ **Split** when you need to:
- enforce architecture boundaries (as here)
- share a library (`common-dto`, `api-client`) between several apps
- build several deployables (API, batch job, consumer) from one codebase
- produce a separate BOM for consumers

❌ **Don't split** just to organise packages. Every module adds a POM, build time and release overhead. Packages are usually enough.

Real-world shapes: Spring Boot projects often use `*-api`, `*-core`, `*-web` and `*-bom`, plus a parent that imports `spring-boot-dependencies`.

## 6. Design principles in this chapter's code

| Principle | Where |
|---|---|
| **S**RP | Each *module* has one reason to change: business rules (domain), workflow (application), technology (infrastructure), wiring (app). The same holds for each class. |
| **O**CP | To switch to a real database or the Razorpay API, add a new adapter in infrastructure that implements the existing port. The domain and application modules aren't touched. |
| **L**SP | `PlaceOrderServiceTest` swaps in lambda fakes for `PaymentGateway`/`OrderIdGenerator`, and the use case behaves identically. |
| **I**SP | `OrderRepository`, `PaymentGateway` and `OrderIdGenerator` are small, focused ports. `PlaceOrderUseCase` is the inbound port that callers depend on. |
| **D**IP | High-level modules (application) depend on abstractions owned by the domain (ports). Low-level modules (infrastructure) implement them. Only `shop-app` knows both. This is **Hexagonal / Ports & Adapters** architecture. |
| Immutability | `Order` transitions return new instances and guard illegal moves (e.g. paying twice). |

**Exercise:** add a `shop-notification` module that depends on the domain, defines an `OrderPaidListener` port and implements it as a console adapter. Wire it in `shop-app`. Then run `mvn -pl shop-app -am package` and check the reactor order.
