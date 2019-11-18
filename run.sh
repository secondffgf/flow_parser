#!/bin/bash

# promotion
# java -jar build/libs/flow_parser.jar "flows/ac-tpm-data-extraction-management.xml;flows/validate-process.xml;flows/reporting-promotion-extract-process-einstein.xml;flows/call-heroku-api.xml" abi-reporting-promotion-einstein promotion.json

#account
java -jar build/libs/flow_parser.jar "/home/tkhom/Work/projects/abi/reporting/reporting-aggregation-process-einstein.xml;/home/tkhom/Work/projects/abi/reporting/ac-tpm-data-extraction-management.xml" abi-reporting-report-Accountplan-einstein account.json
