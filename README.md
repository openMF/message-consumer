# message-consumer

## Event Consumption via Kafka or JMS

###  Spring Profiles

| Profile     | Behavior                                  |
|-------------|-------------------------------------------|
| `kafka`     | Enables Kafka-based event consumption     |
| `jms`       | Enables JMS/ActiveMQ-based consumption    |
| *(none)*    | Disables both consumers                   |

---

### Running with Kafka
```bash
./gradlew bootRun --args='--spring.profiles.active=kafka'
```

### Running with JMS
```bash
./gradlew bootRun --args='--spring.profiles.active=jms'
```
