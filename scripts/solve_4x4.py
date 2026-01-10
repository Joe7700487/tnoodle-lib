"""Example Python wrapper to call `scripts/solve_4x4.bat` and get a 4x4 solution.

Usage:
  python scripts/solve_4x4.py "R U R' U'"
  python scripts/solve_4x4.py "<96-char-facelet-string>"
  echo "R U R' U'" | python scripts/solve_4x4.py
"""
import subprocess
import sys
from pathlib import Path

BATCH = Path(__file__).resolve().parent / "solve_4x4.bat"


def solve(input_str: str) -> str:
    repo = Path(__file__).resolve().parent.parent
    jar_dir = repo / "threephase" / "build" / "libs"
    jar_matches = sorted(jar_dir.glob("threephase-solver-*.jar")) if jar_dir.exists() else []

    if jar_matches:
        # Prefer calling the built fat JAR directly
        jar = jar_matches[-1]
        cmd = ["java", "-jar", str(jar), "--batch", input_str]
    else:
        # Fall back to batch wrapper
        cmd = ["cmd", "/c", str(BATCH), input_str]

    proc = subprocess.run(cmd, capture_output=True, text=True)
    if proc.returncode != 0:
        raise RuntimeError(proc.stderr.strip() or f"Process failed with exit {proc.returncode}")
    out = proc.stdout.strip()
    if not out:
        return ""
    # The Gradle/SLF4J output can include warnings; take the last non-empty line as the solver output
    lines = [l.strip() for l in out.splitlines() if l.strip()]
    return lines[-1] if lines else ""


if __name__ == "__main__":
    if sys.stdin and not sys.stdin.isatty():
        user_input = sys.stdin.read().strip()
    elif len(sys.argv) > 1:
        user_input = " ".join(sys.argv[1:]).strip()
    else:
        user_input = input("Enter scramble or 96-char facelet: ").strip()

    try:
        sol = solve(user_input)
        print(sol)
    except Exception as e:
        print("Error:", e, file=sys.stderr)
        sys.exit(1)