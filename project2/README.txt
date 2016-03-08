Author: Yihong Zhou (yzhou8), Mengwen Li (mli2)

Python version used: 2.7

External libraries used: 
"numpy" was used to perform array arithmetics.
"matplotlib" was used to plot the data points when we were testing.

To run our implementation, use the following command:
python ann.py <input file name>
The default hidden node number is 5 and the default hold out percentage is 0.2.
To specifiy hidden node number, use the following command:
python ann.py h <hidden node number> <input file name>
To specifiy hold out percentage, use the following command:
python ann.py p <hold out percentage> <input file name>
To specifiy both, use the following command:
python ann.py h <hidden node number> p <hold out percentage> <input file name>