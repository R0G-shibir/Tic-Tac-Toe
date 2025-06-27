#!/bin/zsh

# Colors
GREEN="\033[0;32m"
RED="\033[0;31m"
YELLOW="\033[1;33m"
CYAN="\033[0;36m"
NC="\033[0m" # No Color

# File and folder settings
SRC_DIR="src"
BIN_DIR="bin"

echo "${CYAN}🔄 Cleaning previous build...${NC}"
rm -rf "$BIN_DIR"
mkdir -p "$BIN_DIR"

echo "${YELLOW}📦 Compiling all Java files in ${SRC_DIR}...${NC}"
javac -d "$BIN_DIR" ${SRC_DIR}/*.java

if [[ $? -eq 0 ]]; then
    echo "${GREEN}✅ Compilation successful!${NC}"
else
    echo "${RED}❌ Compilation failed!${NC}"
    exit 1
fi

echo "${CYAN}🚀 Running the app...${NC}"
java -cp "$BIN_DIR" App