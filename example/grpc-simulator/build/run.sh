#!/usr/bin/env bash
# Run grpc-simulator after build (thin jar + target/lib/).
# Usage: ./build/run.sh [port]
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
EXAMPLE_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
cd "${EXAMPLE_DIR}"

if [[ -z "${JAVA_HOME:-}" || ! -x "${JAVA_HOME}/bin/java" ]]; then
  for cand in \
    "${HOME}/.local/share/mise/installs/java/zulu-25.34.17.0" \
    "${HOME}/.local/share/mise/installs/java/zulu-25" \
    "${HOME}/.local/share/mise/installs/java/25"; do
    if [[ -x "${cand}/bin/java" ]]; then
      export JAVA_HOME="${cand}"
      break
    fi
  done
fi

if [[ -z "${JAVA_HOME:-}" || ! -x "${JAVA_HOME}/bin/java" ]]; then
  echo "error: JDK 25 required. Set JAVA_HOME to zulu-25 (mise)." >&2
  exit 1
fi

export PATH="${JAVA_HOME}/bin:${PATH}"

JAVA_VER="$("${JAVA_HOME}/bin/java" -version 2>&1 | head -1 || true)"
if ! echo "${JAVA_VER}" | grep -qE 'version "25'; then
  echo "error: JAVA_HOME must be JDK 25, got: ${JAVA_VER}" >&2
  exit 1
fi

JAR="${EXAMPLE_DIR}/target/grpc-simulator.jar"
LIB_DIR="${EXAMPLE_DIR}/target/lib"

if [[ ! -f "${JAR}" || ! -d "${LIB_DIR}" ]]; then
  echo "note: build artifacts missing — running build.sh first..."
  "${SCRIPT_DIR}/build.sh"
fi

if [[ ! -f "${JAR}" || ! -d "${LIB_DIR}" ]]; then
  echo "error: expected ${JAR} and ${LIB_DIR}" >&2
  exit 1
fi

PORT="${1:-9090}"

echo "JAVA_HOME=${JAVA_HOME}"
echo "Starting grpc-simulator on port ${PORT}"
echo "  gRPC h2c  localhost:${PORT}"
echo

exec "${JAVA_HOME}/bin/java" \
  -cp "${JAR}:${LIB_DIR}/*" \
  com.example.grpcsimulator.GrpcSimulatorMain \
  "${PORT}"
