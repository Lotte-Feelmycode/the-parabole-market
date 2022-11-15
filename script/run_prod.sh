#!/bin/bash

PROJECT_NAME="The Parabole"

TODAY=$(date +"%Y%m%d")

DEPLOY_PATH=/home/ec2-user
DEPLOY_JAR=parabole_be-01-prod-0.0.1-SNAPSHOT.jar
DEPLOY_FILE=$DEPLOY_PATH/$DEPLOY_JAR

LOG_PATH=$DEPLOY_PATH/log
LINK_LOG_FILE=$LOG_PATH/parabole.log
LOG_FILE=$LOG_PATH/$PROJECT_NAME_$TODAY.log
ERR_LOG_FILE=$LOG_PATH/$PROJECT_NAME_ERROR_$TODAY.log

echo "> Start run.sh for $PROJECT_NAME : $TODAY" >> $LOG_FILE

echo "> Delete log link file" >> $LOG_FILE
rm $LINK_LOG_FILE
ln -s $LOG_FILE $LINK_LOG_FILE
echo "> Build log : $LOG_FILE" >> $LOG_FILE

echo "> Check $PROJECT_NAME running"
CURRENT_PID=$(ps -ef | grep java | grep parabole | awk '{print $2}')

if [ -z $CURRENT_PID ]
then
  echo "> No such current pid" >> $LOG_FILE
else
  echo "> kill -9 $CURRENT_PID" >> $LOG_FILE
  kill -9 $CURRENT_PID
  sleep 10
fi

echo "> Deploy start : $DEPLOY_JAR"
nohup java -jar $DEPLOY_FILE >> $LOG_FILE 2>$ERR_LOG_FILE &

echo "> End of run.sh" >> $LOG_FILE
