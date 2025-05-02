
## Preventing Concurrent Execution of `@Scheduled` Methods in Spring

In multi-threaded or distributed environments, it's important to prevent concurrent execution of scheduled tasks to avoid problems such as race conditions, duplicate processing, or data corruption.

---

### What Problem Does This Solve?

* Scheduled tasks in Spring are stateless by default.
* If a task takes longer to execute than its configured interval, a new instance may begin before the previous one finishes.
* In a clustered deployment, multiple nodes may execute the same scheduled task at the same time.

These issues can lead to unintended behavior, especially when dealing with database operations, external API calls, or shared resources.

---

### Solutions

#### 1. Using ShedLock (Recommended)

ShedLock is a simple and effective library that helps ensure scheduled tasks do not run concurrently, even across multiple nodes.

**Add the following dependencies to your Gradle configuration:**

```groovy
implementation group: 'net.javacrumbs.shedlock', name: 'shedlock-spring', version: '6.5.0'
implementation group: 'net.javacrumbs.shedlock', name: 'shedlock-provider-jdbc-template', version: '6.5.0'
```

**Enable scheduling and lock configuration:**

```java
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
@Configuration
public class SchedulerConfig {
}
```

**Define the scheduled method with locking:**

```java
@Scheduled(cron = "0 */3 * * * *") // Runs every 3 minutes
@SchedulerLock(name = "processOrdersTask", lockAtMostFor = "PT5M")
public void processOrders() {
    // Task logic here
}
```

* The `lockAtMostFor` parameter ensures the lock is automatically released even if the task fails or the application crashes.
* Lock metadata is stored in a database table (e.g., `shedlock`).

---

#### 2. Database Row Locking (Manual Approach)

If you prefer custom control or don't want to use a third-party library, you can implement manual locking using your own database table.

**Acquire a lock before running the task:**

```
UPDATE scheduled_locks
SET locked = true, last_run = NOW()
WHERE task_name = 'myTask' AND locked = false;
```

**Create the lock table in your database:**

```sql
CREATE TABLE shedlock (
    name       VARCHAR(64)  NOT NULL PRIMARY KEY,
    lock_until TIMESTAMP    NOT NULL,
    locked_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    locked_by  VARCHAR(255) NOT NULL
);
```

