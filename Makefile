application:
	@echo "Building application."
	mvn clean install

docker_server:
	@echo "Building server image."
	docker-compose build server

docker_client:
	@echo "Building client image."
	docker-compose build client

start_server:
	@echo "Starting server."
	docker-compose up server

start_client:
	@echo "Starting client."
	docker run -it ftp/client:latest

stop_server:
	@echo "Stop server."
	docker-compose stop