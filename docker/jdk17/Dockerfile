FROM eclipse-temurin:17 as jre-build

# Create a custom Java runtime
RUN JAVA_TOOL_OPTIONS="-Djdk.lang.Process.launchMechanism=vfork" $JAVA_HOME/bin/jlink \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

WORKDIR /flyway
RUN curl -L https://repo1.maven.org/maven2/org/flywaydb/flyway-commandline/9.3.1/flyway-commandline-9.3.1.tar.gz -o flyway-commandline-9.3.1.tar.gz \
      && gzip -d flyway-commandline-9.3.1.tar.gz \
      && tar -xf flyway-commandline-9.3.1.tar --strip-components=1 \
      && rm flyway-commandline-9.3.1.tar \
      && chmod -R a+r /flyway \
      && chmod a+x /flyway/flyway


FROM ubuntu:latest
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-build /javaruntime $JAVA_HOME

RUN apt-get -y update && apt-get -y install libssl3 curl iputils-ping net-tools && rm -rf /var/lib/api/lists/*
RUN adduser --disabled-password --gecos "" --home /opt/web3signer web3signer && \
    chown web3signer:web3signer /opt/web3signer && chmod 755 /opt/web3signer

USER web3signer
WORKDIR /opt/web3signer

# copy application (with libraries inside)
COPY --chown=web3signer:web3signer web3signer /opt/web3signer/
COPY --from=jre-build --chown=web3signer:web3signer /flyway /opt/flyway/

ENV WEB3SIGNER_HTTP_LISTEN_HOST="0.0.0.0"
ENV WEB3SIGNER_METRICS_HOST="0.0.0.0"

# List Exposed Ports
# Metrics, Rest API
EXPOSE 9001 9000 9000/udp

# specify default command
ENTRYPOINT ["/bin/sh", "-c" , "/opt/flyway/flyway migrate -locations=\"filesystem:/opt/web3signer/migrations/postgresql\" && /opt/web3signer/bin/web3signer eth2"]


# Build-time metadata as defined at http://label-schema.org
ARG BUILD_DATE
ARG VCS_REF
ARG VERSION
LABEL org.label-schema.build-date=$BUILD_DATE \
      org.label-schema.name="Web3Signer" \
      org.label-schema.description="Ethereum 2.0 Signing Service" \
      org.label-schema.url="https://docs.web3signer.consensys.net" \
      org.label-schema.vcs-ref=$VCS_REF \
      org.label-schema.vcs-url="https://github.com/ConsenSys/web3signer.git" \
      org.label-schema.vendor="ConsenSys" \
      org.label-schema.version=$VERSION \
      org.label-schema.schema-version="1.0"