@echo off

:: IT IS NOT TESTED!!!!!!!!!!
:: Path to the mvnw.cmd script
set MVNW_PATH=%CD%\.mvn\mvnw.cmd

:: List of projects in the order you want to build
set projects=parent utils-lib scheduler-lib database broker-engine pattern-detection-engine ui

:: Loop through each project and run mvnw clean install
for %%p in (%projects%) do (
    if exist "%%p" (
        echo Running mvnw clean in %%p
        cd %%p
        call %MVNW_PATH% clean
        cd ..
    ) else (
        echo Skipping %%p (directory not found)
    )
)

:: Loop through each project and run mvnw clean install
for %%p in (%projects%) do (
    if exist "%%p" (
        echo Running mvnw install in %%p
        cd %%p
        call %MVNW_PATH% install
        cd ..
    ) else (
        echo Skipping %%p (directory not found)
    )
)
