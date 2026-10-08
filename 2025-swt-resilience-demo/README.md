# Resilience Demo

In diesem Repo sind folgende Resilienz-Pattern als Demo enthalten:

* Circuit Breaker
* Time Limiter
* Rate Limiter
* Bulkhead
* Alles zusammen

Für das Experimentieren müssen die entsprechenden Zeilen in application.properties auskommentiert und die Zeilen
der anderen Pattern einkommentiert werden.

Dann können die Test-Skripte in run-skripts/ für das ausgewählte Pattern ausgeführt werden.

Ein mehrmaliges Ausführen der Skripts ist immer mit Kontext zu betrachten. Oft bleiben die Komponenten bestehen und
es sind keine frischen Durchläufe.
