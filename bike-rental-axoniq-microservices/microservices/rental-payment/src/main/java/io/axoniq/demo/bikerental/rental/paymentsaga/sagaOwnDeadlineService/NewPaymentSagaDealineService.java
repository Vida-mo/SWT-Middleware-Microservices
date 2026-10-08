package io.axoniq.demo.bikerental.rental.paymentsaga.sagaOwnDeadlineService;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.axoniq.demo.bikerental.coreapi.payment.PaymentConfirmedEvent;
import io.axoniq.demo.bikerental.coreapi.payment.PaymentPreparedEvent;
import io.axoniq.demo.bikerental.coreapi.payment.PaymentRejectedEvent;
import io.axoniq.demo.bikerental.coreapi.payment.PreparePaymentCommand;
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

import java.time.Duration;

//@Saga
public class NewPaymentSagaDealineService {

//        @Autowired
        private transient CommandGateway commandGateway;

//        @Autowired
        private transient PaymentTimeoutService paymentTimeoutService; // Neuer Service für das Timeout

        private String bikeId;
        private String renter;
        private String paymentReference;

        @JsonCreator
        public NewPaymentSagaDealineService(@JsonProperty("bikeId") String bikeId,
                                            @JsonProperty("renter") String renter) {
            this.bikeId = bikeId;
            this.renter = renter;
        }

        public NewPaymentSagaDealineService() {
        }

        @StartSaga
        @SagaEventHandler(associationProperty = "bikeId")
        public void on(BikeRequestedEvent event) {
            this.bikeId = event.bikeId();
            this.renter = event.renter();
            this.paymentReference = event.rentalReference();
            SagaLifecycle.associateWith("paymentReference", paymentReference);
            preparePayment(paymentReference);
        }

        @EndSaga
        @SagaEventHandler(associationProperty = "paymentReference")
        public void on(PaymentConfirmedEvent event) {
            // Zahlung erfolgreich -> Stopp den Timeout
            paymentTimeoutService.cancelTimeout(paymentReference);
            commandGateway.send(new ApproveRequestCommand(bikeId, renter));
        }

        @SagaEventHandler(associationProperty = "paymentReference")
        public void on(PaymentRejectedEvent event) {
            paymentTimeoutService.cancelTimeout(paymentReference);
            commandGateway.send(new RejectRequestCommand(bikeId, renter));
        }

        @EndSaga
        @SagaEventHandler(associationProperty = "bikeId")
        public void on(RequestRejectedEvent event) {
            paymentTimeoutService.cancelTimeout(paymentReference);
        }

        @SagaEventHandler(associationProperty = "paymentReference")
        public void on(PaymentPreparedEvent event) {
            // Zahlung gestartet -> Timeout setzen (z.B. 30 Sekunden)
            paymentTimeoutService.startTimeout(paymentReference, Duration.ofSeconds(30));
        }

        public void preparePayment(String rentalReference) {
            commandGateway.send(new PreparePaymentCommand(10, rentalReference))
                    .whenComplete((r, e) -> {
                        if (e != null) {
                            // Falls die Zahlung fehlschlägt, nach 5 Sekunden erneut versuchen
                            paymentTimeoutService.startRetry(rentalReference, Duration.ofSeconds(5));
                        }
                    });
        }

        @SuppressWarnings("unused")
        public String getBikeId() {
            return bikeId;
        }

        @SuppressWarnings("unused")
        public String getRenter() {
            return renter;
        }
    }

