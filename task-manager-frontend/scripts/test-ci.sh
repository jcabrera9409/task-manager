#!/bin/bash

# Script helper para ejecutar pruebas con Puppeteer Chrome
# Uso: ./scripts/test-ci.sh

echo "🧪 Iniciando pruebas unitarias con coverage..."

# Set Chrome binary path for Puppeteer
export CHROME_BIN=$(node -e "console.log(require('puppeteer').executablePath())")

# Print the path for debugging
echo "📍 Usando Chrome en: $CHROME_BIN"

# Verificar que el archivo existe
if [ ! -f "$CHROME_BIN" ]; then
    echo "❌ Error: Chrome binary no encontrado en $CHROME_BIN"
    echo "💡 Ejecuta: npm install puppeteer"
    exit 1
fi

# Run the tests
echo "🚀 Ejecutando pruebas..."
npm run test:ci

echo "✅ Pruebas completadas. Revisa los resultados arriba."