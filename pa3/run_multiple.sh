#!/bin/sh

a="ybwc9_ri2_px2"
folder="timing_performance_d_9"

./run.sh > "$folder/$a/log_"$a"_0.txt"
./run.sh > "$folder/$a/log_"$a"_1.txt"
./run.sh > "$folder/$a/log_"$a"_2.txt"
./run.sh > "$folder/$a/log_"$a"_3.txt"
./run.sh > "$folder/$a/log_"$a"_4.txt"
./run.sh > "$folder/$a/log_"$a"_5.txt"
