# run-gui.ps1 - Arranca la GUI empaquetada en target\servidor-gui.jar
# Ejecutar desde PowerShell; ubicar este script en el directorio del módulo ServidorCentral.
$here = Split-Path -Parent $PSCommandPath
if ([string]::IsNullOrEmpty($here)) { $here = Get-Location }
$jar = Join-Path $here "target\servidor-gui.jar"
if (-not (Test-Path $jar)) {
    Write-Host "ERROR: No se encontró el JAR: $jar" -ForegroundColor Red
    Write-Host "Compilar con Maven: mvn -f `"$here\pom.xml`" -DskipTests package"
    exit 1
}
Write-Host "Iniciando GUI desde: $jar"
& java -jar $jar
exit $LASTEXITCODE
