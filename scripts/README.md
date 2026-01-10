solve_4x4.bat - wrapper to call the threephase solver in batch mode

Usage (Windows):
  scripts\solve_4x4.bat "R U R' U'"
  echo "R U R' U'" | scripts\solve_4x4.bat

Build a distributable fat/executable JAR (preferred for calling from other scripts):
  ./gradlew :threephase:build

This will produce `threephase/build/libs/threephase-solver-<version>.jar` (a "fat" JAR containing all dependencies). There is a thin wrapper `scripts/solve_4x4_run.bat` that prefers the built JAR (and falls back to Gradle if no JAR is present). The older `scripts/solve_4x4.bat` remains for backward compatibility but may be less reliable.

The wrapper sets an environment variable and calls Gradle (or the JAR) to run the threephase App in `--batch` mode. The `--batch` Java mode prints only the solver's move sequence to stdout (the last non-empty stdout line).

A Python example is provided at `scripts/solve_4x4.py` showing how to call the batch file or the fat JAR and extract the final solver line.
