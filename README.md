# Surgical Process Monitoring Microservice

![Release](https://github.com/smartoperatingblock/surgical-process-monitoring-microservice/actions/workflows/build-and-deploy.yml/badge.svg?style=plastic)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=plastic)](https://opensource.org/licenses/MIT)
![Version](https://img.shields.io/github/v/release/smartoperatingblock/surgical-process-monitoring-microservice?style=plastic)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgical-process-monitoring-microservice&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgical-process-monitoring-microservice)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgical-process-monitoring-microservice&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgical-process-monitoring-microservice)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgical-process-monitoring-microservice&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgical-process-monitoring-microservice)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgical-process-monitoring-microservice&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgical-process-monitoring-microservice)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgical-process-monitoring-microservice&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgical-process-monitoring-microservice)

The microservice responsible to monitor and collect information of surgical processes inside the Operating Block.

## Usage
1. Provide a `.env` file with the following variables:
   - `SURGICAL_PROCESS_MICROSERVICE_MONGODB_URL`: the mongodb connection string
   - `BOOTSTRAP_SERVER_URL`: the kafka connection endpoint
   - `SCHEMA_REGISTRY_URL`: the schema registry url
2. Run the container with the command:
    ```bash
    docker run ghcr.io/smartoperatingblock/surgical-process-monitoring-microservice:latest
    ```

## Documentation
- Check out the website [here](https://smartoperatingblock.github.io/surgical-process-monitoring-microservice)
- Check out the _REST-API_ documentation [here](https://smartoperatingblock.github.io/surgical-process-monitoring-microservice/documentation/openapi-doc)
- Check out the Code documentation here [here](https://smartoperatingblock.github.io/surgical-process-monitoring-microservice/documentation/code-doc)
