#!/bin/bash
rm bh_o*
for i in {0..9}
do
#   curl http://localhost:8080/bulkhead-perform &
    (curl -s http://localhost:8080/bulkhead-perform; echo "") &> bh_output_$i &
done
wait
cat bh_output_*

