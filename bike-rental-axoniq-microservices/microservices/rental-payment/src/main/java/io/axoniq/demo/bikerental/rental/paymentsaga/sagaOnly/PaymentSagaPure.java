package io.axoniq.demo.bikerental.rental.paymentsaga.sagaOnly;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.axoniq.demo.bikerental.coreapi.payment.*;
import io.axoniq.demo.bikerental.coreapi.rental.ApproveRequestCommand;
import io.axoniq.demo.bikerental.coreapi.rental.BikeRequestedEvent;
import io.axoniq.demo.bikerental.coreapi.rental.RejectRequestCommand;
import io.axoniq.demo.bikerental.coreapi.rental.RequestRejectedEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
public class PaymentSagaPure {

    @Autowired
    private transient CommandGateway commandGateway;

    private String bikeId;
    private String renter;
    private String paymentReference;

    @JsonCreator
    public PaymentSagaPure(@JsonProperty("bikeId") String bikeId,
                           @JsonProperty("renter") String renter) {
        this.bikeId = bikeId;
        this.renter = renter;
    }

    public PaymentSagaPure() {
    }

    /**
     * Startet die Saga, wenn eine Mietanfrage gestellt wurde.
     */
    @StartSaga
    @SagaEventHandler(associationProperty = "bikeId")
    public void on(BikeRequestedEvent event) {
        this.bikeId = event.bikeId();
        this.renter = event.renter();
        this.paymentReference = event.rentalReference();
        // Saga mit Payment-Referenz verknüpfen
        SagaLifecycle.associateWith("paymentReference", paymentReference);

        // Zahlung vorbereiten
        preparePayment(paymentReference);
    }

    /**
     * Erfolgreiche Zahlung -> Genehmige die Mietanfrage und beende die Saga.
     */
    @EndSaga
    @SagaEventHandler(associationProperty = "paymentReference")
    public void on(PaymentConfirmedEvent event) {
        commandGateway.send(new ApproveRequestCommand(bikeId, renter));
    }

    /**
     * Abgelehnte Zahlung -> Lehne die Mietanfrage ab.
     */
    @SagaEventHandler(associationProperty = "paymentReference")
    public void on(PaymentRejectedEvent event) {
        commandGateway.send(new RejectRequestCommand(bikeId, renter));
    }

    /**
     * Falls die Mietanfrage aus anderen Gründen abgelehnt wird, beende die Saga.
     */
    @EndSaga
    @SagaEventHandler(associationProperty = "bikeId")
    public void on(RequestRejectedEvent event) {
        // Die Zahlung sollte storniert werden
        commandGateway.send(new RejectPaymentCommand(paymentReference));
    }

    /**
     * Startet den Zahlungsprozess.
     */
    @SagaEventHandler(associationProperty = "paymentReference")
    public void on(PaymentPreparedEvent event) {
        // Hier könnte eine Benachrichtigung an den Nutzer oder ein anderer Prozess gestartet werden.
    }

    /**
     * Löst die Bezahlung aus.
     */
    public void preparePayment(String rentalReference) {
        commandGateway.send(new PreparePaymentCommand(10, rentalReference))
                .whenComplete((r, e) -> {
                    if (e != null) {
                        System.err.println("⚠ Zahlung fehlgeschlagen: " + e.getMessage());
                        // Falls ein Retry-Mechanismus benötigt wird, könnte er hier implementiert werden
                    }
                });
    }

    // Getter für JSON-Serialisierung (benötigt von Jackson)

    @SuppressWarnings("unused")
    public String getBikeId() {
        return bikeId;
    }

    @SuppressWarnings("unused")
    public String getRenter() {
        return renter;
    }
}


