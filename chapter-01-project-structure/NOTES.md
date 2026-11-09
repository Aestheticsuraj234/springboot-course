# Chapter 1 — Maven Project Structure & `pom.xml`

**Example:** `order-pricing`, a small ShopEase library that prices a cart. It adds the subtotal, applies a discount and then adds GST.

```
mvn test                    # compile + run unit tests
mvn compile exec:java       # run PricingApp
```

## 1. Convention over configuration

Maven expects a fixed layout. If you follow it, the `pom.xml` doesn't need to say where anything is.

```
chapter-01-project-structure/
├── pom.xml                       ← the Project Object Model
├── src/
│   ├── main/
│   │   ├── java/                 ← production code      → target/classes
│   │   └── resources/            ← config, templates    → target/classes (copied as-is)
│   └── test/
│       ├── java/                 ← test code            → target/test-classes
│       └── resources/            ← test-only fixtures   → target/test-classes
└── target/                       ← ALL build output (never commit it; `mvn clean` deletes it)
```

| Path | In the final jar? | On the test classpath? |
|---|---|---|
| `src/main/java`, `src/main/resources` | ✅ | ✅ |
| `src/test/java`, `src/test/resources` | ❌ | ✅ |

In this chapter, `pricing.properties` ships inside the jar. `pricing-test.properties` exists only for tests (see `PricingConfigTest`).

**Packages:** organise them by responsibility, for example `domain`, `policy`, `service` and `config`. Each folder under `src/main/java` must match its `package` declaration.

## 2. Anatomy of `pom.xml`

| Section | Purpose |
|---|---|
| `modelVersion` | POM schema version. Always `4.0.0`. |
| `groupId` / `artifactId` / `version` | The **coordinates (GAV)**, which are the artifact's unique address in a repository. |
| `packaging` | `jar` (default), `war`, `pom`, … It decides which plugins the lifecycle runs (chapter 2). |
| `name`, `description` | Human-readable metadata. |
| `properties` | Reusable values (`${junit.version}`), plus well-known ones such as `maven.compiler.release`. |
| `dependencyManagement` | Pins **versions** only. It does *not* add dependencies. |
| `dependencies` | What your code actually uses. |
| `build/plugins` | Configures the tools that run during the build. |

### Coordinates

`com.example.shopease:order-pricing:1.0.0-SNAPSHOT` is stored in your local repository at
`~/.m2/repository/com/example/shopease/order-pricing/1.0.0-SNAPSHOT/order-pricing-1.0.0-SNAPSHOT.jar`

- **`-SNAPSHOT`** means the version is still in development. Maven may re-download a newer snapshot.
- **Release versions** (`1.0.0`) are immutable. Once published, they never change.

### Dependency scopes

| Scope | Compile | Test | Runtime / packaged | Typical use |
|---|---|---|---|---|
| `compile` (default) | ✅ | ✅ | ✅ | Libraries your code calls |
| `provided` | ✅ | ✅ | ❌ | Servlet API (the server supplies it) |
| `runtime` | ❌ | ✅ | ✅ | JDBC drivers |
| `test` | ❌ | ✅ | ❌ | JUnit, Mockito |
| `import` | Only inside `dependencyManagement` with `type=pom` | | | BOMs |

### BOMs and transitive dependencies
- When you depend on A and A depends on B, you get B automatically. This is a **transitive** dependency.
- When two versions of the same artifact appear, the **nearest one wins**. When they are equally near, the first one declared wins.
- A **BOM** (`junit-bom` here) is a POM that only contains `dependencyManagement`. Importing it keeps a family of artifacts on matching versions.

## 3. Useful commands

```
mvn help:effective-pom        # the full POM after inheritance and defaults: what Maven really uses
mvn dependency:tree           # who pulled in which transitive dependency
mvn dependency:analyze        # used-but-undeclared / declared-but-unused dependencies
mvn -q test                   # quiet output
mvn -o test                   # offline (local repo only)
```

## 4. Design principles in this chapter's code

| Principle | Where |
|---|---|
| **S**ingle Responsibility | `Order` only knows its subtotal. `GstTaxPolicy` only knows tax. `PricingConfig` only reads settings. `OrderPricingService` only orchestrates. |
| **O**pen/Closed | To add a festive sale, write a new `DiscountPolicy` class. `OrderPricingService` doesn't change. |
| **L**iskov Substitution | Every `DiscountPolicy` must honour the contract "discount ≤ subtotal". The service rejects one that doesn't. |
| **I**nterface Segregation | `DiscountPolicy` and `TaxPolicy` are separate, single-method interfaces instead of one large `PricingRules` interface. |
| **D**ependency Inversion | `OrderPricingService` depends on interfaces. `PricingApp` (the **composition root**) is the only class that uses `new` on concrete policies. |
| Value objects | `Money` is immutable, validated, has a fixed scale and never goes negative. |

**Exercise:** add a `ThresholdDiscount` ("₹200 off on orders above ₹2,000") with its own test. You shouldn't need to change any existing class.
