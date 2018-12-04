@echo off
xcopy L:\Doc\SVN\Work\AirHub\trunk\src\AirHubHd\app\src\main L:\Doc\Git\AppInvAirHub\app\src\main\ /S
xcopy L:\Doc\SVN\Work\AirHub\trunk\src\AirHubHd\app\libs L:\Doc\Git\AppInvAirHub\app\libs\ /S
copy L:\Doc\SVN\Work\AirHub\trunk\src\AirHubHd\app\build.gradle L:\Doc\Git\AppInvAirHub\app
pause
