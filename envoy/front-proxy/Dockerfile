FROM envoyproxy/envoy-dev:latest

RUN apt-get update && apt-get -q install -y \
    curl
COPY ./envoy-config.yaml /etc/envoy-config.yaml
RUN chmod go+r /etc/envoy-config.yaml
CMD ["/usr/local/bin/envoy", "-c", "/etc/envoy-config.yaml", "--service-cluster", "front-proxy"]
