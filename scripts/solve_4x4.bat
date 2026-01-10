@echo off
REM Wrapper to call threephase solver in batch mode
REM Usage:
REM   scripts\solve_4x4.bat "R U R' U'"
REM   echo "R U R' U'" | scripts\solve_4x4.bat

pushd "%~dp0\.."

REM If no args provided, call gradle with only --batch (Java will read stdin)
if "%~1"=="" (
    "%~dp0\..\gradlew.bat" :threephase:run -q --console=plain --no-daemon --args="--batch"
    set "rc=%ERRORLEVEL%"
) else (
    REM Set TNOODLE_INPUT so Java can pick it up from environment variables (more robust than trying to pipe through Gradle on Windows)
    set "TNOODLE_INPUT=%*"
    "%~dp0\..\gradlew.bat" :threephase:run -q --console=plain --no-daemon --args="--batch"
    set "rc=%ERRORLEVEL%"
    set "TNOODLE_INPUT="
)
popd
exit /b %rc%










exit /b %rc%popd)    set "rc=%ERRORLEVEL%"    "%~dp0\..\gradlew.bat" :threephase:run -q --console=plain --no-daemon --args="--batch %*") else (    set "rc=%ERRORLEVEL%"    "%~dp0\..\gradlew.bat" :threephase:run -q --console=plain --no-daemon --args="--batch"if "%~1"=="" (n:: If no args provided, call gradle with only --batch (Java will read stdin)