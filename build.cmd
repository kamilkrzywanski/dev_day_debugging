@echo off
REM Setup/weryfikacja: buduje wszystkie stacje i rozgrzewa cache. Nie musisz miec
REM zainstalowanego Mavena - wrapper (mvnw.cmd) pobierze wlasciwa wersje przy pierwszym
REM uruchomieniu. Uruchom RAZ z internetem przed warsztatem; potem dziala offline.
setlocal
cd /d "%~dp0"

call mvnw.cmd clean install %*
if errorlevel 1 exit /b 1

set IMAGE=eclipse-temurin:21-jre
where docker >nul 2>&1
if errorlevel 1 (
  echo ^>^> Docker niedostepny - pomijam obraz ^(potrzebny tylko na stacji 03^).
  goto done
)
docker info >nul 2>&1
if errorlevel 1 (
  echo ^>^> Docker nie dziala - pomijam obraz ^(potrzebny tylko na stacji 03^).
  goto done
)
echo ^>^> Pobieram obraz Dockera dla stacji 03: %IMAGE%
docker pull %IMAGE%

:done
echo ^>^> Gotowe. Warsztat jest gotowy do pracy offline.
endlocal
