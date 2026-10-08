package io.axoniq.demo.bikerental.rental.paymentsaga.sagaOwnDeadlineService;

import io.axoniq.demo.bikerental.coreapi.payment.PreparePaymentCommand;
import io.axoniq.demo.bikerental.coreapi.payment.RejectPaymentCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.*;

//@Service
public class PaymentTimeoutService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Map<String, ScheduledFuture<?>> timeoutTasks = new ConcurrentHashMap<>();
    private final CommandGateway commandGateway;

//    @Autowired
    public PaymentTimeoutService(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    public void startTimeout(String paymentReference, Duration duration) {
        cancelTimeout(paymentReference); // Falls bereits ein Timeout existiert, abbrechen
        ScheduledFuture<?> future = scheduler.schedule(
                () -> timeoutPayment(paymentReference),
                duration.toMillis(),
                TimeUnit.MILLISECONDS
        );
        timeoutTasks.put(paymentReference, future);
    }

    public void startRetry(String paymentReference, Duration duration) {
        ScheduledFuture<?> future = scheduler.schedule(
                () -> retryPayment(paymentReference),
                duration.toMillis(),
                TimeUnit.MILLISECONDS
        );
        timeoutTasks.put(paymentReference, future);
    }

    public void cancelTimeout(String paymentReference) {
        if (paymentReference == null) {
            System.err.println("❌ WARNUNG: paymentReference ist null, Timeout kann nicht abgebrochen werden!");
            return;
        }

        ScheduledFuture<?> future = timeoutTasks.remove(paymentReference);
        if (future != null) {
            future.cancel(false);
        }
    }


    private void timeoutPayment(String paymentReference) {
        commandGateway.send(new RejectPaymentCommand(paymentReference));
    }

    private void retryPayment(String paymentReference) {
        commandGateway.send(new PreparePaymentCommand(10, paymentReference));
    }
}

