#!/bin/bash

# Lösche alte Ausgabedateien
rm t_output_*

# Anzahl der Anfragen, die gleichzeitig gesendet werden sollen
REQUEST_COUNT=10

# URL des Endpunkts
URL="http://localhost:8080/resilient-task"

echo "Sending $REQUEST_COUNT concurrent requests to $URL"

# Sendet mehrere Anfragen parallel
for i in $(seq 1 $REQUEST_COUNT)
do
    # Curl-Anfrage im Hintergrund ausführen und Ausgabe in Datei umleiten
    (curl -s "$URL"; echo "") &> t_output_$i &
done

# Warten auf Beendigung aller Hintergrundprozesse
wait

# Konsolidieren und Anzeigen der Ausgaben
echo "Outputs from all requests:"
cat t_output_*

