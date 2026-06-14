# Levanta QaliKay backend con JDK 21 (Temurin)
$jdk = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"

if (-not (Test-Path "$jdk\bin\javac.exe")) {
    Write-Host "ERROR: No se encontro JDK 21 en:" -ForegroundColor Red
    Write-Host $jdk
    Write-Host "Instala Temurin JDK 21 desde https://adoptium.net"
    exit 1
}

$env:JAVA_HOME = $jdk
$env:Path = "$jdk\bin;" + $env:Path

Write-Host "Usando Java:" -ForegroundColor Green
java -version
Write-Host ""

Set-Location $PSScriptRoot
.\mvnw.cmd spring-boot:run
