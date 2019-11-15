#!/bin/bash

java -jar build/libs/flow_parser.jar "flows/ac-tpm-data-extraction-management.xml;flows/validate-process.xml;flows/reporting-promotion-extract-process-einstein.xml;flows/call-heroku-api.xml" abi-reporting-promotion-einstein output.json
