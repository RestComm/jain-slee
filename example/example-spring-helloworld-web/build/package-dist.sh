#!/usr/bin/env bash
# Build a directory distribution for Spring Boot:
#
#   dist/<app>-jainslee/
#     run.sh
#     <app>-jainslee.jar          # Spring Boot executable (fat) jar
#     lib/                        # runtime deps for ops visibility (jainslee-*, ra-*, …)
#
# The fat jar is self-contained; lib/ mirrors Maven runtime deps for inspection/patching.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
EXAMPLE_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
cd "${EXAMPLE_DIR}"

ARTIFACT_ID="example-spring-helloworld-web"
VERSION="1.0.0-SNAPSHOT"
APP_NAME="${APP_NAME:-helloworld-web-jainslee}"
DIST_ROOT="${DIST_ROOT:-${EXAMPLE_DIR}/dist}"
DIST_DIR="${DIST_ROOT}/${APP_NAME}"
PACKAGED_JAR="target/${ARTIFACT_ID}-${VERSION}.jar"

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

echo "Packaging Spring Boot executable jar …"
mvn -B -ntp package -DskipTests

if [[ ! -f "${PACKAGED_JAR}" ]]; then
  echo "error: missing ${PACKAGED_JAR} after mvn package" >&2
  exit 1
fi

echo "Copying runtime dependencies to target/lib/ …"
mvn -B -ntp dependency:copy-dependencies \
  -DincludeScope=runtime \
  -DoutputDirectory=target/lib \
  -DoverWriteReleases=false \
  -DoverWriteSnapshots=true

if [[ ! -d target/lib ]]; then
  echo "error: missing target/lib after dependency:copy-dependencies" >&2
  exit 1
fi

rm -rf "${DIST_DIR}"
mkdir -p "${DIST_DIR}/lib"
cp -a target/lib/. "${DIST_DIR}/lib/"
cp -a "${PACKAGED_JAR}" "${DIST_DIR}/${APP_NAME}.jar"

cat > "${DIST_DIR}/run.sh" <<EOF
#!/usr/bin/env bash
# Launch ${APP_NAME} (Spring Boot executable jar; lib/ for ops visibility).
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
echo "  UI     http://localhost:8080/"
echo "  health http://localhost:8080/health"
echo "  RA     http://localhost:8081/"
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
echo "  └── lib/   (${LIB_COUNT} jars — jainslee-*, ra-*, spring-*, …)"
echo
echo "Run:  ${DIST_DIR}/run.sh"
