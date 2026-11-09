# Chapter 3 — Maven Profiles

**Example:** `order-notifications`. ShopEase sends an order confirmation, and how it is sent depends on the environment:

| Environment | How to build | Channel | Payment gateway |
|---|---|---|---|
| local dev (defaults) | `mvn compile exec:java` | console (nobody gets spammed) | local mock |
| staging | `mvn compile exec:java -Pstaging` | email | payment provider's sandbox |
| production | `mvn compile exec:java -Denv=prod` | SMS | live API |
| CI server | any build with `CI=true` set | *(unchanged)* | *(unchanged)*, but compiler warnings fail the build |

## 1. What a profile is

A **profile** is a named block of POM configuration that is only applied when the profile is **active**. It can override:
`properties`, `dependencies`, `dependencyManagement`, `build/plugins`, `resources`, `modules`, `repositories`, `distributionManagement`…

```xml
<profiles>
  <profile>
    <id>staging</id>
    <activation>…</activation>   <!-- optional: when to switch on automatically -->
    <properties>…</properties>    <!-- or any other overridable section -->
  </profile>
</profiles>
```

## 2. How this chapter uses them

1. Default values (local dev) are in the top-level `<properties>`.
2. Each profile overrides only the values that differ.
3. `src/main/resources/application.properties` contains `${notification.channel}` etc. Because **resource filtering** is on, Maven writes the active profile's values into `target/classes/application.properties`.
4. `AppConfig` fails fast if a value is missing or still contains `${`. `PackagedConfigTest` runs that check on every build, whichever profile is active.

## 3. Ways to activate a profile

| Activation | Example | In this chapter |
|---|---|---|
| Command line | `mvn -Pstaging`, several: `-Pstaging,ci` | `staging` |
| Deactivate | `mvn -P '!ci'` | |
| System property present | `<property><name>skipDocker</name></property>` | |
| System property value | `<property><name>env</name><value>prod</value></property>` → `-Denv=prod` | `prod` |
| Environment variable | `<property><name>env.CI</name></property>` | `ci` |
| JDK version | `<jdk>[21,)</jdk>` | |
| OS | `<os><family>windows</family></os>` | |
| File exists / missing | `<file><missing>local.properties</missing></file>` | |
| `activeByDefault` | `<activeByDefault>true</activeByDefault>` | ⚠ see gotchas |
| `settings.xml` | `<activeProfiles>` in `~/.m2/settings.xml` | |

Inspect what's active:
```
mvn help:active-profiles -Pstaging
mvn help:effective-pom -Denv=prod      # see the merged result
```

## 4. Gotchas

1. **`activeByDefault` is switched off as soon as any other profile in the same POM becomes active, including one activated automatically.** If dev settings lived in an `activeByDefault` profile, setting `CI=true` would activate `ci`, deactivate dev, and leave `${notification.channel}` unresolved. That's why this chapter puts defaults in `<properties>`.
2. **When several profiles are active and set the same property, the one declared *later* in the POM wins.** `-Pstaging -Denv=prod` produces prod values.
3. **Build once, deploy everywhere.** Building a separate artifact per environment means the jar you tested in staging isn't the jar that runs in production. Real projects build *one* artifact and supply environment config at **runtime**: environment variables, Spring profiles (`spring.profiles.active`), Kubernetes ConfigMaps. Use Maven profiles for *build-time* differences: CI strictness, optional modules, native/Docker builds, release signing, developer conveniences. The env-specific example in this chapter shows the mechanics. In a Spring Boot app, move those values into `application-{profile}.yml` instead.
4. **Secrets never go in the POM.** Use `settings.xml` (`<servers>`) or environment variables.

## 5. Design principles in this chapter's code

| Principle | Where |
|---|---|
| **S**RP | `ClasspathPropertiesLoader` reads files, `AppConfig` validates and types settings, each sender delivers, and `OrderConfirmationService` holds the business message. |
| **O**CP | To add a WhatsApp channel, write `WhatsAppNotificationSender` and register it in `NotificationApp`. The registry and service stay as they are. |
| **L**SP | Any `NotificationSender` works in `OrderConfirmationService`. The test uses a `RecordingSender`. |
| **I**SP | `NotificationSender` has only what callers need: `channel()` and `send()`. |
| **D**IP | The service depends on `NotificationSender`. Senders receive their `PrintStream` "transport" through the constructor, so tests capture output without touching `System.out`. |
| Fail fast | A misconfigured build fails in `mvn test`, not at 2 a.m. in production. |

**Exercise:** add a `qa` profile that activates when the file `qa.marker` exists in the project root and uses the email channel.
