#!/bin/bash

err=1
until [ $err == 0 ];
do
	[ -d log/ ] || mkdir log/
	[ -f log/console.log ] && mv log/console.log "log/backup/`date +%Y-%m-%d_%H-%M-%S`_console.log"
	java -Xms4096m -Xmx8192m -ea -javaagent:./libs/al-commons.jar -cp ./libs/*:AL-Game.jar com.aionemu.gameserver.GameServer > log/console.log 2>&1
	err=$?
	gspid=$!
	echo ${gspid} > gameserver.pid
	sleep 10
done