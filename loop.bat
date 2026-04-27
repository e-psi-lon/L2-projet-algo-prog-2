@echo off
setlocal

set iterations=2

echo Running java -cp ./build/classes/ dev.e_psi_lon.Main %iterations% times...
echo.

for /L %%i in (1,1,%iterations%) do (
    echo [%%i/%iterations%] Running java -cp ./build/classes/ dev.e_psi_lon.Main...
    call C:\Users\nelsd\.vscode\extensions\redhat.java-1.54.0-win32-x64\jre\21.0.10-win32-x86_64\bin\java.exe -cp ./build/classes/java/main dev.e_psi_lon.Main
    if errorlevel 1 (
        echo Error occurred on iteration %%i. Stopping.
        pause
        exit /b 1
    )
    echo.
)

echo All %iterations% iterations completed successfully!
pause