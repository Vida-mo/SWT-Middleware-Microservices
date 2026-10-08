#!/bin/bash
rm cbo*
for i in {0..9}
do
    (time curl -s http://localhost:8080/get; echo "") &> cboutput_$i &
    sleep 0.9
done
wait
cat cboutput_*

