import os
import re
from matplotlib.pylab import plt #load plot library
import numpy as np

# Process Logs

processed_logs = []
depth = 8

log_dir = "timing_performance"
for log_folder in os.listdir(log_dir):
    if log_folder == ".DS_Store":
        continue
    elif re.match(".*" + str(depth) + ".*", log_folder) is None:
         print("skip " + log_folder)
         continue
    else:
        print("read ", log_folder)

    log_folder_path = log_dir + "/" + log_folder

    print(os.listdir(log_folder_path))

    times = []

    for logfile in os.listdir(log_folder_path):
        # if logfile.endswith(".txt"):
        log_file_path = log_folder_path + "/" + logfile
        if re.match(".*" + str(depth) + ".*\.txt", log_file_path) is None:
            print("skip " + log_file_path)
            continue

        else:
            print("logfile:", log_file_path)
            file = open(log_file_path, "r")
            log = file.read()
            time_taken_str = re.findall('Time taken\: (\d*).\d*ms\.', log)
            time_taken = [int(time) for time in time_taken_str]
            print(time_taken)
            times.append(np.array(time_taken))

    print("times:")
    print(times)

    # Find min length
    min_length = len(times[0])
    for t in times:
        if len(t) < min_length:
            min_length = len(t)

    # Use all the shortest path
    processed_times = []
    for t in times:
        processed_times.append(t[:min_length])
    pt2 = np.array(processed_times)
    # Get average
    print(pt2)
    #t2 = np.array(times)
    #print("t2")
    #print(t2)

    average_time = np.mean(pt2, axis=0)
    processed_logs.append((log_folder, average_time))

    # print(processed_logs)

# Plot Results
for plog in processed_logs:
    plt.plot(plog[1])
print([plog[0] for plog in processed_logs])
plt.legend([plog[0] for plog in processed_logs])
plt.ylim((0, 3000))
plt.show()
