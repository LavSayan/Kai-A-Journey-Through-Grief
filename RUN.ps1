#!/usr/bin/env pwsh
# Start Kai - A Journey Through Grief

Write-Host "Starting Kai - A Journey Through Grief..." -ForegroundColor Green

# Get the directory where the script is located
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path

# Change to that directory
Set-Location $scriptDir

# Run the executable JAR with Java 21
& c:\Users\brizu\.jdk\jdk-21.0.8\bin\java.exe -jar target\Kai-A-Journey-Through-Grief-1.0-SNAPSHOT.jar
