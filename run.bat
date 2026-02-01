@echo off
chcp 65001 >nul
REM MyHome - Roda na pasta raiz do projeto (Windows)
cd /d "%~dp0"

where mvn >nul 2>nul
if %errorlevel% equ 0 (
  echo Usando Maven...
  call mvn -q compile exec:java
  goto fim
)

echo Maven nao encontrado. Usando JDK (javac + java)...
if not exist bin mkdir bin
dir /s /b src\main\java\*.java > javelist.txt 2>nul
if not exist javelist.txt (
  echo Erro: nenhum arquivo .java em src\main\java\
  goto fim
)
javac -d bin -encoding UTF-8 -sourcepath src\main\java @javelist.txt
del javelist.txt 2>nul
if %errorlevel% equ 0 (
  java -cp bin br.edu.ifpb.myhome.Main
) else (
  echo Erro na compilacao. Verifique se o JDK 11+ esta instalado: javac -version
)
:fim
echo.
pause
