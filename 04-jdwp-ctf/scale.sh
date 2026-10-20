#!/usr/bin/env bash
# Generuje compose na N instancji CTF. Każda dostaje unikalną flagę i własne porty.
# Użycie:  ./scale.sh 30 [bind]
#   bind (tylko JDWP): 127.0.0.1 (domyślnie, dostęp przez tunel SSH) lub 0.0.0.0 (zaufany LAN).
#   HTTP (/verify) zawsze na 0.0.0.0 — uczestnicy sprawdzają flagę wprost.
set -euo pipefail
cd "$(dirname "$0")"
N=${1:-30}
BIND=${2:-127.0.0.1}
{
  echo "services:"
  for i in $(seq 1 "$N"); do
    idx=$(printf '%02d' "$i")
    http=$((8080 + i))     # 8081..
    jdwp=$((5005 + i))     # 5006..
    rand=$(head -c6 /dev/urandom | od -An -tx1 | tr -d ' \n')
    printf '  ctf-%s:\n    build: .\n    environment:\n      WORKSHOP_FLAG: "FLAG-%s-%s"\n    ports:\n      - "0.0.0.0:%d:8080"\n      - "%s:%d:5005"\n' \
      "$idx" "$rand" "$idx" "$http" "$BIND" "$jdwp"
  done
} > compose.scale.yaml
echo "compose.scale.yaml: $N instancji"
echo "  HTTP hosta:  0.0.0.0:8081..$((8080+N))"
echo "  JDWP hosta:  $BIND:5006..$((5005+N))"
echo "Start:  docker compose -f compose.scale.yaml up --build -d"
echo "Flagi:  docker compose -f compose.scale.yaml exec ctf-01 printenv WORKSHOP_FLAG  # itd."
