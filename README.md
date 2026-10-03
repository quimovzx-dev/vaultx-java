# VAULTX ☕

[![CI](https://github.com/quimovzx-dev/vaultx-java/actions/workflows/ci.yml/badge.svg)](https://github.com/quimovzx-dev/vaultx-java/actions/workflows/ci.yml) [![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

A dependency-free Java CLI for authenticated file encryption using **AES-256-GCM** with keys derived through **PBKDF2-HMAC-SHA256**.

## Security model
- Random 128-bit salt per vault
- Random 96-bit GCM nonce per encryption
- 210,000 PBKDF2 iterations
- AES-GCM authentication detects tampering or wrong passwords
- Password char arrays are cleared after use

> This is an educational portfolio project, not a replacement for a professionally audited password manager.

## Build & run
```bash
javac -d out src/VaultX.java
java -cp out VaultX encrypt input.txt secret.vx "password"
java -cp out VaultX decrypt secret.vx restored.txt "password"
```

## CI
GitHub Actions compiles the project and runs an encryption/decryption round-trip test.

## License
MIT
