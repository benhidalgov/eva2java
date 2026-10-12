@echo off
setlocal
cd /d "%~dp0"

rem ------------------------------------------------------------
rem EventPass - compila con Maven y arranca la API en el puerto 8081
rem (el 8080 suele estar ocupado por Steam)
rem ------------------------------------------------------------
if not defined JAVA_HOME set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot"
set "PATH=%JAVA_HOME%\bin;%LOCALAPPDATA%\Programs\apache-maven-3.9.11\bin;%PATH%"
set "PORT=8081"

rem Si ya corre una instancia en este puerto, la detiene (permite reiniciar
rem con doble clic sin que falle el build por el .jar bloqueado)
for /f "tokens=5" %%a in ('netstat -ano ^| findstr /C:":%PORT% " ^| findstr LISTENING') do (
    echo Deteniendo la instancia anterior en el puerto %PORT% ^(PID %%a^)
    taskkill /f /pid %%a >nul 2>&1
)
timeout /t 2 /nobreak >nul

echo ============================================
echo  EventPass - mvn clean install
echo ============================================
call mvn -q clean install
if errorlevel 1 (
    echo.
    echo [ERROR] Falló la compilación.
    pause
    exit /b 1
)

echo.
echo ============================================
echo  API corriendo en  http://localhost:%PORT%
echo  Consola H2       http://localhost:%PORT%/h2-console
echo  Presiona Ctrl+C para detener
echo ============================================
java -jar target\eventpass-1.0.0.jar --server.port=%PORT%
pause
