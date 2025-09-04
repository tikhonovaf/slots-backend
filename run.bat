@echo off
chcp 65001

REM === Переменные окружения ===
set TIME_ZONE=Europe/Moscow

set DB_URL=jdbc:oracle:thin:@//localhost:1521/XEPDB1
set DB_USERNAME=SLOTUSER
set DB_PASSWORD=SLOTUSER
set DB_DRIVER=oracle.jdbc.OracleDriver

set MAIL_HOST=smtp.yandex.ru
set MAIL_PORT=465
set MAIL_USERNAME=tikhonovafZel@yandex.ru
set MAIL_PASSWORD=lrvbtcwgqqmfupgp

REM === Запуск приложения ===
java -Dfile.encoding=UTF-8 -jar target\slotsbe.jar

pause
