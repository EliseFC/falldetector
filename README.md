# FallDetector

* Team members
Rui Duan
Fei Chen
Qiyan Wang

* GitHub
https://github.com/EliseFC/FallDetector

Part of our collaborations and contributions can be seen on GitHub.

* Functionalities implemented
  - Fall detection with a simple algorithm.
  - SMS Alert on a fall event.
  - Acceleration data display.
  - Tab view in the main Activity with 4 Fragments.
  - Save sensor data to "acc.csv" file.
  - store fall events detected in SQLite database
  - display the database records in the History Fragment, and keep the ListView up-to-date when the database changes
  - use GraphView's realtime chart to visualize the square sum of the accelerations
  - in Statistics Fragment, add total falls detected, true positive count, and false positive count, and keep them up-to-date when the database changes
  - in /statistics folder, script get-app-data.bat is added. It is used to fetch the app data files to the workstation PC and convert the SQLite DB to CVS format so that we can use R to analyze the accelerometer wave forms and the fall event history database
  - validation: field test in the gym, and calculate TP, FP, TN, FN, and the related measurements.