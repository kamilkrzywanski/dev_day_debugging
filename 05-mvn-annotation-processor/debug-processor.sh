#!/usr/bin/env bash
# Buduje procesor, a potem uruchamia moduł example z JVM zawieszoną na debuggerze
# (JDWP, port 8000) — odpowiednik `mvnDebug`, ale przez wrapper (bez instalacji Mavena).
# Podłącz IntelliJ: Remote JVM Debug -> localhost:8000. Breakpoint w DescribeProcessor.mapType.
set -euo pipefail
cd "$(dirname "$0")/.."
./mvnw -q -pl 05-mvn-annotation-processor/processor -am install
export MAVEN_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8000"
echo ">> Maven czeka na debugger na porcie 8000. Podłącz IDE i build ruszy dalej."
./mvnw -pl 05-mvn-annotation-processor/example clean test
