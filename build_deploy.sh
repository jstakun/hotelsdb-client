VERSION=0.1
podman build -f ./Containerfile.distroless -t dev.local/jstakun/hotelsdb-client:$VERSION .
podman tag dev.local/jstakun/hotelsdb-client:$VERSION quay.io/jstakun/hotelsdb-client:$VERSION
podman push quay.io/jstakun/hotelsdb-client:$VERSION
podman tag dev.local/jstakun/hotelsdb-client:$VERSION quay.io/jstakun/hotelsdb-client:latest
podman push quay.io/jstakun/hotelsdb-client:latest
