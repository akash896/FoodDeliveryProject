
# cd ../Tests
# python3 Private1-Project1Phase1.py
# python3 Private2-Project1Phase1.py
# python3 Private3-Project1Phase1.py
# python3 Private4-Project1Phase1.py
# python3 Private5-Project1Phase1.py

echo killing AKKA Server
PID=`lsof -i :8081 | awk '{print $2}'`
if [[ "" !=  "$PID" ]]; then
  echo "killing $PID"
  kill -9 $PID
fi

echo Stoping Containers
docker container stop deli rest wall

echo Deleting/Removing Containers
docker container rm deli rest wall

echo Deleting/Removing Images
docker image rm deli rest wall

exit