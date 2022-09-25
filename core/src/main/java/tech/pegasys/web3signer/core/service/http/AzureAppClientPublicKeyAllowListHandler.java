package tech.pegasys.web3signer.core.service.http;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Streams.stream;

public class AzureAppClientPublicKeyAllowListHandler implements Handler<RoutingContext> {
    private static final Logger LOG = LogManager.getLogger();
    private static final String APP_SERVICE_CLIENT_CERT_HEADER = "X-ARR-ClientCert";
    private final List<String> azureAppClientPubKeyAllowList;

    public AzureAppClientPublicKeyAllowListHandler(final List<String> azureAppClientPubKeyAllowList) {
        this.azureAppClientPubKeyAllowList = azureAppClientPubKeyAllowList;
    }

    @Override
    public void handle(final RoutingContext event) {
        final Optional<String> clientCertHeader = getAndValidateClientCertHeader(event);
        if (event.request().path() == "/healthcheck" || azureAppClientPubKeyAllowList.contains("*")
                || (clientCertHeader.isPresent() && clientCertIsInAllowlist(clientCertHeader.get()))) {
            event.next();
        } else {
            final HttpServerResponse response = event.response();
            if (!response.closed()) {
                response
                        .setStatusCode(403)
                        .putHeader("Content-Type", "application/json; charset=utf-8")
                        .end("{\"message\":\"Client not authorized.\"}");
            }
        }
    }

    private Optional<String> getAndValidateClientCertHeader(final RoutingContext event) {
        //CertificateFactory.
        return Optional.ofNullable(event.request().getHeader(APP_SERVICE_CLIENT_CERT_HEADER));
    }

    private boolean clientCertIsInAllowlist(final String clientCertHeader) {
        if (azureAppClientPubKeyAllowList.stream()
                .anyMatch(allowlistEntry -> allowlistEntry.equalsIgnoreCase(clientCertHeader))) {
            return true;
        } else {
            LOG.info("Client cert public key not in allowlist: '{}'", clientCertHeader);
            return false;
        }
    }
}
