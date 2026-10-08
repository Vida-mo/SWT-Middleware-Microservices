# Bike Rental Demo AxonIQ

## Building An Axon Framework Application From Scratch

Vorbereitung und (lokale) Installation des Axon-Servers
- https://docs.axoniq.io/axon-server-reference/development/

Da die Demo einige zusätzliche Features beinhaltet, finden Sie
im Swt-Repo eine bearbeitete (vereinfachte) Version der Microservices, die in der Vorlesung behandelt wird.
- https://docs.axoniq.io/bikerental-demo/main/

Weitere Quellen
- https://github.com/axonIQ/bike-rental-quick-start/
- https://docs.axoniq.io/axon-framework-reference/4.11/

---

## Infos zum Ausführen der Microservices

### docker

- starte zunächst Docker.app oder Docker Desktop
- danach
  ```shell
  mvn clean install
  sh ./restart-axon-server-data.sh
  ```

### requests.http
- mit environment 'dev' ausführen



---

## Änderungen und Vereinfachungen

Die Bezeichnung entfernt bedeutet, dass das Feature auskommentiert ist und damit bei Ausführung nicht eingesetzt wird.

- eventProcessingCustomizer in RentalPaymentSagaApplication und RentalQueryApplication, PaymentApplication entfernt 
- workerExecutorService() in RentalPaymentSagaApplication und RentalQueryApplication entfernt 
- DeadlineManager in RentalPaymentSagaApplication und PaymentSaga entfernt
- QueryUpdaterEmitter in BikeStatusProjection und PaymentStatusProjection entfernt

---

## H2-Datenbank nach Saga-Einträgen durchsuchen

**Manuelle Konvertierung von Hex nach Text:**

```bash
echo 7b2262696b654964223a2239626334636335652d363761392d343631312d393738392d366663653034353763346636222c2272656e746572223a2253617261227d | xxd -r -p
```

📌 **Ergebnis:**

```json
{"bikeId":"9bc4cc5e-67a9-4611-9789-6fce0457c4f6","renter":"Sara"}
```

---
