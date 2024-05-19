Dataset - sta-f-83, courses = 139\
If slots = 13 and shift * iters>=12 I observe no clashes, why?\
My criteria for timetable was 0 clashes and strictly more than 10 courses
Timetables are written in the file /src/sta-f-83_13_slots_0_clash.txt\
0.139*139=19 records were recorded in the file.

Dataset - uta-s-92, courses = 622\
Slots = 30, it's hard to make assumption about shift * iters\
My criteria for timetable was 0 clashes and strictly more than 15 courses
Timetables are written in the file /src/uta-s-92_30_slots_0_clash.txt\
0.139*139=86 records were recorded in the file.

Unsure what getTrainingCapacity is for?\
I'm not sure that I have implemented unitUpdate correctly and the logic related under neurons and patterns\
Because after unitUpdate I'm getting much worse results than before\
I have updated the logic of a unitUpdate, and now I'm getting much better results.\
The model with various combinations of shifts and iterations results in much fewer clashes than without training. Results are even better for a greater number of cycles.\
I'm not sure that index number of a timetable in the dataset for training is even needed.\
