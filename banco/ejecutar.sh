#!/bin/bash
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
echo "=== COMPILANDO SISTEMA BANCARIO ==="
javac -d "$SCRIPT_DIR" "$SCRIPT_DIR/src/"*.java
if [ $? -eq 0 ]; then
    echo "=== COMPILACION EXITOSA ==="
    echo ""
    echo "=== EJECUTANDO ==="
    echo ""
    java -cp "$SCRIPT_DIR" Main
else
    echo "ERROR: Fallo la compilacion."
    exit 1
fi
