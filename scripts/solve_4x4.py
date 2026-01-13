"""Example Python wrapper to call `scripts/solve_4x4.bat` and get a 4x4 solution.

Usage:
  python scripts/solve_4x4.py "R U R' U'"
  python scripts/solve_4x4.py "<96-char-facelet-string>"
  echo "R U R' U'" | python scripts/solve_4x4.py
"""
import subprocess
import sys
from pathlib import Path
import cv2 as cv
import numpy as np
import math

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

# convert solution function

# get facelit from camera function
def getCube():
    # Open webcam
    cap = cv.VideoCapture(0)

    # Check if the video opened correctly
    if not cap.isOpened():
        print("Error: Could not open video capture.")
        exit()

    # Palette (BGR) for the six target colors: white, yellow, orange, red, green, blue
    palette_bgr = np.array([
        [255, 255, 255],  # white
        [0, 255, 255],    # yellow
        [0, 140, 255],    # orange
        [0, 0, 255],      # red
        [0, 255, 0],      # green
        [255, 0, 0],      # blue
    ], dtype=np.uint8)

    # Convert palette to LAB for perceptual distance
    palette_lab = cv.cvtColor(palette_bgr.reshape((-1, 1, 3)), cv.COLOR_BGR2LAB).reshape((-1, 3)).astype(int)

    # Read frames, quantize each pixel to the nearest palette color, and display
    while True:
        ret, frame = cap.read()
        if not ret:
            break   # No more frames -> exit loop

        # Convert frame to LAB (perceptual color space)
        lab = cv.cvtColor(frame, cv.COLOR_BGR2LAB).astype(int)
        h, w, _ = lab.shape
        lab_flat = lab.reshape((-1, 3))

        # Compute squared distances to each palette color and pick nearest
        dists = np.sum((lab_flat[:, None, :] - palette_lab[None, :, :]) ** 2, axis=2)
        idx = np.argmin(dists, axis=1)

        # Map each pixel to the nearest palette BGR color
        quant_flat = palette_bgr[idx]
        quant = quant_flat.reshape((h, w, 3))

        cv.imshow("Quantized", quant)

        # Press Q to quit
        if cv.waitKey(1) & 0xFF == ord('q'):
            break

    # Release resources
    cap.release()
    cv.destroyAllWindows()
    

if __name__ == "__main__":
    getCube()

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


# UUURUUUFUUUFUUUF RRRBRRRBRRRBRRRB RRRDFFFDFFFDFFFD DDDBDDDBDDDBDDDL FFFFLLLLLLLLLLLL ULLLUBBBUBBBUBBB
# U face           R face           F face           D face           L face           B face