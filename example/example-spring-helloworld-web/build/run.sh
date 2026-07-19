#!/usr/bin/env bash
# Run example-spring-helloworld-web after package.
# Usage (from example root or from build/):
#   ./build/run.sh
#   ./run.sh
# If the Spring Boot jar is missing, packages it first (needs JDK 25 + mvn).
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
EXAMPLE_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
cd "${EXAMPLE_DIR}"

ARTIFACT_ID="example-spring-helloworld-web"
VERSION="1.0.0-SNAPSHOT"
JAR="target/${ARTIFACT_ID}-${VERSION}.jar"

# JDK 25 only
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

find_app_jar() {
  if [[ -f "${JAR}" ]]; then
    echo "${JAR}"
    return 0
  fi
  local found
  found="$(find target -maxdepth 1 -type f -name "${ARTIFACT_ID}-*.jar" ! -name "*.original" 2>/dev/null | head -1 || true)"
  if [[ -n "${found}" && -f "${found}" ]]; then
    echo "${found}"
    return 0
  fi
  return 1
}

JAR="$(find_app_jar)" || JAR=""

if [[ -z "${JAR}" ]]; then
  echo "note: no packaged Spring Boot jar under target/."
  echo "Packaging now (mvn package -DskipTests)..."
  if ! command -v mvn >/dev/null 2>&1; then
    echo "error: mvn not found. Install Maven 3, then:" >&2
    echo "  cd ${EXAMPLE_DIR}" >&2
    echo "  mvn -B -ntp package -DskipTests" >&2
    exit 1
  fi
  mvn -B -ntp package -DskipTests
  JAR="$(find_app_jar)" || JAR=""
fi

if [[ -z "${JAR}" || ! -f "${JAR}" ]]; then
  echo "error: Spring Boot executable jar still missing after package." >&2
  echo "  try: ant -f ${SCRIPT_DIR}/build.xml install-deps package" >&2
  exit 1
fi

echo "JAVA_HOME=${JAVA_HOME}"
echo "Starting ${JAR}"
echo "  UI     http://localhost:8080/"
echo "  health http://localhost:8080/health"
echo "  RA     http://localhost:8081/  (http.ra.port)"
echo

exec "${JAVA_HOME}/bin/java" -jar "${JAR}" "$@"
