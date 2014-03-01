@echo off
setlocal

rem BomEraser実行コマンド
set COMMAND=java -jar lib/BomEraser.jar

if "%1" == "" ( goto :HELP )
if "%2" == "" ( goto :HELP )

:START
%COMMAND% "%~1" "%~2"
exit /b %ERRORLEVEL%

:HELP
%COMMAND%
exit /b %ERRORLEVEL%
