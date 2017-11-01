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
	echo -ne "${RESET}Running test ${YELLOW}$folder${RESET} - ${RED}"

	# Invoke the program
	java -cp ../bin/ abl_cisc327.Main input/$folder/valid_accounts.txt output/$folder/transaction_summary.log < input/$folder/in.txt > output/$folder/out.log

	# Check the output
	diff expected/$folder/out.txt output/$folder/out.log
	OUTPUT_CODE=$?

	# Check that we actually have a file
	if [ -f expected/$folder/transaction_summary.txt ]; then
		diff expected/$folder/transaction_summary.txt output/$folder/transaction_summary.log
		TRANSACTION_CODE=$?
	else
		TRANSACTION_CODE=0
	fi

	# Output success if the test passed.
	if [ $TRANSACTION_CODE == 0 ] && [ $OUTPUT_CODE == 0 ]; then
		echo -e "${GREEN}Passed"
	fi
done
