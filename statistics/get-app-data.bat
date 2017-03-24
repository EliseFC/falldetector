set path = %path%;D:\sqlite-tools

adb -d shell "run-as com.android.falldetector cat /data/user/0/com.android.falldetector/files/acc.csv" > acc.csv

adb -d shell "run-as com.android.falldetector cat /data/data/com.android.falldetector/databases/DetectInform.db" > DetectInform.db

sqlite3 -header -csv DetectInform.db "SELECT * FROM HISTORY;" > out.csv
