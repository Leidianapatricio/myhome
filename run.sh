#!/bin/bash
# Roda o MyHome a partir da pasta raiz do projeto (qualquer Linux/Mac)
cd "$(dirname "$0")"
mvn -q compile exec:java
