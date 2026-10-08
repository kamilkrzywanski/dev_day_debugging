@echo off
REM Buduje procesor, a potem uruchamia modul example z JVM zawieszona na debuggerze
REM (JDWP, port 8000) - odpowiednik mvnDebug, przez wrapper (bez instalacji Mavena).
REM Podlacz IntelliJ: Remote JVM Debug -> localhost:8000. Breakpoint w DescribeProcessor.mapType.
setlocal
cd /d "%~dp0.."
call mvnw.cmd -q -pl 05-mvn-annotation-processor/processor -am install || exit /b 1
set MAVEN_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8000
echo ^>^> Maven czeka na debugger na porcie 8000. Podlacz IDE i build ruszy dalej.
call mvnw.cmd -pl 05-mvn-annotation-processor/example clean test
endlocal
