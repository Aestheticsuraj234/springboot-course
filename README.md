# Maven Course — ShopEase

A chapter-by-chapter Maven course built around one fictional e-commerce company, **ShopEase**. Each chapter is a standalone, runnable Maven project with annotated `pom.xml` files and a `NOTES.md`. The code follows SOLID, and each chapter's notes map every principle to the class that shows it.

| # | Chapter | Real-world example | Run it |
|---|---|---|---|
| 1 | [Project structure & `pom.xml`](chapter-01-project-structure/NOTES.md) | Order pricing with discounts + GST | `mvn compile exec:java` |
| 2 | [Lifecycle, plugins & packaging](chapter-02-lifecycle-plugins-packaging/NOTES.md) | Invoice generator CLI (executable jar) | `mvn verify && java -jar target/invoice-generator.jar csv` |
| 3 | [Profiles](chapter-03-profiles/NOTES.md) | Order notifications per environment | `mvn compile exec:java -Pstaging` |
| 4 | [Multi-module](chapter-04-multi-module/NOTES.md) | Layered order platform (ports & adapters) | `mvn package && java -jar shop-app/target/shop-app.jar` |

Run the commands from inside each chapter folder. To build and test **everything** from this folder, run:

```
mvn verify
```

Requirements: JDK 21+ and Maven 3.9+.

**Suggested path:** for each chapter, read its `NOTES.md`, then its `pom.xml` (every section has a comment), then run it, then do the exercise at the end of the notes.
