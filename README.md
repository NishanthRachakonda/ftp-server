# FTP Server

FTP server is a simple java server client application with commands to download files from server.

## Installation

Clone the project from [this](https://github.com/NishanthRachakonda/ftp-server) repository.
```bash
$ git clone https://github.com/NishanthRachakonda/ftp-server
```

## Setup

Setup using make.
```bash
$ make application
```

## Usage

For starting server:
```bash
$ make docker_server
$ make start_server
```

For starting client:
```bash
$ make docker_client
$ make start_client
```

For stoping server:
```bash
$ make stop_server
```

## Commands

Following commands are currently working

Command | Use
--- | ---
DIR \<dir\> | Change directory
LIST | list all files in directory
COPY \<source\> \<destinatio\> | copy file from source to destination
ZIP \<folder\> \<file.zip\> | recursive zip to copy large datasets easily. 

## License 

License via Apache License