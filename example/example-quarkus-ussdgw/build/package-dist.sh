#!/usr/bin/env bash
# Build a classic directory distribution (NOT an uber-jar):
#
#   dist/<app>-jainslee/
#     run.sh
#     <app>-jainslee.jar          # thin Quarkus runner
#     lib/                        # jainslee-*, ra-*, netty-*, …
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
EXAMPLE_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
cd "${EXAMPLE_DIR}"

APP_NAME="${APP_NAME:-ussdgw-jainslee}"
DIST_ROOT="${DIST_ROOT:-${EXAMPLE_DIR}/dist}"
DIST_DIR="${DIST_ROOT}/${APP_NAME}"

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

echo "Packaging legacy-jar (runner + flat lib/) …"
mvn -B -ntp package \
  -Dquarkus.build.skip=false \
  -Dquarkus.package.jar.type=legacy-jar \
  -DskipTests

RUNNER="$(ls -1 target/*-runner.jar 2>/dev/null | head -1 || true)"
if [[ -z "${RUNNER}" || ! -f "${RUNNER}" ]]; then
  echo "error: no target/*-runner.jar after legacy-jar package" >&2
  exit 1
fi
if [[ ! -d target/lib ]]; then
  echo "error: missing target/lib after legacy-jar package" >&2
  exit 1
fi

rm -rf "${DIST_DIR}"
mkdir -p "${DIST_DIR}/lib"
cp -a target/lib/. "${DIST_DIR}/lib/"
cp -a "${RUNNER}" "${DIST_DIR}/${APP_NAME}.jar"

cat > "${DIST_DIR}/run.sh" <<EOF
#!/usr/bin/env bash
# Launch ${APP_NAME} (directory layout: runner jar + lib/).
set -euo pipefail
ROOT="\$(cd "\$(dirname "\${BASH_SOURCE[0]}")" && pwd)"
cd "\${ROOT}"

if [[ -z "\${JAVA_HOME:-}" || ! -x "\${JAVA_HOME}/bin/java" ]]; then
  for cand in \\
    "\${HOME}/.local/share/mise/installs/java/zulu-25.34.17.0" \\
    "\${HOME}/.local/share/mise/installs/java/zulu-25" \\
    "\${HOME}/.local/share/mise/installs/java/25"; do
    if [[ -x "\${cand}/bin/java" ]]; then
      export JAVA_HOME="\${cand}"
      break
    fi
  done
fi
if [[ -z "\${JAVA_HOME:-}" || ! -x "\${JAVA_HOME}/bin/java" ]]; then
  echo "error: JDK 25 required" >&2
  exit 1
fi

echo "JAVA_HOME=\${JAVA_HOME}"
echo "Starting \${ROOT}/${APP_NAME}.jar"
echo "  admin  http://localhost:18080/"
echo "  USSD   http://localhost:8080/api/ussd/"
echo
exec "\${JAVA_HOME}/bin/java" \${JAVA_OPTS:-} -jar "${APP_NAME}.jar" "\$@"
EOF
chmod +x "${DIST_DIR}/run.sh"

if [[ -f src/main/resources/application.properties ]]; then
  cp -a src/main/resources/application.properties "${DIST_DIR}/application.properties.sample"
fi

LIB_COUNT="$(find "${DIST_DIR}/lib" -name '*.jar' | wc -l | tr -d ' ')"
echo
echo "Distribution ready:"
echo "  ${DIST_DIR}/"
echo "  ├── run.sh"
echo "  ├── ${APP_NAME}.jar"
echo "  └── lib/   (${LIB_COUNT} jars — jainslee-*, ra-*, netty-, …)"
echo
echo "Run:  ${DIST_DIR}/run.sh"
