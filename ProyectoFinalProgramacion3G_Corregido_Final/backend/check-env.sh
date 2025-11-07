#!/bin/bash
# Script para verificar variables de entorno antes del despliegue

echo "=== Verificación de Variables de Entorno ==="
echo ""

# Variables requeridas para PostgreSQL
REQUIRED_VARS=("JDBC_DATABASE_URL" "JDBC_DATABASE_USERNAME" "JDBC_DATABASE_PASSWORD" "JWT_SECRET")

# Variables alternativas que Render podría proporcionar
ALTERNATIVE_VARS=("DATABASE_URL" "DATABASE_USERNAME" "DATABASE_PASSWORD")

echo "Variables requeridas:"
for var in "${REQUIRED_VARS[@]}"; do
    if [ -z "${!var}" ]; then
        echo "❌ $var: NO CONFIGURADA"
    else
        # Ocultar valores sensibles
        if [[ "$var" == *"PASSWORD"* ]] || [[ "$var" == *"SECRET"* ]]; then
            echo "✅ $var: [CONFIGURADA - VALOR OCULTO]"
        else
            echo "✅ $var: ${!var}"
        fi
    fi
done

echo ""
echo "Variables alternativas (Render):"
for var in "${ALTERNATIVE_VARS[@]}"; do
    if [ -z "${!var}" ]; then
        echo "⚠️  $var: NO CONFIGURADA"
    else
        if [[ "$var" == *"PASSWORD"* ]]; then
            echo "✅ $var: [CONFIGURADA - VALOR OCULTO]"
        else
            echo "✅ $var: ${!var}"
        fi
    fi
done

echo ""
echo "Perfil activo: ${SPRING_PROFILES_ACTIVE:-dev}"
echo "Puerto: ${PORT:-8080}"
