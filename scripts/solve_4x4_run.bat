@echo off
REM Prefer a built fat JAR if present, otherwise fall back to Gradle run
REM Usage:
REM   scripts\solve_4x4_run.bat "R U R' U'"
REM   echo "R U R' U'" | scripts\solve_4x4_run.bat

pushd "%~dp0\.."
setlocal






























exit /b %rc%popdendlocalset "TNOODLE_INPUT=":: cleanup and exit)    set "rc=%ERRORLEVEL%"    "%~dp0\..\gradlew.bat" :threephase:run -q --console=plain --no-daemon --args="--batch") else (    set "rc=%ERRORLEVEL%"    java -jar "%JAR%" --batchif defined JAR (:foundJar)    goto :foundJar    set "JAR=%%~ff"for %%f in (threephase\build\libs\threephase-solver-*.jar) do (set "JAR="
n:: Look for a built fat JAR (threephase-solver-<version>.jar)set "TNOODLE_INPUT=%*"
n:: There are args -> set TNOODLE_INPUT so Java can read it from env)    exit /b %rc%    endlocal & popd    set "rc=%ERRORLEVEL%"    "%~dp0\..\gradlew.bat" :threephase:run -q --console=plain --no-daemon --args="--batch"if "%~1"=="" (n:: If no args provided, call gradle with only --batch (Java will read stdin)