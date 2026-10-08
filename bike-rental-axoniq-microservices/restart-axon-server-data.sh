docker compose down
docker volume rm bike-rental-quick-start_axon-server-data
docker volume rm bike-rental-quick-start_axon-server-events
docker volume rm bike-rental-quick-start_axon-server-config
sh ./create-axon-server-data.sh
docker compose up --build
