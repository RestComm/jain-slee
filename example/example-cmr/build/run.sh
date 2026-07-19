#!/usr/bin/env bash
# Run example-cmr after package.
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

find_app_jar() {
  if [[ -f target/quarkus-app/quarkus-run.jar ]]; then
    echo "target/quarkus-app/quarkus-run.jar"
    return 0
  fi
  if [[ -f target/example-cmr-1.0.0-SNAPSHOT-runner.jar ]]; then
    echo "target/example-cmr-1.0.0-SNAPSHOT-runner.jar"
    return 0
  fi
  local found
  found="$(find target -maxdepth 2 -type f \( -name 'quarkus-run.jar' -o -name '*-runner.jar' \) 2>/dev/null | head -1 || true)"
  if [[ -n "${found}" && -f "${found}" ]]; then
    echo "${found}"
    return 0
  fi
  return 1
}

JAR=""
JAR="$(find_app_jar)" || JAR=""

if [[ -z "${JAR}" ]]; then
  echo "Packaging now (mvn package -Dquarkus.build.skip=false -DskipTests)..."
  mvn -B -ntp package -Dquarkus.build.skip=false -DskipTests
  JAR="$(find_app_jar)" || JAR=""
fi

if [[ -z "${JAR}" || ! -f "${JAR}" ]]; then
  echo "error: Quarkus runnable jar still missing after package." >&2
  echo "  try: ant -f ${SCRIPT_DIR}/build.xml install-deps package" >&2
  exit 1
fi

echo "JAVA_HOME=${JAVA_HOME}"
echo "Starting ${JAR}"
echo "  site   http://localhost:8082/  (cmr.http.port)"
echo "  admin  http://localhost:8082/admin"
echo "  dash   http://localhost:8082/telemetry"
echo

exec "${JAVA_HOME}/bin/java" -jar "${JAR}" "$@"
