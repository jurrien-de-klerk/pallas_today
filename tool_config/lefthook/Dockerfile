FROM python:3.11.15-trixie

SHELL ["/bin/bash", "-o", "pipefail", "-c"]

# Install apt packages and binary tools in a single layer
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        curl=8.14.1-2+deb13u2 \
        jq=1.7.1-6+deb13u1 \
        libxml2-utils=2.12.7+dfsg+really2.9.14-2.1+deb13u2 \
        nodejs=20.19.2+dfsg-1+deb13u2 \
        npm=9.2.0~ds1-3 \
    && rm -rf /var/lib/apt/lists/* \
    # Install Python tools
    && pip install --no-cache-dir \
        mdformat==1.0.0 \
        mdformat-gfm==1.0.0 \
        yamllint==1.38.0 \
    # Install markdownlint-cli2 and jsonlint via npm
    && npm install -g \
        markdownlint-cli2@0.22.1 \
        jsonlint@1.6.3 \
    # Install yamlfmt
    && curl -fsSL \
        https://github.com/google/yamlfmt/releases/download/v0.21.0/yamlfmt_0.21.0_Linux_x86_64.tar.gz \
    | tar -xz -C /usr/local/bin yamlfmt \
    # Install hadolint
    && curl -fsSL \
        https://github.com/hadolint/hadolint/releases/download/v2.14.0/hadolint-Linux-x86_64 \
        -o /usr/local/bin/hadolint \
    && chmod +x /usr/local/bin/hadolint \
    # Install dockerfmt
    && curl -fsSL \
        https://github.com/reteps/dockerfmt/releases/download/v0.5.2/dockerfmt-v0.5.2-linux-amd64.tar.gz \
    | tar -xz -C /usr/local/bin dockerfmt