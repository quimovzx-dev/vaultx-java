# VAULTX ☕
Java CLI encrypted file vault using AES-GCM and PBKDF2.

## Build
```bash
javac -d out src/VaultX.java
java -cp out VaultX encrypt input.txt secret.vx "password"
java -cp out VaultX decrypt secret.vx restored.txt "password"
```
