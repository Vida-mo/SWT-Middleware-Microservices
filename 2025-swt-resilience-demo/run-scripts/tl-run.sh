#!/bin/bash
for i in {0..9}
do
#   curl http://localhost:8080/perform &
    (time curl -s http://localhost:8080/time-limited) &> rt_output_rt_$i &
done
wait
cat rt_output_rt_*

