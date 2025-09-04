#!/bin/bash

# === Переменные окружения ===
export TIME_ZONE="Europe/Moscow"

export DB_URL="jdbc:oracle:thin:@//localhost:1521/XEPDB1"
export DB_USERNAME="SLOTUSER"
export DB_PASSWORD="SLOTUSER"
export DB_DRIVER="oracle.jdbc.OracleDriver"

export MAIL_HOST="smtp.yandex.ru"
export MAIL_PORT="465"
export MAIL_USERNAME="tikhonovafZel@yandex.ru"
export MAIL_PASSWORD="lrvbtcwgqqmfupgp"

# === Запуск приложения ===
java -Dfile.encoding=UTF-8 -jar target/slotsbe.jar
