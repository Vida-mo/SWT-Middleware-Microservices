#!/bin/bash
clear
for i in {0..9}
do
#   curl http://localhost:8080/perform &
    (time curl -s http://localhost:8080/rate-limited) &> rl_output_$i &
    sleep 0.2
done
wait
cat rl_output_*

