#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=../bash_log/log.sh
source "${SCRIPT_DIR}/../bash_log/log.sh"

IMAGE="ghcr.io/jurrien-de-klerk/commit-hook-tool"

# --- Validate argument ---
if [[ $# -ne 1 ]]; then
	log::error "Usage: $(basename "$0") <major>.<minor>.<patch>"
	exit 1
fi

VERSION="$1"
if [[ ! "$VERSION" =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)$ ]]; then
	log::error "version must be in the form [major].[minor].[patch], e.g. 1.2.3"
	exit 1
fi

MAJOR="${BASH_REMATCH[1]}"
MINOR="${BASH_REMATCH[2]}"
PATCH="${BASH_REMATCH[3]}"

TAG_PATCH="${MAJOR}.${MINOR}.${PATCH}"
TAG_MINOR="${MAJOR}.${MINOR}.x"
TAG_MAJOR="${MAJOR}.x"
TAG_LATEST="latest"

log::info "Building ${IMAGE}:${TAG_PATCH}"
docker build \
	--tag "${IMAGE}:${TAG_PATCH}" \
	--tag "${IMAGE}:${TAG_MINOR}" \
	--tag "${IMAGE}:${TAG_MAJOR}" \
	--tag "${IMAGE}:${TAG_LATEST}" \
	"$SCRIPT_DIR"

log::info "Pushing tags: ${TAG_PATCH}, ${TAG_MINOR}, ${TAG_MAJOR}, ${TAG_LATEST}"
docker push "${IMAGE}:${TAG_PATCH}"
docker push "${IMAGE}:${TAG_MINOR}"
docker push "${IMAGE}:${TAG_MAJOR}"
docker push "${IMAGE}:${TAG_LATEST}"

log::info "Done"
