@echo off
REM Kai - A Journey Through Grief Launcher
REM This script launches the application using Java with proper JavaFX module configuration

setlocal enabledelayedexpansion

REM Set Java paths
set JAVA_HOME=C:\Users\brizu\.jdk\jdk-21.0.8
set JAVA_EXE=!JAVA_HOME!\bin\java.exe

REM Navigate to project directory
cd /d "%~dp0"

REM Set up module path for JavaFX
set JAVAFX_HOME=C:\Users\brizu\.m2\repository\org

REM Build the classpath with application and dependencies
set CLASSPATH=target\Kai-A-Journey-Through-Grief-1.0-SNAPSHOT.jar
for /r "%USERPROFILE%\.m2\repository\com\fasterxml\jackson" %%f in (*.jar) do (
    set CLASSPATH=!CLASSPATH!;%%f
)

REM Build the module path with JavaFX
set MODULEPATH=!JAVAFX_HOME!\openjfx\javafx-base\21.0.6\javafx-base-21.0.6-win.jar
set MODULEPATH=!MODULEPATH!;!JAVAFX_HOME!\openjfx\javafx-graphics\21.0.6\javafx-graphics-21.0.6-win.jar
set MODULEPATH=!MODULEPATH!;!JAVAFX_HOME!\openjfx\javafx-controls\21.0.6\javafx-controls-21.0.6-win.jar
set MODULEPATH=!MODULEPATH!;!JAVAFX_HOME!\openjfx\javafx-fxml\21.0.6\javafx-fxml-21.0.6-win.jar
set MODULEPATH=!MODULEPATH!;!JAVAFX_HOME!\openjfx\javafx-media\17.0.6\javafx-media-17.0.6-win.jar

REM Launch the application
!JAVA_EXE! --module-path "!MODULEPATH!" --add-modules javafx.controls,javafx.fxml,javafx.media -cp "!CLASSPATH!" com.example.kaiajourneythroughgrief.Main

if errorlevel 1 (
    echo.
    echo Error running the application. Press any key to exit...
    pause
)
