#!/bin/bash

RED="\033[0;31m"
GREEN="\033[0;32m"
YELLOW="\033[0;33m"
RESET="\033[0;0m"

# Clean up from previous runs
rm -r output

# Run a test for each folder
for folder in $(ls -v input); do
	# Create the output folder
	mkdir -p output/$folder
	echo -e "${RESET}Running test ${YELLOW}$folder${RESET}"

	# Invoke the program
	java -cp ../bin/ abl_cisc327.Main input/$folder/valid_accounts.txt output/$folder/transaction_summary.log < input/$folder/in.txt > output/$folder/out.log
done
