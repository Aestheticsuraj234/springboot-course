# Chapter 2 — Build Lifecycle, Plugins & Packaging

**Example:** `invoice-generator`, a ShopEase CLI that renders an invoice as text or CSV. It is shipped as an **executable jar**.

```
mvn verify                                   # full build incl. integration test on the jar
java -jar target/invoice-generator.jar       # text invoice
java -jar target/invoice-generator.jar csv > invoice.csv
mvn clean                                    # delete target/
```

## 1. Lifecycles, phases and goals

The three terms nest inside each other:

- A **lifecycle** is an ordered list of **phases**. Maven has three lifecycles: `clean`, `default` (build) and `site`.
- A **phase** is a step such as `compile` or `package`. A phase does nothing by itself.
- A **goal** is a task provided by a **plugin**, written `plugin:goal`, for example `compiler:compile`. Goals are **bound** to phases.

**Running a phase runs every earlier phase in the same lifecycle first.** So `mvn package` runs validate → compile → test → package.

### The `default` lifecycle (main phases) and what this project binds to each

| Phase | Bound goal in this project | What happens |
|---|---|---|
| `validate` | `enforcer:enforce` | Fails if Maven < 3.9 or Java < 21 |
| `process-resources` | `resources:resources` | Copies `src/main/resources`, **replacing `${...}`** (filtering) |
| `compile` | `compiler:compile` | `src/main/java` → `target/classes` |
| `process-test-resources` | `resources:testResources` | |
| `test-compile` | `compiler:testCompile` | |
| `test` | `surefire:test` | Runs `*Test.java`. A failure stops the build. |
| `package` | `jar:jar` | `target/invoice-generator.jar` with `Main-Class` in its manifest |
| `integration-test` | `failsafe:integration-test` | Runs `*IT.java` against the packaged jar |
| `verify` | `failsafe:verify` | Fails the build if any IT failed |
| `install` | `install:install` | Copies the jar + pom to `~/.m2/repository` |
| `deploy` | `deploy:deploy` | Uploads to a remote repository (Nexus/Artifactory) |

Actual output of `mvn verify` for this chapter. Every line is a goal running in its phase:
```
--- enforcer:3.5.0:enforce (enforce-build-environment)
--- resources:3.3.1:resources (default-resources)
--- compiler:3.13.0:compile (default-compile)
--- resources:3.3.1:testResources (default-testResources)
--- compiler:3.13.0:testCompile (default-testCompile)
--- surefire:3.5.2:test (default-test)
--- jar:3.4.2:jar (default-jar)
--- failsafe:3.5.2:integration-test (default)
--- failsafe:3.5.2:verify (default)
```
`default-xxx` execution ids mean the binding came **for free from `packaging=jar`**. Named ids (`enforce-build-environment`) are bindings **we added** with `<executions>`.

### The other two lifecycles
- **`clean`**: `pre-clean` → `clean` → `post-clean`. Use `mvn clean verify` for a from-scratch build.
- **`site`**: `site` → `site-deploy`. Generates project documentation and reports.

### Ways to invoke Maven
```
mvn package                    # a phase: runs everything up to it
mvn dependency:tree            # a goal on its own, no lifecycle
mvn clean install              # several phases / lifecycles in order
mvn package -DskipTests        # compile tests but don't run them
mvn package -Dmaven.test.skip  # don't even compile tests (avoid)
mvn verify -Dtest=CsvInvoiceFormatterTest   # run a single unit test
```

## 2. Plugins

All real work is done by plugins. The `<plugin>` element supports three levels of configuration:

```xml
<plugin>
  <groupId>…</groupId><artifactId>maven-failsafe-plugin</artifactId>
  <version>3.5.2</version>            <!-- ALWAYS pin: reproducible builds -->
  <configuration>…</configuration>    <!-- applies to all goals of the plugin -->
  <executions>
    <execution>
      <id>…</id>
      <phase>…</phase>                <!-- optional: override the goal's default phase -->
      <goals><goal>integration-test</goal></goals>
      <configuration>…</configuration><!-- applies to this execution only -->
    </execution>
  </executions>
</plugin>
```

| Plugin | Why it's in this project |
|---|---|
| **enforcer** | Gives a clear error instead of a confusing compile failure on a wrong JDK/Maven |
| **resources** | **Filtering** writes `project.version` and the build time into `build-info.properties` (see `BuildInfo`) |
| **compiler** | `maven.compiler.release=21` produces bytecode that runs on Java 21+ |
| **surefire** vs **failsafe** | Unit tests run *before* packaging. Integration tests run *after* it, against the real jar (`InvoiceJarIT`). |
| **jar** | `<mainClass>` makes `java -jar` work. `finalName` gives the jar a fixed name. |

**Gotcha:** `${maven.build.timestamp}` can't be filtered directly into resources. Copy it into your own property first (`build.timestamp` in the pom).

## 3. Packaging types

| `packaging` | Produces | Used for |
|---|---|---|
| `jar` | `.jar` | Libraries, CLIs, Spring Boot apps |
| `war` | `.war` | Apps deployed to Tomcat/Jetty |
| `pom` | nothing but the POM | Parent POMs, aggregators, BOMs (chapter 4) |
| `ear` | `.ear` | Jakarta EE enterprise bundles |
| `maven-plugin` | plugin jar | Writing your own Maven plugin |

**Thin jar vs fat jar.** This chapter's jar has no runtime dependencies, so a plain jar is enough to run it. Once an app has dependencies, use one of these:
- **`maven-shade-plugin`** builds an uber-jar containing all dependencies. It's used in chapter 4's `shop-app`.
- **`maven-assembly-plugin`** builds a zip/tar distribution containing scripts, config and `lib/`.
- **`spring-boot-maven-plugin`** builds Spring Boot's nested executable jar.

## 4. Design principles in this chapter's code

| Principle | Where |
|---|---|
| **S**RP | `Invoice` holds data. Each `InvoiceFormatter` only renders. `BuildInfo` only reads build metadata. `InvoiceApp` only wires and runs. |
| **O**CP | To add a JSON or PDF format, write a new `InvoiceFormatter` and register it in `InvoiceApp`. Nothing else changes. |
| **L**SP | Every formatter is interchangeable behind `InvoiceFormatter`. `InvoiceFormatters` never checks concrete types. |
| **I**SP | `InvoiceSource` (loading) and `InvoiceFormatter` (rendering) are separate, small interfaces. |
| **D**IP | `InvoiceApp` depends on `InvoiceSource`, so swapping `SampleInvoiceSource` for a database-backed source is a one-line change. |
| Clean CLI | Banner → stderr, data → stdout, exit code 2 for bad input. That makes `… csv > file.csv` safe. |

**Exercise:** add a `JsonInvoiceFormatter` and a unit test, then run `mvn verify`.
