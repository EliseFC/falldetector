* Team members
Rui Duan
Fei Chen
Qiyan Wang

* GitHub
https://github.com/EliseFC/FallDetector

* Functionalities implemented in assignment 3
  1. store fall events detected in SQLite database
  2. display the database records in the History Fragment, and keep the ListView up-to-date when the database changes
  3. use GraphView's realtime chart to visualize the square sum of the accelerations
  4. in Statistics Fragment, add total falls detected, true positive count, and false positive count, and keep them up-to-date when the database changes
  5. in /statistics folder, script get-app-data.bat is added. It is used to fetch the app data files to the workstation PC and convert the SQLite DB to CVS format so that we can use R to analyze the accelerometer wave forms and the fall event history database
